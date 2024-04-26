(ns dinodoc.contextive
  (:require
   [babashka.fs :as fs]
   [clj-yaml.core :as yaml]
   [clojure.java.io :as io]
   [dinodoc.generator :as generator]
   [slugify.core :refer [slugify]]))

(deftype ContextiveGenerator [opts ^:volatile-mutable bounded-contexts ^:volatile-mutable index]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [definitions-file]} opts
          {:keys [contexts]} (yaml/parse-string (slurp definitions-file))]
      (set! bounded-contexts contexts)
      (set! index
            (->> contexts
                 (reduce (fn [m {:keys [terms] context-name :name}]
                           (reduce (fn [m {term-name :name}]
                                     (let [target (str context-name "#" (slugify term-name))]
                                       (-> m
                                           (assoc term-name target)
                                           (assoc (str context-name ":" term-name) target))))
                                   m terms))
                         {})))))
  (resolve-link [_ target]
    (get index target))
  (generate [_ {:keys [output-path]}]
    (let [path (str output-path "/index.md")]

      (fs/create-dirs (fs/parent path))
      (with-open [out (io/writer path)]
        (binding [*out* out]
          (doseq [{context-name :name} bounded-contexts]
            (println "# Bounded Contexts")
            (println)
            (println (str "- [" context-name "](" context-name "/)")))))

      (doseq [{:keys [terms domainVisionStatement] context-name :name} bounded-contexts]
        (assert (string? context-name))
        (let [path (str output-path "/" context-name "/index.md")]
          (fs/create-dirs (fs/parent path))
          (with-open [out (io/writer path)]
            (binding [*out* out]
              (println (str "# " context-name))
              (println)
              (println domainVisionStatement)
              (doseq [{:keys [definition examples] term-name :name} terms]
                (assert (string? term-name))
                (println)
                (println "##" term-name (str "{#" (slugify term-name) "}"))
                (when definition
                  (println)
                  (println definition))
                (when (seq examples)
                  (println)
                  (println "Examples:")
                  (println)
                  (doseq [example examples]
                    (println "-" example)))))))))))

(defn make-generator [opts]
  (->ContextiveGenerator opts nil nil))

(comment
  (def definitions
    (yaml/parse-string (slurp "examples/contextive/.contextive/definitions.yml")))

  (->> (:contexts definitions)
       first
       :terms
       first)

  (doto (make-generator {:definitions-file "examples/contextive/.contextive/definitions.yml"})
    (generator/prepare-index)
    (generator/generate {:output-path "examples/contextive/docs"})))



