(ns dinodoc.core
  (:require
   [babashka.fs :as fs]
   [cheshire.core :as json]
   [clj-kondo.core :as clj-kondo]
   [clj-yaml.core :as yaml]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [quickdoc.api :as qd]
   [quickdoc.impl :as impl]
   [slugify.core :refer [slugify]]))

(defn slugify-path [path]
  (let [[_ path extension] (or (re-matches #"(.*)(\.\p{Alnum}+)" path)
                               [nil path ""])]
    (str (->> (str/split path #"/")
              (map slugify)
              (str/join "/"))
         extension)))

(defn strip-docusaurus-path [path]
  (str/replace path "?" ""))

(comment
  (slugify-path "A A/B (x)/c")
  (slugify-path "A A/My Doc.md"))

(defn make-ns->vars [analysis]
  (let [var-defs (:var-definitions analysis)
        nss (group-by :ns var-defs)
        ns->vars (update-vals nss (comp set (partial map :name)))]
    ns->vars))

(defn- copy-with-frontmatter [src target data analysis]
  (let [content (slurp (fs/file src))
        current-ns nil
        path-prefix "./api"
        ns->vars (make-ns->vars analysis)
        format-href (fn [target-ns target-var]
                      (let [formatted-ns (str path-prefix "/" (impl/absolute-namespace-link target-ns))]
                        (impl/format-href formatted-ns target-var)))
        content (impl/format-docstring* ns->vars current-ns format-href content {:var-regex impl/backticks-and-wikilinks-pattern})]
    (spit (fs/file target)
          (str "---\n"
               (yaml/generate-string data)
               "---\n\n"
               content))))

(defn- collect-doc-files [doc-tree]
  (cond
    (sequential? doc-tree)
    (->> doc-tree
         (map collect-doc-files)
         (apply concat))

    (map? doc-tree) [(:file doc-tree)]
    :else nil))

(defn- process-doc-tree
  ([opts items]
   (doseq [[i item] (map-indexed list items)]
     (process-doc-tree opts i item)))
  ([opts i item]
   (let [{:keys [parent-path input-path make-edit-url analysis root?]} opts
         [label {:keys [file]} & children] item
         path (str parent-path "/" (some-> label slugify))
         file-path (cond
                     (and root? (zero? i)) (str parent-path "/index.md")
                     (and label (seq children)) (str path "/index.md")
                     label (str parent-path "/" (slugify label) "." (fs/extension file))
                     :else (str parent-path "/" (strip-docusaurus-path (fs/file-name file))))]
     (fs/create-dirs (fs/parent file-path))
     (cond
       file (copy-with-frontmatter (str input-path "/" file)
                                   file-path
                                   (cond-> {:sidebar_position i
                                            :custom_edit_url (make-edit-url file)}
                                     label (assoc :sidebar_label label))
                                   analysis)
       label (spit (str path "/_category_.json")
                   (json/generate-string {:position i  :label label})))
     (process-doc-tree {:parent-path path
                        :input-path input-path
                        :make-edit-url make-edit-url
                        :analysis analysis
                        :root? false}
                       children))))

(defn- normalize-input [input root-opts]
  (let [root-outdir (:outdir root-opts)
        {:keys [github/repo git/branch make-edit-url
                include-readme? path doc-path doc-tree outdir]} (merge (select-keys root-opts [:github/repo :git/branch])
                                                                       (if (map? input) input {:path input}))
        path (or (some-> path str) ".")
        outdir (or outdir (fs/file-name path))
        outdir (str root-outdir "/" outdir)
        doc-path (str path "/" (or doc-path "doc"))
        source-paths [(str path "/src")]
        cljdoc-path (str  doc-path "/cljdoc.edn")
        api-docs-dir (str outdir "/api")
        readme-path (when-not (false? include-readme?)
                      (str path "/README.md"))
        make-edit-url (or make-edit-url
                          (fn [filename]
                            (str repo "/tree/" branch "/" filename)))
        doc-tree (or doc-tree
                     (when (fs/exists? cljdoc-path)
                       (->> (edn/read-string (slurp cljdoc-path))
                            :cljdoc.doc/tree)))
        processed-doc-file? (set (collect-doc-files doc-tree))
        doc-files (->> (concat (when (and readme-path
                                          (fs/exists? readme-path))
                                 [readme-path])
                               (->> (fs/glob doc-path "*.md")
                                    (map str)))
                       (map (fn [file]
                              (if (str/starts-with? file (str path "/"))
                                (str/replace-first file (str path "/") "")
                                file)))
                       (remove processed-doc-file?)
                       (sort)
                       (map (fn [file]
                              [nil {:file file}])))]
    {:path path
     :readme-path readme-path
     :doc-tree (concat doc-tree
                       doc-files)
     :outdir outdir
     :source-paths source-paths
     :api-docs-dir api-docs-dir
     :github/repo repo
     :git/branch branch
     :make-edit-url make-edit-url}))

(defn run-analysis [source-paths]
  (-> (clj-kondo/run! {:lint source-paths
                       :config {:skip-comments true
                                :output {:analysis
                                         {:arglists true
                                          :var-definitions {:meta [:no-doc
                                                                   :skip-wiki
                                                                   :arglists]}
                                          :namespace-definitions {:meta [:no-doc
                                                                         :skip-wiki]}}}}})
      :analysis))

(defn generate [{:keys [paths api-docs] root-outdir :outdir :as root-opts}]
  (let [inputs (->> paths
                    (map #(normalize-input % root-opts)))]
    (fs/delete-tree root-outdir)
    (fs/create-dir root-outdir)

    (doseq [input inputs]
      (let [{:keys [path doc-tree outdir source-paths api-docs-dir github/repo git/branch make-edit-url]} input
            analysis (run-analysis source-paths)]

        (process-doc-tree {:parent-path outdir
                           :input-path path
                           :root? true
                           :make-edit-url make-edit-url
                           :analysis analysis}
                          doc-tree)

        (when (not= api-docs :global)
          (println "Generating" path)
          (qd/quickdoc
           {;:source-paths source-paths
            :analysis analysis
            :filename-remove-prefix path
            :outdir api-docs-dir
            :git/branch branch
            :github/repo repo})

          (when (fs/exists? api-docs-dir)
            (spit (str api-docs-dir "/_category_.json")
                  "{\"label\":\"API\"}")))))

    (when (= api-docs :global)
      (let [source-paths (mapcat :source-paths inputs)
            {:keys [github/repo git/branch]} root-opts
            api-docs-dir (str root-outdir "/api")]
        (qd/quickdoc
         {:source-paths source-paths
          ;; TODO filename-remove-prefix
          ; :filename-remove-prefix path
          :outdir api-docs-dir
          :git/branch branch
          :github/repo repo})

        (when (fs/exists? api-docs-dir)
          (spit (str api-docs-dir "/_category_.json")
                "{\"label\":\"API\"}"))))))

(comment
  (def doc-tree
    (-> (slurp "../promesa/promesa/doc/cljdoc.edn")
        (edn/read-string)
        :cljdoc.doc/tree))

  (defn run [source-paths]
    (:analysis (clj-kondo/run! {:lint source-paths
                                :config {:skip-comments true
                                         :output {:analysis
                                                  {:arglists true
                                                   :var-definitions {:meta [:no-doc
                                                                            :skip-wiki
                                                                            :arglists]}
                                                   :namespace-definitions {:meta [:no-doc
                                                                                  :skip-wiki]}}}}})))

  (let [a (run ["reitit/modules/reitit-core/src"])
        b (run ["reitit/modules/reitit-swagger/src"])

        ab (run ["reitit/modules/reitit-core/src"
                 "reitit/modules/reitit-swagger/src"])]
   ; (=
   ;  (set (concat  (:var-definitions a)
   ;                (:var-definitions b)))
   ;
   ;  (set (:var-definitions ab)))

    (=
     (set (concat  (:namespace-definitions a)
                   (:namespace-definitions b)))

     (set (:namespace-definitions ab))))

  (tap> (run ["../samples/a/src"
              "../samples/b/src"])))

