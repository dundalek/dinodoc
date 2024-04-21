(ns dinodoc.contextmapper.impl
  {:no-doc true}
  (:require
   [babashka.fs :as fs]
   [clojure.java.data :as j]
   [clojure.java.io :as io])
  (:import
   (net.sourceforge.plantuml FileFormat FileFormatOption SourceStringReader)
   (org.contextmapper.dsl.contextMappingDSL BoundedContextType)
   (org.contextmapper.dsl.generator PlantUMLGenerator)
   (org.contextmapper.dsl.generator.plantuml PlantUMLBoundedContextClassDiagramCreator PlantUMLComponentDiagramCreator)
   (org.contextmapper.dsl.standalone ContextMapperStandaloneSetup)))

(def domain-types
  [{:id "CORE_DOMAIN" :label "Core domain"}
   {:id "SUPPORTING_DOMAIN" :label "Supporting domain"}
   {:id "GENERIC_SUBDOMAIN" :label "Generic domain"}
   {:id "UNDEFINED" :label nil}])

(def bounded-context-type->label
  {"SYSTEM" "System"
   "APPLICATION" "Application"
   "FEATURE" "Feature"
   "UNDEFINED" nil})

(defn load-model [model-file]
  (let [context-mapper (ContextMapperStandaloneSetup/getStandaloneAPI)
        resource (.loadCML context-mapper model-file)]
    (.getContextMappingModel resource)))

(defn render-plantuml-svg [source output-path]
  (let [reader (SourceStringReader. source)]
    (with-open [os (io/output-stream output-path)]
      (.generateImage reader os (FileFormatOption. FileFormat/SVG)))))

;; See possible diagram types in org.contextmapper.dsl.generator.PlantUMLGenerator
(defn context-map-diagram [jmodel]
  (-> (PlantUMLComponentDiagramCreator.)
      (.createDiagram (.getMap jmodel))))

(defn bounded-context-diagram [jbounded-context]
  (-> (PlantUMLBoundedContextClassDiagramCreator.)
      (.createDiagram jbounded-context)))

(defn bounded-context-path [bounded-context]
  (str "contexts/" (:name bounded-context)))

(defn domain-path [domain]
  (str "domains/" (:name domain)))

(defn bounded-contexts-without-teams [model]
  (->> (:boundedContexts model)
       (remove #(= (:type %) "TEAM"))))

(defn team-contexts [model]
  (->> (:boundedContexts model)
       (filter #(= (:type %) "TEAM"))))

(defn implemented-contexts-per-domain [model]
  (->> (bounded-contexts-without-teams model)
       (mapcat (fn [{:keys [implementedDomainParts] context-name :name}]
                 (->> implementedDomainParts
                      (map (fn [{:keys [name]}]
                             [name context-name])))))
       (reduce (fn [m [domain-name context-name]]
                 (update m domain-name (fnil conj #{}) context-name))
               {})))

(defn render-teams [{:keys [output-path model]} teams]
  (let [path (str output-path "/teams")
        contexts-by-team (->> model :boundedContexts
                              (mapcat (fn [{:keys [aggregates] context-name :name}]
                                        (->> aggregates
                                             (keep (fn [{:keys [owner] aggregate-name :name}]
                                                     (when owner
                                                       {:team (:name owner)
                                                        :context context-name
                                                        :aggregate aggregate-name}))))))
                              (group-by :team))]
    (fs/create-dirs path)
    (with-open [out (io/writer (str path "/index.md"))]
      (binding [*out* out]
        (println "# Teams")
        (doseq [{:keys [name]} (->> teams (sort-by :name))]
          (println)
          (println "##" name)
          (println)
          (doseq [[context-name aggregates] (->> (get contexts-by-team name)
                                                 (group-by :context)
                                                 (sort-by key))]
            (let [aggregates (->> aggregates
                                  (map :aggregate)
                                  (sort))]
              (println (str "- [" context-name "](../" (bounded-context-path {:name context-name}) ")"))
              (print (str "  - owns: "))
              (print (first aggregates))
              (doseq [aggregate (rest aggregates)]
                (print "," aggregate))
              (println))))))))

(defn render-domain [{:keys [model output-path]} domain]
  (let [path (str output-path "/" (domain-path domain))
        subdomains-map (->> (:subdomains domain)
                            (group-by :type))
        contexts-map (implemented-contexts-per-domain model)]
    (fs/create-dirs path)
    (with-open [out (io/writer (str path "/index.md"))]
      (binding [*out* out]
        (println "#" (:name domain))
        (when-some [description (:domainVisionStatement domain)]
          (println)
          (println description))
        (doseq [{:keys [id label]} domain-types]
          (let [subdomains (get subdomains-map id)]
            (doseq [subdomain subdomains]
              (println)
              (print "##" (:name subdomain))
              (println)
              (println)
              (when-some [description (:domainVisionStatement subdomain)]
                (println description)
                (println))
              (when label
                (println (str "*" label "*"))
                (println))
              (doseq [context (get contexts-map (:name subdomain))]
                (println (str "- [" context "](../../" (bounded-context-path {:name context}) ")"))))))))))

(defn render-bounded-context [{:keys [output-path]} jbounded-context]
  (let [bounded-context (j/from-java jbounded-context)
        path (str output-path "/" (bounded-context-path bounded-context))
        diagram-filename "diagram.svg"]
    (fs/create-dirs path)
    (render-plantuml-svg (bounded-context-diagram jbounded-context) (str path "/" diagram-filename))
    (with-open [out (io/writer (str path "/index.md"))]
      (binding [*out* out]
        (println "#" (:name bounded-context))
        (println)
        (when-some [description (:domainVisionStatement bounded-context)]
          (println description)
          (println))
        (when-some [label (bounded-context-type->label (:type bounded-context))]
          (println "- Type:" label))
        (when-some [responsibilities (seq (:responsibilities bounded-context))]
          (print "- Responsibilities: ")
          (print (first responsibilities))
          (doseq [responsibility (rest responsibilities)]
            (print "," responsibility))
          (println))
        (when-some [technology (:implementationTechnology bounded-context)]
          (print "- Implementation Technology: ")
          (println technology))
        (println)
        (println (str "![" (:name bounded-context) "](./" diagram-filename ")"))))))
        ;; Fields not rendered:
        ;; :evolution :knowledgeLevel - do not seem that useful
        ;; :implementedDomainParts - could be used for backlinks to domains

(defn render-model [{:keys [jmodel output-path]}]
  (let [model (j/from-java jmodel)
        bounded-contexts (bounded-contexts-without-teams model)]
    (fs/delete-tree output-path)
    (fs/create-dirs output-path)
    (with-open [out (io/writer (str output-path "/index.md"))]
      (binding [*out* out]
        (let [map-name (-> model :map :name)
              diagram-filename (str "context-map" map-name ".svg")
              source (context-map-diagram jmodel)]
          (render-plantuml-svg source (str output-path "/" diagram-filename))
          (println "---")
          (println "sidebar_position: 0")
          (println "title:" map-name)
          (println "---")
          (println)
          (println "#" map-name)
          (println)
          (println (str "![" map-name  "](./" diagram-filename ")"))
          (println)
          (println "## Domains")
          (println)
          (doseq [domain (sort-by :name (:domains model))]
            (println (str "- [" (:name domain) "](" (domain-path domain) ")")))
          (println)
          (println "## Bounded Contexts")
          (println)
          (doseq [bounded-context (sort-by :name bounded-contexts)]
            (println (str "- [" (:name bounded-context) "](" (bounded-context-path bounded-context) ")")))
          (println))))
    (let [ctx {:output-path output-path
               :model model}]
      (doseq [jbounded-context (->> (.getBoundedContexts jmodel)
                                    (remove #(= (.getType %) BoundedContextType/TEAM)))]
        (render-bounded-context ctx jbounded-context))
      (doseq [domain (:domains model)]
        (render-domain ctx domain))
      (when-some [teams (seq (team-contexts model))]
        (render-teams ctx teams)
        (spit (str output-path "/teams/_category_.json") "{\"position\":3,\"label\":\"Teams\"}")))
    (when (fs/directory? (str output-path "/domains"))
      (spit (str output-path "/domains/_category_.json") "{\"position\":1,\"label\":\"Domains\"}"))
    (when (fs/directory? (str output-path "/contexts"))
      (spit (str output-path "/contexts/_category_.json") "{\"position\":2,\"label\":\"Contexts\"}"))))

(comment
  (do
    (def model-file "examples/contextmapper/examples/Insurance-Example-Stage-5.cml")
    (def jmodel (load-model model-file))
    (def model (j/from-java jmodel)))

  (render-model {:jmodel jmodel
                 :output-path "website/docs/examples/contextmapper"})

  (let [context-mapper (ContextMapperStandaloneSetup/getStandaloneAPI)
        resource (.loadCML context-mapper model-file)]
    (.callGenerator context-mapper resource (PlantUMLGenerator.) "target/diagrams"))

  (keys model)

  (->> (:boundedContexts model)
       count)

  (->> (:boundedContexts model)
       first
       keys)

  (->> (.getBoundedContexts jmodel)
       count)

  (->> (team-contexts model)
       count)

  (->> (:domains model)
       count)

  ;; Maybe render relationships in the future
  (->> model
       :map
       :relationships
       (map (fn [rel]
              (-> rel
                  (update :upstream :name)
                  (update :downstream :name)
                  (update :participant1 :name)
                  (update :participant2 :name)
                  (dissoc :upstreamExposedAggregates :implementedDomainParts))))))
