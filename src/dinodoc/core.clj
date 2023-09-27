(ns dinodoc.core
  (:require
   [babashka.fs :as fs]
   [clj-yaml.core :as yaml]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [quickdoc.api :as qd]
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

(defn- copy-with-frontmatter [src target data]
  (spit (fs/file target)
        (str "---\n"
             (yaml/generate-string data)
             "---\n\n"
             (slurp (fs/file src)))))

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
   (let [{:keys [parent-path input-path root?]} opts
         [label {:keys [file]} & children] item
         path (str parent-path "/" (some-> label slugify))
         file-path (cond
                     (and root? (zero? i)) (str parent-path "/index.md")
                     label (str path "/index.md")
                     :else (str parent-path "/" (strip-docusaurus-path (fs/file-name file))))]
     (fs/create-dirs (fs/parent file-path))
     (when file
       (copy-with-frontmatter (str input-path "/" file)
                              file-path
                              (cond-> {:sidebar_position i}
                                label (assoc :sidebar_label label))))
     (process-doc-tree {:parent-path path
                        :input-path input-path
                        :root? false}
                       children))))

(defn generate [{:keys [paths outdir] root-outdir :outdir :as opts}]
  (fs/delete-tree root-outdir)
  (fs/create-dir root-outdir)
  (doseq [input paths]
    (let [{:keys [github/repo git/branch
                  path doc-path doc-tree outdir]} (merge (select-keys opts [:github/repo :git/branch])
                                                         (if (map? input) input {:path input}))
          path (or (some-> path str) ".")
          outdir (or outdir (fs/file-name path))
          outdir (str root-outdir "/" outdir)
          doc-path (str path "/" (or doc-path "doc"))
          source-paths [(str path "/src")]
          cljdoc-path (str  doc-path "/cljdoc.edn")
          api-docs-dir (str outdir "/api")
          readme-path (str path "/README.md")
          doc-tree (or doc-tree
                       (when (fs/exists? cljdoc-path)
                         (->> (edn/read-string (slurp cljdoc-path))
                              :cljdoc.doc/tree)))]
      (println "Generating" path)

      (let [processed-doc-file? (set (collect-doc-files doc-tree))
            doc-files (->> (concat (when (fs/exists? readme-path)
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
        (process-doc-tree {:parent-path outdir
                           :input-path path
                           :root? true}
                          (concat doc-tree
                                  doc-files)))

      (qd/quickdoc
       {:source-paths source-paths
        :filename-remove-prefix path
        :outdir api-docs-dir
        :git/branch branch
        :github/repo repo})

      (when (fs/exists? api-docs-dir)
        (spit (str api-docs-dir "/_category_.json")
              "{\"label\":\"API\"}")))))

(comment
  (def doc-tree
    (-> (slurp "../promesa/promesa/doc/cljdoc.edn")
        (edn/read-string)
        :cljdoc.doc/tree)))
