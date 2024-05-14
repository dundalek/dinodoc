(ns dinodoc.impl.core
  {:no-doc true}
  (:require
   [babashka.fs :as fs]
   [cheshire.core :as json]
   [clj-kondo.core :as clj-kondo]
   [clj-yaml.core :as yaml]
   [clojure.edn :as edn]
   [clojure.string :as str]
   [dinodoc.generator :as generator]
   [dinodoc.impl.git :as git]
   [dinodoc.impl.quickdoc.impl :as qimpl]
   [slugify.core :refer [slugify]]))

(def link-regex #"(\[[^\]]*\]\()([^\)]+)(\))")
(def reference-link-regex #"(\[[^\]]+\]: +)([^\n)]+)")
(def hash-regex #"^([^#]+)(#.*)?$")

(defn path-to-root [path]
  (let [segments (cond-> (count (re-seq #"/" path))
                   (str/starts-with? path "/") dec)]
    (if (pos? segments)
      (str/join "/" (repeat segments ".."))
      ".")))

(defn replace-links [content {:keys [source link-map]}]
  (let [path-to-source (get link-map source)
        _ (assert (some? path-to-source)
                  (pr-str [source link-map]))
        base-path (some-> (fs/parent source) str)
        root-path (path-to-root path-to-source)
        replace-fn (fn [[match prefix href suffix]]
                     (let [path-to-target (-> (if base-path
                                                (str base-path "/" href)
                                                href)
                                              fs/normalize
                                              str)
                           [_ path-to-target hash-part] (re-matches hash-regex path-to-target)
                           replacement (get link-map path-to-target)
                           replacement-path (-> (str root-path "/" replacement)
                                                (str/replace #"^\./" ""))]
                       (if replacement
                         (str prefix replacement-path hash-part suffix)
                         match)))]
    (-> content
        (str/replace link-regex replace-fn)
        (str/replace reference-link-regex replace-fn))))

(defn strip-docusaurus-path [path]
  (str/replace path "?" ""))

(defn make-ns->vars [analysis]
  (let [var-defs (:var-definitions analysis)
        nss (group-by :ns var-defs)
        ns->vars (update-vals nss (comp set (partial map :name)))]
    ns->vars))

(defn- copy-with-frontmatter [{:keys [file src target data link-resolver file-map]}]
  (let [link-resolver #(link-resolver target %)
        content (slurp (fs/file src))
        content (replace-links content {:source file
                                        :link-map file-map})
        content (qimpl/format-docstring link-resolver content {:var-regex qimpl/backticks-and-wikilinks-pattern})]
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

(defn process-doc-tree-pure
  ([opts items]
   (->> (map-indexed list items)
        (mapcat (fn [[i item]]
                  (process-doc-tree-pure opts i item)))))
  ([opts i item]
   (let [{:keys [root-path parent-path input-path edit-url-fn]} opts
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
                                                    label (assoc :sidebar_label label))}]
              label [:spit
                     (str path "/_category_.json")
                     (json/generate-string {:position i  :label label})])]
     (concat (if op [op] [])
             (process-doc-tree-pure (assoc opts :parent-path path)
                                    children)))))

(defn process-doc-tree! [ops {:keys [file-map link-resolver]}]
  (doseq [[op & args] ops]
    (case op
      :copy-with-frontmatter
      (let [[{:keys [target] :as opts}] args]
        (fs/create-dirs (fs/parent target))
        (copy-with-frontmatter (assoc opts
                                      :file-map file-map
                                      :link-resolver link-resolver)))
      :spit
      (let [[target content] args]
        (fs/create-dirs (fs/parent target))
        (spit target content)))))

(defn normalize-input [input root-opts]
  (let [root-outdir (str (fs/normalize (:output-path root-opts)))
        merged-options (merge (select-keys root-opts [:source-paths :doc-path :edit-url-fn :github/repo :git/branch])
                              (if (map? input) input {:path input}))
        {:keys [github/repo git/branch edit-url-fn source-paths
                path doc-path doc-tree output-path
                generator]} merged-options
        path (or (some-> path str) ".")
        outdir (or output-path (fs/file-name path))
        outdir (str (fs/normalize (str root-outdir "/" outdir)))
        doc-path (str path "/" (or doc-path "doc"))
        source-paths (->> (or source-paths ["src"])
                          (map #(str path "/" %)))
        cljdoc-path (str  doc-path "/cljdoc.edn")
        path-to-root-fn (if (= (:api-mode root-opts) :global)
                          (fn [file-path]
                            (path-to-root (str/replace-first file-path root-outdir "")))
                          (fn [file-path]
                            (path-to-root (str/replace-first file-path outdir ""))))
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
    ;; Following needs some untangling, we are mixing common options with clj api generator and articles generator options
    {:path path
     :doc-tree (concat doc-tree
                       doc-files)
     :output-path outdir
     :output-path-prefix (str/replace-first outdir (str root-outdir "/") "")
     :source-paths source-paths
     :path-to-root-fn path-to-root-fn

     :github/repo repo
     :git/branch branch

     :edit-url-fn edit-url-fn
     :generator generator}))

(defn input->var-source-opts [{:keys [gihub/repo git/branch]}]
  (let [source-uri (if (and repo branch)
                     "{repo}/blob/{branch}/{filename}#L{row}-L{end-row}"
                     "")]
    {:git/branch branch
     :github/repo repo
     :source-uri source-uri}))

(defn make-resolve-link [generator-inputs]
  (fn [target]
    ;; TODO: handle multiple resolved candidates
    (some (fn [{:keys [generator generator-output-prefix]}]
            (some->> (generator/resolve-link generator target)
                     (str generator-output-prefix "/")))
          generator-inputs)))

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
