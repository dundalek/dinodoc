(ns dinodoc.structurizr.impl.core
  {:no-doc true}
  (:require
   [babashka.fs :as fs]
   [clojure.java.data :as j]
   [clojure.java.io :as io]
   [clojure.string :as str])
  (:import
   [com.structurizr Workspace]
   [com.structurizr.dsl StructurizrDslParser]
   [com.structurizr.export Diagram]
   [com.structurizr.export.mermaid MermaidDiagramExporter]
   [com.structurizr.model Container Element SoftwareSystem]
   [com.structurizr.util WorkspaceUtils]
   [java.net URLEncoder]))

; (set! *warn-on-reflection* true)

;; When setting URL on element with `.setUrl`, structurizr validates that it is an absolute URL with a protocol scheme.
;; To make the generator easier to use without needing to specify domain, we use relative paths by using a placeholder that gets stripped out afterwards
(def relative-url-placeholder "http://_DINODOC_RELATIVE_URL_PLACEHOLDER_")

(defn fix-mermaid-links [s path-to-root]
  (->> (str/split s #"\n")
       (map (fn [line]
              (str/replace line #"^(\s*click [^\s]+ )([^\s]+) \"([^\s]+)\"" #_"$1\"$2\""
                           (fn [[_ prefix url title]]
                             (str prefix "\""
                                  (str/replace url relative-url-placeholder path-to-root)
                                  "\" \""
                                  (str/replace title relative-url-placeholder "")
                                  "\"")))))
       (str/join "\n")))

(comment
  (fix-mermaid-links "click 1 http://example.com/1 \"http://example.com/1\"" ""))

;; Structurizr exports mermaid diagrams with white background, which does not
;; look good with dark theme. The styles are hard-coded in mermaid exporter,
;; but we can workaround by replacing with transparent background.
(defn fix-mermaid-background [s]
  (str/replace s #"fill:#ffffff" "fill:transparent"))

(defn mermaid-diagram-exporter []
  (MermaidDiagramExporter.))

(defn render-view-diagram [^MermaidDiagramExporter exporter view path-to-root]
  (let [^Diagram diagram (.export exporter view)]
    (println
     (str "### " (.getKey diagram) "\n\n"
          "```mermaid\n"
          (-> (.getDefinition diagram)
              (fix-mermaid-background)
              (fix-mermaid-links path-to-root))
          "\n```\n"))))

(defn render-image-view [{:keys [content title key]}]
  (println "###" title)
  (println)
  (println (str "![" key "](" content ")"))
  (println))

(defn encode-url [url]
  (-> (URLEncoder/encode url)
      (str/replace "+" "%20")))

(defn element-path-segment [element]
  (str (-> (:name element)
           ;; Got an error from Docusaurus `Module not found: Error: Can't resolve`
           ;; when a name that ends up being used as file path contained a dot,
           ;; stripping it as a workaround.
           (str/replace "." ""))
       "-"
       (:id element)))

(defn set-element-urls [{:keys [^Workspace workspace workspace-edn]}]
  (let [model (.getModel workspace)
        path relative-url-placeholder]
    (doseq [element (->> workspace-edn :model :softwareSystems)]
      (let [path (str path (encode-url (element-path-segment element)) "/")
            element-obj (.getElement model (:id element))]
        (.setUrl element-obj path)
        (doseq [element (:containers element)]
          (let [path (str path  (encode-url (element-path-segment element)) "/")
                element-obj (.getElement model (:id element))]
            (.setUrl element-obj path)
            (doseq [element (:components element)]
              (let [path (str path (encode-url (element-path-segment element)) "/")
                    element-obj (.getElement model (:id element))]
                (.setUrl element-obj path)))))))))

(defn render-children-links [label children]
  (when (seq children)
    (println (str label ":"))
    (println)
    (doseq [child (sort-by :name children)]
      (let [child-path  (str "./"
                             (encode-url (element-path-segment child))
                             "/")]
        (println (str "- [" (:name child) "](" child-path ")"))))
    (println)))

(defn views-for-element [workspace-edn element-id]
  (->> [; only show systemLandscapeViews on workspace level
        ;:systemLandscapeViews
        :systemContextViews
        :containerViews
        :componentViews
        :dynamicViews
        ;; depolyment views rendered separately for a system
        ; :deploymentViews
        :filteredViews
        :customViews]
       (mapcat #(->> workspace-edn :views %))
       (filter (fn [{:keys [elements]}]
                 (some #(= (:id %) element-id) elements)))))

(defn view-by-key [^Workspace workspace key]
  (.getViewWithKey (.getViews workspace) key))

(defn render-element [ctx {:keys [element get-children render-child children-label path-to-root]}]
  (let [{:keys [workspace-edn workspace exporter output-path]} ctx
        path-segment (element-path-segment element)
        path (str output-path "/" path-segment)]
    (fs/create-dirs path)
    (with-open [out (io/writer (str path "/index.md"))]
      (binding [*out* out]
        (let [{:keys [id name description]} element]
          (println "#" name)
          (println)
          (when description
            (println description)
            (println))
          (when-some [technology (:technology element)]
            (println "Technology:" technology)
            (println))
          ;; render tags?
          (render-children-links children-label (get-children element))

          (->> (views-for-element workspace-edn id)
               (map #(view-by-key workspace (:key %)))
               (run! #(render-view-diagram exporter % path-to-root)))
          (->> workspace-edn :views :imageViews
               (filter #(= (:elementId %) id))
               (run! render-image-view))
          ;; only for software system
          (when-some [deployment-views (->> workspace-edn :views :deploymentViews
                                            (filter #(= (:softwareSystemId %) id))
                                            seq)]
            (let [environments (group-by :environment deployment-views)]
              (doseq [[env views] (sort-by key environments)]
                (println "## Deployment -" env)
                (doseq [{:keys [key description]} views]
                  (render-view-diagram exporter (view-by-key workspace key) path-to-root))))))))
    (run! #(render-child (assoc ctx :output-path path) %)
          (get-children element))))

(defn render-component [ctx component]
  (render-element ctx {:element component
                       :get-children (fn [_])
                       :render-child (fn [_])
                       :path-to-root "../../../"}))

(defn render-container [ctx container]
  (render-element ctx {:element container
                       :get-children :components
                       :render-child render-component
                       :children-label "Components"
                       :path-to-root "../../"}))

(defn render-software-system [ctx system]
  (render-element ctx {:element system
                       :get-children :containers
                       :render-child render-container
                       :children-label "Containers"
                       :path-to-root "../"}))

(defn workspace->data [workspace]
  (j/from-java workspace))

(defn render-workspace [{:keys [workspace output-path]}]
  (let [workspace-edn (workspace->data workspace)
        exporter (mermaid-diagram-exporter)
        ctx {:workspace workspace
             :workspace-edn workspace-edn
             :exporter exporter
             :output-path output-path}
        systems (->> workspace-edn
                     :model
                     :softwareSystems)]
    (set-element-urls ctx)
    (fs/delete-tree output-path)
    (fs/create-dirs output-path)
    (with-open [out (io/writer (str output-path "/index.md"))]
      (binding [*out* out]
        (println "#" (:name workspace-edn))
        (println)
        (println (:description workspace-edn))
        (println)
        (render-children-links "Systems" systems)
        (println)
        (->> workspace-edn :views :systemLandscapeViews
             (map #(view-by-key workspace (:key %)))
             (run! #(render-view-diagram exporter % "")))))
    (run! #(render-software-system ctx %) systems)))

;; Inspired by https://github.com/structurizr/cli/blob/6f40424dd6c7777150fa6f22ffc5489ec7d3ece6/src/main/java/com/structurizr/cli/AbstractCommand.java#L45
(defn load-workspace [workspace-path-string]
  (let [workspace-file (io/file workspace-path-string)]
    (if (str/ends-with? workspace-path-string ".json")
      (WorkspaceUtils/loadWorkspaceFromJson workspace-file)
      (let [parser (StructurizrDslParser.)]
        (.parse parser workspace-file)
        (.getWorkspace parser)))))

(defn build-index [workspace]
  (let [^Workspace workspace workspace]
    (->>
     (.getModel workspace)
     (.getSoftwareSystems)
     (mapcat (fn [^SoftwareSystem system]
               (cons system
                     (->> (.getContainers system)
                          (mapcat (fn [^Container container]
                                    (cons container
                                          (.getComponents container))))))))
     (map (fn [^Element element]
            [(.getName element)
             (-> (.getUrl element)
                 (str/replace-first relative-url-placeholder ""))]))
     (into {}))))

(comment
  (do
    (def workspace (load-workspace "examples/structurizr/examples/dsl/big-bank-plc/workspace.dsl"))
    (def workspace-edn (workspace->data workspace))))
