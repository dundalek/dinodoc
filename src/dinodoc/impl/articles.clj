(ns dinodoc.impl.articles
  (:require
   [clojure.string :as str]
   [dinodoc.generator :as generator]
   [dinodoc.impl.core :as impl]))

(deftype ArticlesGenerator [opts ^:volatile-mutable state]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [input]} opts
          {:keys [path doc-tree output-path edit-url-fn]} input
          doc-tree-opts {:root-path output-path
                         :parent-path output-path
                         :input-path path
                         :edit-url-fn edit-url-fn}
          doc-tree-ops (impl/process-doc-tree-pure doc-tree-opts doc-tree)
          file-map (->> doc-tree-ops
                        (filter #(= (first %) :copy-with-frontmatter))
                        (map (fn [[_ {:keys [file target]}]]
                               [file (str/replace-first target (str output-path "/") "")]))
                        (into {}))]
      (set! state {:doc-tree-ops doc-tree-ops
                   :file-map file-map})))
  (resolve-link [_ _])
  (generate [_ {:keys [_output-path]}]
    (let [{:keys [input resolve-apilink]} opts
          {:keys [path-to-root-fn]} input
          {:keys [doc-tree-ops file-map link-resolver]} state
          link-resolver (fn [file-path s]
                          (when-some [target (resolve-apilink s)]
                            ;; pathname:// workaround for non-absolute links to HTML assets
                            ;; https://github.com/facebook/docusaurus/issues/3894#issuecomment-740622170
                            (let [html-target? (re-find #"\.html$|\.html#.*$" target)]
                              (str (when html-target? "pathname://") (path-to-root-fn file-path) "/" target))))]
      (impl/process-doc-tree! doc-tree-ops {:file-map file-map
                                            :link-resolver link-resolver}))))

(defn make-generator [opts]
  (->ArticlesGenerator opts nil))
