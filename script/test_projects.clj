(ns test-projects
  (:require
   [clj-kondo.core :as clj-kondo]
   [clojure.java.io :as io]
   [clojure.java.shell :refer [sh]]
   [clojure.string :as str]
   [quickdoc.api]
   [dinodoc.core :as dinodoc]))

; (def out-path "test-output/docs/api")
;
; (do
;   (sh "rm" "-rf" out-path)
;   (sh "mkdir" "-p" out-path))
;
; (do
;   ;; Just overwriting when iterating otherwise docusaurus watcher gets confused
;   ;; For clean slate also remove out-path above, but then docusaurus needs to be restarted
;   (spit (str out-path "/index.md")
;         "---\ntitle: Namespaces\n---\n\nHere can be auto-generated overview")
;   (quickdoc.api/quickdoc
;    {:git/branch "master"
;     :github/repo "https://github.com/funcool/promesa"
;     :source-paths ["test-projects/promesa/src"]
;     :outdir out-path
;     :filename-remove-prefix "test-projects/promesa/"})
;   (quickdoc.api/quickdoc
;    {:git/branch "master"
;     :github/repo "https://github.com/weavejester/codox"
;     :source-paths ["test-projects/codox/example/src"]
;     :outdir out-path
;     :filename-remove-prefix "test-projects/codox/"})
;   (quickdoc.api/quickdoc
;    {:git/branch "master"
;     :github/repo "https://github.com/borkdude/quickdoc"
;     :source-paths ["test-projects/samples/src"]
;     :outdir out-path
;     :filename-remove-prefix "test-projects/samples"}))
;
; (shutdown-agents)

;; Specifying different repos for different inputs in global mode is broken
(dinodoc/generate
 {:paths [{:path "test-projects/promesa"
           :github/repo "https://github.com/funcool/promesa"}
          {:path "test-projects/codox/example"
           :github/repo "https://github.com/weavejester/codox"}
          {:path "test-projects/samples"
           :github/repo "https://github.com/dundalek/dinodoc"}]
  :outdir "test-output/docs"
  :api-docs :global
  :git/branch "master"})

(comment
  (let [source-paths ["codox/example/src" "quickdoc/test-resources"]]
    (def ana (-> (clj-kondo/run! {:lint source-paths
                                  :config {:skip-comments true
                                           :output {:analysis
                                                    {:arglists true
                                                     :var-definitions {:meta [:no-doc
                                                                              :skip-wiki
                                                                              :arglists]}
                                                     :namespace-definitions {:meta [:no-doc
                                                                                    :skip-wiki]}}}}})
                 :analysis)))

  (let [projects [{:name "codox"
                   :source-paths ["codox/example/src"]}
                  {:name "quickdoc"
                   :source-paths ["quickdoc/test-resources"]}]
        prefix-pattern (re-pattern (->> projects
                                        (mapcat :source-paths)
                                        (map #(str "^" %))
                                        (str/join "|")))
        filename->prefix #(re-find prefix-pattern (:filename %))
        prefix->project (reduce (fn [m {:keys [name source-paths]}]
                                  (reduce (fn [m path]
                                            (assoc m path name))
                                          m
                                          source-paths))
                                {}
                                projects)
        filename->project #(-> % filename->prefix prefix->project)]
    (->> ana
         :namespace-definitions
         (map filename->project))))

(comment
  (quickdoc.api/quickdoc
   {:git/branch "master"
    :github/repo "https://github.com/borkdude/quickdoc"
    :source-paths ["quickdoc/test-resources"]
   ; :outfile (str out-path "/" "promesa" ".md")
    :outdir out-path
    :filename-remove-prefix "quickdoc/"})

 ; munge: https://github.com/clojure/clojure/blob/4090f405466ea90bbaf3addbe41f0a6acb164dbb/src/jvm/clojure/lang/Compiler.java#L2916

  (let [char-map (dissoc clojure.lang.Compiler/CHAR_MAP
                         \-)
        name "a->b!"]
    (->> name
         (map #(or (char-map %) %))
         (str/join ""))))

(comment
  (def sources
    (->> (.listFiles (io/file "clojure-polylith-realworld-example-app/components"))))

  (do
    (sh "rm" "-rf" out-path)
    (sh "mkdir" out-path)
    (doseq [file sources]
      (quickdoc.api/quickdoc
       {:git/branch "master"
        :github/repo "https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app"
        :source-paths  [(.getPath (io/file file "src"))]
        :outfile (str out-path "/" (.getName file) ".md")
        :filename-remove-prefix "clojure-polylith-realworld-example-app/"}))))
