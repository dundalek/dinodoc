(ns dinodoc.api
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.generator :as generator]
   [dinodoc.impl.cljapi :as cljapi]
   [dinodoc.impl.core :as impl]
   [dinodoc.impl.quickdoc.api :as qd]))

(defn generate
  "Generates documentation for given inputs. Input options can be also specified as top-level keys that will be shared by all inputs.

Options:

* `:output-path` - Directory where to output the documentation (required)
* `:api-mode` - Set to `:global` to render API docs for inputs combined in a single namespace hierarchy (default: separate for each input)
* `:resolve-apilink` - EXPERIMENTAL - Function used to resolve API links from logical value to physical href
* `:inputs` - List of strings/paths/files or maps of:
  * `:path`
  * `:output-path` - Directory where to output documentation of the input relative to top level `:output-path` (default: last segment of `:path`)
  * `:source-paths` - Directories with source files for API docs, relative to `:path` (default: `[\"src\"]`)
  * `:doc-path` - Directory with markdown articles, relative to `:path` (default `\"doc\"`)
  * `:doc-tree` - Tree of articles in the format of `:cljdoc.doc/tree` (default: tries to read `:doc-path`/`cljdoc.edn`)
  * `:github/repo` - Link to Github repo used for generating  \"Edit this page\" links, for example `https://github.com/org/repo` (string)
  * `:git/branch` - Default git branch, used for \"Edit this page\" links (string)
  * `:edit-url-fn` - Function that gets a `filename` parameter and returns a custom edit url, signature: `(fn [filename])`"
  [opts]
  (let [{:keys [inputs api-mode resolve-apilink] root-outdir :output-path :as root-opts} opts
        inputs (->> (if (seq inputs) inputs ["."])
                    (map #(impl/normalize-input % root-opts)))
        global-analysis (when (= api-mode :global)
                          (impl/run-analysis (mapcat :source-paths inputs)))
        inputs (->> inputs
                    (map (fn [input]
                           (if (:generator input)
                             (assoc input :generator-output-path (:output-path input))
                             (assoc input
                                    :generator-output-path "api"
                                    :cljapi-generator true
                                    :generator
                                    (cljapi/make-generator
                                     (-> (select-keys input [:source-paths :path :github/repo :git/branch :api-docs-dir])
                                         (assoc :global-analysis global-analysis))))))))
        resolve-from-generators (impl/make-resolve-link inputs)]

    (fs/delete-tree root-outdir)
    (fs/create-dirs root-outdir)

    (when (= api-mode :global)
      (let [{:keys [github/repo git/branch]} root-opts
            api-docs-dir (str root-outdir "/api")]
        (qd/quickdoc
         {:analysis global-analysis
          ;; TODO filename-remove-prefix
          ; :filename-remove-prefix path
          :outdir api-docs-dir
          :git/branch branch
          :github/repo repo})

        (when (fs/exists? api-docs-dir)
          (spit (str api-docs-dir "/_category_.json")
                "{\"label\":\"API\"}"))))

    (doseq [{:keys [generator]} inputs]
      (generator/prepare-index generator))

    (doseq [input inputs]
      (if-not (:cljapi-generator input)
        (let [{:keys [generator generator-output-path]} input]
          (generator/generate generator {:output-path generator-output-path}))

        (let [{:keys [path doc-tree output-path path-to-root-fn edit-url-fn]} input
              doc-tree-opts {:root-path output-path
                             :parent-path output-path
                             :input-path path
                             :edit-url-fn edit-url-fn}
              doc-tree-ops (impl/process-doc-tree-pure doc-tree-opts doc-tree)
              file-map (->> doc-tree-ops
                            (filter #(= (first %) :copy-with-frontmatter))
                            (map (fn [[_ {:keys [file target]}]]
                                   [file (str/replace-first target (str output-path "/") "")]))
                            (into {}))
              resolve-apilink (or resolve-apilink resolve-from-generators)
              link-resolver (fn [file-path s]
                              (when-some [target (resolve-apilink s)]
                                ;; pathname:// workaround for non-absolute links to HTML assets
                                ;; https://github.com/facebook/docusaurus/issues/3894#issuecomment-740622170
                                (let [html-target? (re-find #"\.html$|\.html#.*$" target)]
                                  (str (when html-target? "pathname://") (path-to-root-fn file-path) "/" target))))]

          (impl/process-doc-tree! doc-tree-ops {:file-map file-map
                                                :link-resolver link-resolver})
          (when (not= api-mode :global)
            (println "Generating" path)
            (let [{:keys [generator generator-output-path]} input]
              (generator/generate generator {:output-path generator-output-path}))))))))

(comment
  (generate
   {:inputs ["test/resources/fixlinks-sample"]
    :output-path "tmp"}))
