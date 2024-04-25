(ns dinodoc.api
  (:require
   [babashka.fs :as fs]
   [dinodoc.generator :as generator]
   [dinodoc.impl.articles :as articles]
   [dinodoc.impl.cljapi :as cljapi]
   [dinodoc.impl.core :as impl]))

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
        generator-inputs (->> inputs
                              (keep (fn [input]
                                      (cond
                                        (:generator input)
                                        {:generator (:generator input)
                                         :generator-output-path (:output-path input)
                                         :generator-output-prefix (:output-path-prefix input)}

                                        (not= api-mode :global)
                                        {:generator-output-path (:api-docs-dir input)
                                         :generator-output-prefix (:api-docs-prefix input)
                                         :generator
                                         (cljapi/make-generator
                                          (select-keys input [:source-paths :path :github/repo :git/branch]))}))))
        generator-inputs (cond->> generator-inputs
                           (= api-mode :global)
                           (cons {:generator-output-path (str root-outdir "/api")
                                  :generator-output-prefix "api"
                                  :generator (cljapi/make-generator
                                              (-> (select-keys root-opts [:github/repo :git/branch])
                                                  (assoc :path nil
                                                         :source-paths (mapcat :source-paths inputs))))}))
        resolve-from-generators (impl/make-resolve-link generator-inputs)
        resolve-link (or resolve-apilink resolve-from-generators)
        article-generators (->> inputs
                                (remove :generator)
                                (map (fn [input]
                                       {:generator
                                        (articles/make-generator {:input input})})))
        generator-inputs (concat article-generators generator-inputs)]

    (fs/delete-tree root-outdir)
    (fs/create-dirs root-outdir)

    (doseq [{:keys [generator]} generator-inputs]
      (generator/prepare-index generator))

    (doseq [{:keys [generator generator-output-path]} generator-inputs]
      (generator/generate generator {:output-path generator-output-path
                                     :resolve-link resolve-link}))))

(comment
  (generate
   {:inputs ["test/resources/fixlinks-sample"]
    :output-path "tmp"}))
