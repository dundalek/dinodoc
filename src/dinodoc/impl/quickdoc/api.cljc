(ns dinodoc.impl.quickdoc.api
  {:no-doc true}
  (:require
   #?(:bb [babashka.pods :as pods]
      :clj [clj-kondo.core :as clj-kondo])
   [clojure.java.io :as io]
   [clojure.string :as str]
   [dinodoc.impl.quickdoc.impl :as impl]))

#?(:bb
   (or (try (requiring-resolve 'pod.borkdude.clj-kondo/run!)
            (catch Exception _ nil)) ;; pod is loaded via bb.edn
       (pods/load-pod 'clj-kondo/clj-kondo "2022.11.02")))

#?(:bb
   (require '[pod.borkdude.clj-kondo :as clj-kondo]))

(defn quickdoc
  "Generate API docs. Options:
  * `:github/repo` -  a link like `https://github.com/borkdude/quickdoc`
  * `:git/branch` - branch name for source links, default to `\"main\"`
  * `:source-uri` - source link template. Supports `{row}`, `{end-row}`, `{col}`, `{end-col}`, `{filename}`, `{branch}`, `{path}`, `{repo}`.
  * `:outfile` - file where API docs are written, or falsey if you don't need a file. Defaults to `\"API.md\"`
  * `:source-paths` - sources that are scanned for vars. Defaults to `[\"src\"]`.
  * `:toc` - generate table of contents. Defaults to `true`.
  * `:var-links` - generate links to vars within the same namespace. Defauls to `true`.
  * `:var-pattern` - detecting vars for linking, either `:backticks` (default) or `:wikilinks` (double brackets)
  * `:overrides` - overrides in the form `{namespace {:no-doc true var {:no-doc true :doc ...}}}`
  * `:filename-add-prefix` - add a prefix to the filename for source links.
  * `:filename-remove-prefix` - remove a prefix from the filename for source links.
  * `:filename-fn` - transformation of filename before it is rendered to markdown, e.g. for source links.

  Returns a map containing the generated markdown string under the key `:markdown`."
  {:org.babashka/cli
   {:coerce {:outfile (fn [s]
                        (if (or (= "false" s)
                                (= "nil" s))
                          false
                          s))
             :toc :boolean
             :var-links :boolean}
    :collect {:source-paths []}}}
  [opts]
  (let [{:as opts
         :keys [outdir
                source-paths
                analysis
                overrides]}   (merge {:git/branch   "main"
                                      :outfile      "API.md"
                                      :source-paths ["src"]
                                      :toc          true
                                      :var-links    true
                                      :var-pattern  :backticks-and-wikilinks}
                                     opts)
        opts (assoc opts :var-regex (case (:var-pattern opts)
                                      :backticks #"`(.*?)`"
                                      :wikilinks #"\[\[(.*?)\]\]"
                                      :backticks-and-wikilinks impl/backticks-and-wikilinks-pattern))
        ana (or analysis
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
        var-defs (:var-definitions ana)
        ns-defs (:namespace-definitions ana)
        ns-defs (group-by :name ns-defs)
        nss (group-by :ns var-defs)
        ns->vars (update-vals nss (comp set (partial map :name)))]
    (run! (fn [[ns-name vars]]
            (let [docs (with-out-str
                         (impl/print-namespace ns-defs ns->vars ns-name vars opts overrides))
                  outfile (str outdir "/" (str/replace ns-name #"\." "/") "/index.md")]
              (when-not (str/blank? docs)
                (io/make-parents outfile)
                (spit outfile docs))))
          (sort-by first nss))))
