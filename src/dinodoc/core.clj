(ns dinodoc.core
  (:require
   [babashka.fs :as fs]
   [cheshire.core :as json]
   [clj-kondo.core :as clj-kondo]
   [clj-yaml.core :as yaml]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [dinodoc.impl :refer [doc-tree->file-map replace-links path-to-root slugify-path strip-docusaurus-path]]
   [quickdoc.api :as qd]
   [quickdoc.impl :as impl]
   [dinodoc.impl.git :as git]
   [slugify.core :refer [slugify]]))

(defn- make-ns->vars [analysis]
  (let [var-defs (:var-definitions analysis)
        nss (group-by :ns var-defs)
        ns->vars (update-vals nss (comp set (partial map :name)))]
    ns->vars))

(defn- copy-with-frontmatter [{:keys [file src target data ns->vars api-path-prefix file-map]}]
  (let [content (slurp (fs/file src))
        current-ns nil
        format-href (fn [target-ns target-var]
                      (let [formatted-ns (str api-path-prefix "/" (impl/absolute-namespace-link target-ns))]
                        (impl/format-href formatted-ns target-var)))
        content (replace-links content {:source file
                                        :link-map file-map})
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

(defn- process-doc-tree-pure
  ([opts items]
   (->> (map-indexed list items)
        (mapcat (fn [[i item]]
                  (process-doc-tree-pure opts i item)))))
  ([opts i item]
   (let [{:keys [root-path parent-path input-path edit-url-fn ns->vars]} opts
         [label {:keys [file]} & children] item
         path (str parent-path "/" (some-> label slugify))
         root? (= root-path parent-path)
         file-path (cond
                     (and root? (zero? i)) (str parent-path "/index.md")
                     (and label (seq children)) (str path "/index.md")
                     label (str parent-path "/" (slugify label) "." (fs/extension file))
                     :else (str parent-path "/" (strip-docusaurus-path (fs/file-name file))))
         op (cond
              file [:copy-with-frontmatter {:file file
                                            :src (str input-path "/" file)
                                            :target file-path
                                            :data (cond-> {:sidebar_position i
                                                           :custom_edit_url (edit-url-fn file)}
                                                    label (assoc :sidebar_label label))
                                            :ns->vars ns->vars
                                            :api-path-prefix (str (path-to-root (str/replace-first file-path root-path ""))
                                                                  "/api")}]
              label (do
                      [:spit (str path "/_category_.json")
                       (json/generate-string {:position i  :label label})]))]
     (concat (if op [op] [])
             (process-doc-tree-pure (assoc opts :parent-path path)
                                    children)))))

(defn- process-doc-tree! [ops file-map]
  (doseq [[op & args] ops]
    (case op
      :copy-with-frontmatter
      (let [[{:keys [target] :as opts}] args]
        (fs/create-dirs (fs/parent target))
        (copy-with-frontmatter (assoc opts :file-map file-map)))
      :spit
      (let [[target content] args]
        (fs/create-dirs (fs/parent target))
        (spit target content)))))

(defn- normalize-input [input root-opts]
  (let [root-outdir (:output-path root-opts)
        merged-options (merge (select-keys root-opts [:source-paths :doc-path :edit-url-fn :github/repo :git/branch])
                              (if (map? input) input {:path input}))
        {:keys [github/repo git/branch edit-url-fn source-paths
                path doc-path doc-tree output-path]} merged-options
        path (or (some-> path str) ".")
        outdir (or output-path (fs/file-name path))
        outdir (str root-outdir "/" outdir)
        doc-path (str path "/" (or doc-path "doc"))
        source-paths (->> (or source-paths ["src"])
                          (map #(str path "/" %)))
        cljdoc-path (str  doc-path "/cljdoc.edn")
        api-docs-dir (str outdir "/api")
        [repo branch] (if (and repo branch)
                        [repo branch]
                        ;; potential optimization: could skip detection if `edit-url-fn` option is set
                        (let [{:keys [url branch]} (git/detect-repo-info path)]
                          [url branch]))
        edit-url-fn (or edit-url-fn
                        (fn [filename]
                          (str repo "/tree/" branch "/" filename)))
        doc-tree (or doc-tree
                     (when (fs/exists? cljdoc-path)
                       (->> (edn/read-string (slurp cljdoc-path))
                            :cljdoc.doc/tree))
                     (when (fs/exists? (str path "/README.md"))
                       [[nil {:file "README.md"}]]))
        processed-doc-file? (set (collect-doc-files doc-tree))
        doc-files (->>
                   ;; rendering files from top level for now, handle hierarchy later
                   (fs/glob doc-path "*.md")
                   (map (fn [file]
                          (str/replace-first (str file) (str path "/") "")))
                   (remove processed-doc-file?)
                   (sort)
                   (map (fn [file]
                          [nil {:file file}])))]
    {:path path
     :doc-tree (concat doc-tree
                       doc-files)
     :output-path outdir
     :source-paths source-paths
     :api-docs-dir api-docs-dir
     :github/repo repo
     :git/branch branch
     :edit-url-fn edit-url-fn}))

(defn- run-analysis [source-paths]
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

(defn generate
  "Generates documentation for given inputs. Input options can be also specified as top-level keys that will be shared by all inputs.

Options:
  * `:output-path` - Directory where to output the documentation (required)
  * `:api-mode` - Set to `:global` to render API docs for inputs combined in a single namespace hierarchy (default: separate for each input)
  * `:inputs` - List of strings/paths/files or maps of:
    * `:path`
    * `:output-path` - Directory where to output documentation of the input relative to top level `:output-path` (default: last segment of `:path`)
    * `:source-paths` - Directories with source files for API docs, relative to `:path` (default: `[\"src\"]`)
    * `:doc-path` - Directory with markdown articles, relative to `:path` (default `\"doc\"`)
    * `:doc-tree` - Tree of articles in the format of `:cljdoc.doc/tree` (default: tries to read `:doc-path`/`cljdoc.edn`)
    * `:github/repo` - Link to Github repo used for generating  \"Edit this page\" links, for example `https://github.com/org/repo` (string)
    * `:git/branch` - Default git branch, used for \"Edit this page\" links (string)
    * `:edit-url-fn` - Function that gets a `filename` parameter and returns a custom edit url, signature: `(fn [filename])`
  "
  [opts]
  (let [{:keys [inputs api-mode] root-outdir :output-path :as root-opts} opts
        inputs (->> (if (seq inputs) inputs ["."])
                    (map #(normalize-input % root-opts)))
        global-analysis (when (= api-mode :global)
                          (run-analysis (mapcat :source-paths inputs)))]
    (fs/delete-tree root-outdir)
    (fs/create-dir root-outdir)

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

    (doseq [input inputs]
      (let [{:keys [path doc-tree output-path source-paths api-docs-dir github/repo git/branch edit-url-fn]} input
            analysis (or global-analysis (run-analysis source-paths))
            doc-tree-opts {:root-path output-path
                           :parent-path output-path
                           :input-path path
                           :edit-url-fn edit-url-fn
                           :ns->vars (make-ns->vars analysis)}
            doc-tree-ops (process-doc-tree-pure doc-tree-opts doc-tree)
            new-file-map (->> doc-tree-ops
                              (filter #(= (first %) :copy-with-frontmatter))
                              (map (fn [[_ {:keys [file target]}]]
                                     [file (str/replace-first target (str output-path "/") "")]))
                              (into {}))]

        (process-doc-tree! doc-tree-ops new-file-map)

        (when (not= api-mode :global)
          (println "Generating" path)
          (qd/quickdoc
           {:analysis analysis
            :filename-remove-prefix path
            :outdir api-docs-dir
            :git/branch branch
            :github/repo repo})

          (when (fs/exists? api-docs-dir)
            (spit (str api-docs-dir "/_category_.json")
                  "{\"label\":\"API\"}")))))))

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
              "../samples/b/src"]))

  (generate
   {:paths ["test-resources/fixlinks-sample"]
    :outdir "tmp"}))
