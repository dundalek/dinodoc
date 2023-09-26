(ns dinodoc.core
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   [quickdoc.api :as qd]
   [slugify.core :refer [slugify]]
   [clj-yaml.core :as yaml]))

(defn- copy-with-frontmatter [src target data]
  (spit (fs/file target)
        (str "---\n"
             (yaml/generate-string data)
             "---\n\n"
             (slurp (fs/file src)))))

(defn- process-doc-tree
  ([opts items]
   (doseq [[i item] (map-indexed list items)]
     (process-doc-tree opts i item)))
  ([opts i item]
   (let [{:keys [parent-path input-path root?]} opts
         [label {:keys [file]} & children] item
         path (str parent-path "/" (slugify label))
         file-path (if (and root? (zero? i))
                     parent-path
                     path)]
     (fs/create-dirs path)
     (fs/copy (str input-path "/" file) (str file-path "/index.md"))
     (when file
       (copy-with-frontmatter (str input-path "/" file)
                              (str file-path "/index.md")
                              {:sidebar_position i
                               :sidebar_label label}))
     #_(spit (str file-path "/_category_.json")
             (json/generate-string
              {:position i
               :label label}))
     (process-doc-tree {:parent-path path
                        :input-path input-path
                        :root? false}
                       children))))

(defn generate [{:keys [paths outdir] :as opts}]
  (let [path (first paths)
        source-paths [(str path "/src")]
        api-docs-dir (str outdir "/api")]

    (fs/delete-tree outdir)
    (fs/create-dir outdir)

    (->> (edn/read-string (slurp (str path "/doc/cljdoc.edn")))
         :cljdoc.doc/tree
         (process-doc-tree {:parent-path outdir
                            :input-path path
                            :root? true}))

    (qd/quickdoc
     {:source-paths source-paths
      :filename-remove-prefix path
      :outdir api-docs-dir
      :git/branch (:git/branch opts)
      :github/repo (:github/repo opts)})

    (spit (str api-docs-dir "/_category_.json")
          "{\"label\":\"API\"}")))
