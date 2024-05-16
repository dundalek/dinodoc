(ns dinodoc.api
  (:require
   [babashka.fs :as fs]
   [dinodoc.generator :as generator]
   [dinodoc.impl.articles :as articles]
   [dinodoc.impl.cljapi :as cljapi]
   [dinodoc.impl.core :as impl]
   [dinodoc.impl.quickdoc.impl :as qimpl]))

(def ^:private default-api-prefix "api")

(defn generate
  "Generates documentation for given inputs. Input options can be also specified as top-level keys that will be shared by all inputs.

Options:

* `:output-path` - Directory where to output the documentation (required)
* `:api-mode` - Set to `:global` to render API docs for inputs combined in a single namespace hierarchy (default: separate for each input)
* `:resolve-apilink` - EXPERIMENTAL - Function used to resolve API links from logical value to physical href
* `:inputs` - List of strings/paths/files or maps of:
  * `:path`
  * `:output-path` - Directory where to output documentation of the input relative to top level `:output-path` (default: last segment of `:path`)
  * `:generator` - Instance implementing `dinodoc.generator/Generator` that will be used for this input
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
                         (let [repos (->> inputs (map :github/repo) set)
                               branches (->> inputs (map :git/branch) set)
                               opts (merge {:github/repo (when (= (count repos) 1) (first repos))
                                            :git/branch (when (= (count branches) 1) (first branches))}
                                           root-opts)
                               var-source-opts (impl/input->var-source-opts opts)]
                           [{:generator-output-path (str root-outdir "/" default-api-prefix)
                             :generator-output-prefix default-api-prefix
                             :generator (cljapi/make-generator
                                         {:source-paths (mapcat :source-paths inputs)
                                          :path ""
                                          :var-source-fn #(qimpl/var-source % var-source-opts)})}])
                         (->> inputs
                              (remove :generator)
                              (map (fn [{:keys [output-path path source-paths] :as input}]
                                     (let [var-source-opts (impl/input->var-source-opts input)]
                                       {:generator-output-path (str output-path "/" default-api-prefix)
                                        :generator-output-prefix default-api-prefix
                                        :generator
                                        (cljapi/make-generator
                                         {:source-paths source-paths
                                          :path path
                                          :var-source-fn #(qimpl/var-source % var-source-opts)})})))))
        article-generators (->> inputs
                                (remove :generator)
                                (map (fn [input]
                                       {:generator (articles/make-generator
                                                    (impl/input->article-opts input))})))
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
