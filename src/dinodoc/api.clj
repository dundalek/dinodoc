(ns dinodoc.api
  (:require
   [babashka.fs :as fs]
   [dinodoc.generator :as generator]
   [dinodoc.impl.articles :as articles]
   [dinodoc.impl.cljapi :as cljapi]
   [dinodoc.impl.core :as impl]))

(def default-api-prefix "api")

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
        input-generators (->> inputs
                              (filter :generator)
                              (map (fn [{:keys [generator output-path output-path-prefix]}]
                                     {:generator generator
                                      :generator-output-path output-path
                                      :generator-output-prefix output-path-prefix})))
        api-generators (if (= api-mode :global)
                         [{:generator-output-path (str root-outdir "/" default-api-prefix)
                           :generator-output-prefix default-api-prefix
                           :generator (cljapi/make-generator
                                       (-> (select-keys root-opts [:github/repo :git/branch :source-uri])
                                           (assoc :source-paths (mapcat :source-paths inputs))))}]
                         (->> inputs
                              (remove :generator)
                              (map (fn [{:keys [output-path] :as input}]
                                     {:generator-output-path (str output-path "/" default-api-prefix)
                                      :generator-output-prefix default-api-prefix
                                      :generator
                                      (cljapi/make-generator
                                       (select-keys input [:source-paths :path :github/repo :git/branch :source-uri]))}))))
        article-generators (->> inputs
                                (remove :generator)
                                (map (fn [{:keys [path doc-tree output-path edit-url-fn path-to-root-fn]}]
                                       {:generator (articles/make-generator
                                                    {:path path
                                                     :doc-tree doc-tree
                                                     :output-path output-path
                                                     :edit-url-fn edit-url-fn
                                                     :path-to-root-fn path-to-root-fn})})))
        generators (concat article-generators api-generators input-generators)
        resolve-link (or resolve-apilink (impl/make-resolve-link generators))]

    (fs/delete-tree root-outdir)
    (fs/create-dirs root-outdir)

    (doseq [{:keys [generator]} generators]
      (generator/prepare-index generator))

    (doseq [{:keys [generator generator-output-path]} generators]
      (generator/generate generator {:output-path generator-output-path
                                     :resolve-link resolve-link}))))

(comment
  (generate
   {:inputs ["test/resources/fixlinks-sample"]
    :output-path "tmp"}))
