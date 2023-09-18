(ns quickdoc.impl
  {:no-doc true}
  (:require
   [clojure.edn :as edn]
   [clojure.pprint :as pprint]
   [clojure.string :as str]))

(def backticks-and-wikilinks-pattern
  #"`(.*?)`|\[\[(.*?)\]\]")

(defn debug [& xs]
  (binding [*out* *err*]
    (apply println xs)))

(defn- var-filter [var]
  (let [mvar (merge (:meta var) var)]
    (and (not (:no-doc mvar))
         (not (:skip-wiki mvar))
         (not (:private var))
         (not (= 'clojure.core/defrecord (or (:defined-by->lint-as var)
                                             (:defined-by var)))))))

;; Adapted from `hiccup.core/escape-html`
(defn escape-html
  "Change special characters into HTML character entities."
  [text]
  (.. ^String text
      (replace "&"  "&amp;")
      (replace "<"  "&lt;")
      (replace ">"  "&gt;")
      (replace "\"" "&quot;")
      (replace "'"  "&apos;")))

(defn mini-markdown [s]
  (str/replace s #"`(.*?)`"
               (fn [[_ s]]
                 (format "<code>%s</code>" (escape-html s)))))

(defn var-summary
  "Returns the first sentence of the var's DOC'umentation, if any

  It collapses all continuous whitespaces to a single space."
  [{:keys [doc] :as _var}]
  (when-not (str/blank? doc)
    (let [norm (-> (str/replace doc #"\s+" " ")
                   str/trim)
          sen (or (some->> (re-find #"(.*?\.)[\s]|(.*?\.)$" norm)
                           rest
                           (some identity))
                  (str norm "."))]
      (mini-markdown sen))))

(defn var-source [var {:keys [github/repo git/branch
                              filename-remove-prefix
                              filename-add-prefix
                              source-uri
                              filename-fn]
                       :or {filename-fn identity
                            source-uri "{repo}/blob/{branch}/{filename}#L{row}-L{end-row}"}}]
  (let [var-filename (:filename var)
        filename
        (cond
          (and filename-remove-prefix (str/starts-with? var-filename filename-remove-prefix))
          (str/replace-first var-filename filename-remove-prefix "")
          filename-add-prefix (str filename-add-prefix var-filename)
          :else var-filename)
        filename (filename-fn filename)]
    (->
     source-uri
     (str/replace "{repo}" (str repo))
     (str/replace "{branch}" branch)
     (str/replace "{filename}" filename)
     (str/replace "{row}" (str (:row var)))
     (str/replace "{col}" (str (:col var)))
     (str/replace "{end-row}" (str (:end-row var)))
     (str/replace "{end-col}" (str (:end-col var))))))

(defn extract-var-links [var-regex docstring]
  (->> (re-seq var-regex docstring)
       (map (fn [[raw & inners]]
              [raw (some identity inners)]))))

(defn print-docstring [ns->vars current-ns docstring opts]
  (println
   (if-some [var-regex (:var-regex opts)]
     (reduce (fn [docstring [raw inner]]
               (cond
                  ;; Looks qualified
                 (str/includes? inner "/")
                 (let [split (str/split inner #"/")]
                   (if (and (= (count split) 2)
                            (get-in ns->vars [(symbol (first split))
                                              (symbol (second split))]))
                     (str/replace docstring raw (format "[`%s`](#%s)" inner inner))
                     docstring))
                  ;; Not qualified, maybe a namespace
                 (contains? ns->vars (symbol inner))
                 (str/replace docstring raw (format "[`%s`](#%s)" inner inner))
                  ;; Not qualified, maybe a var in the current namespace
                 (get-in ns->vars [current-ns (symbol inner)])
                 (str/replace docstring raw (format "[`%s`](#%s/%s)" inner current-ns inner))
                  ;; Just regular markdown backticks
                 :else
                 docstring))
             docstring
             (extract-var-links var-regex docstring))
     docstring)))

(defn print-metadata-line [m]
  (let [macro? (and (:arglist-strs m)
                    (:macro m))
        protocol? (= (:defined-by m) 'clojure.core/defprotocol)
        {:keys [deprecated added]} m
        deprecated-str (cond
                         (true? deprecated) "deprecated"
                         deprecated (str "deprecated in " deprecated))
        added-str (cond
                    (true? added) "added"
                    added (str "added in " added))
        metadata (cond-> []
                   macro? (conj "macro")
                   protocol? (conj "protocol")
                   (:dynamic (meta (:name m))) (conj "dynamic")
                   deprecated-str (conj deprecated-str)
                   added-str (conj added-str))]
    (when (seq metadata)
      (println)
      (println (str "*" (str/join " | " metadata) "*"))
      (println))))

(defn print-var-metadata-line [var]
  (print-metadata-line var))

(defn print-ns-metadata-line [ns]
  (print-metadata-line (select-keys ns [:deprecated :added])))

(defn print-var [ns->vars ns-name var _source {:keys [collapse-vars] :as opts}]
  (println)
  (when (var-filter var)
    (when collapse-vars (println "<details>\n\n"))
    (when collapse-vars
      (println (str "<summary><code>" (:name var) "</code>"
                    (when-let [summary (var-summary var)]
                      (str " - " summary)))
               "</summary>\n\n"))
    (print "##" (format "<a name=\"%s/%s\">`%s`</a>"
                        ns-name
                        (:name var)
                        (:name var)))
    ;; I found the icon too big and drawing too much attention, so I reverted to
    ;; printing the source link in a <sub> below again
    #_(println (format " [📃](%s)"
                       (var-source var opts)))
    (println (format "<a name=\"%s/%s\"></a>"
                     ns-name
                     (:name var)))
    (when-let [arg-lists (or (when-let [quoted-arglists (-> var :meta :arglists)]
                               (if (and (seq? quoted-arglists)
                                        (= 'quote (first quoted-arglists)))
                                 (second quoted-arglists)
                                 quoted-arglists))
                             (seq (:arglist-strs var)))]
      (println "``` clojure\n")
      (doseq [arglist arg-lists]
        (let [arglist (try (edn/read-string arglist)
                           (catch Exception _ arglist))
              arglist (if (coll? arglist)
                        (cons (:name var) arglist)
                        (list (str (:name var) arglist)))
              arglist (binding [pprint/*print-miser-width* nil
                                pprint/*print-right-margin* 120]
                        (with-out-str (pprint/pprint arglist)))]
          (print arglist)))
      (println "```"))
    (println)
    (when-let [doc (:doc var)]
      (println)
      (print-docstring ns->vars ns-name doc opts))
    (print-var-metadata-line var)
    ;; This needs to be in its own paragraph since the docstring may end with an indented list
    (println (format "<p><sub><a href=\"%s\">Source</a></sub></p>" (var-source var opts)))
    (when collapse-vars (println "</details>\n\n"))))

(defn print-ns-frontmatter [ns-name]
  (println "---")
  ;; https://docusaurus.io/docs/api/plugins/@docusaurus/plugin-content-docs#markdown-front-matter
  ;; only show last segment in sidebar because it is shown in hierarchy
  (println "sidebar_label:" (last (str/split (str ns-name) #"\.")))
  (println "title:" ns-name)
  ; also could hide title completely for greater control
  ; (println "hide_title: true")
  ;; increase max heading level because we are using h4 for protocol members
  (println "toc_min_heading_level: 2")
  (println "toc_max_heading_level: 4")
  (println "---")
  (println))

(defn print-namespace [ns-defs ns->vars ns-name vars opts overrides]
  (let [ns (get-in ns-defs [ns-name 0])
        filename (:filename ns)
        source (try (slurp filename)
                    (catch Exception _ nil))
        mns (get ns :meta)
        overriden-ns (get overrides ns-name)
        mns (merge mns overriden-ns)]
    (when (and (not (:no-doc mns))
               (not (:skip-wiki mns)))
      (print-ns-frontmatter ns-name)
      (let [var-map (zipmap (map :name vars) vars)
            var-map (merge-with merge var-map overriden-ns)]
        (when-let [vars (seq (filter var-filter (vals var-map)))]
          (let [ana (group-by :name vars)
                collapse-nss (:collapse-nss opts)]
            (when collapse-nss (println "<details>\n\n"))
            (when collapse-nss (println "<summary><code>" ns-name "</code></summary>\n\n"))
            (println (format "# <a name=\"%s\">%s</a>\n\n" ns-name ns-name))
            (when-let [doc (:doc ns)]
              (print-docstring ns->vars ns-name doc opts))
            (print-ns-metadata-line ns)
            (println "\n\n")
            (run! (fn [[_ vars]]
                    (let [var (last vars)]
                      (print-var ns->vars ns-name var source opts)))
                  (sort-by first ana))
            (when collapse-nss (println "</details>\n\n"))))))))

(defn print-toc* [nss ns-defs _opts overrides]
  (println "# Table of contents")
  (doseq [[ns-name vars] (sort-by first nss)]
    (let [ns (get-in ns-defs [ns-name 0])
          overriden-ns (get overrides ns-name)
          mns (get ns :meta)
          mns (merge mns overriden-ns)]
      (when (and (not (:no-doc mns))
                 (not (:skip-wiki mns)))
        (println "- " (format "[`%s`](#%s) %s"
                              ns-name
                              ns-name
                              (str (when-let [summary (var-summary ns)]
                                     (str " - " summary)))))
        (let [vars (group-by :name vars)
              vars (sort-by first vars)]
          (doseq [[var-name var-infos] vars]
            (let [v (last var-infos)]
              (when (var-filter (merge v (get overriden-ns var-name)))
                (println
                 "    - "
                 (str (format "[`%s`](#%s)"
                              var-name
                              (str ns-name "/" var-name))
                      (when-let [summary (var-summary v)]
                        (str " - " summary))))))))))))

(defn print-toc [nss ns-defs opts overrides]
  (when (:toc opts)
    (print-toc* nss ns-defs opts overrides)))
