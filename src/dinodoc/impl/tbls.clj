(ns dinodoc.impl.tbls
  ^{:no-doc true}
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.generator :as generator]
   [dinodoc.impl.fs :refer [create-local-temp-dir]]
   [babashka.process :refer [shell]]))

(defn resolve-link [dir target]
  (let [segments (str/split target #":")
        target-path (->> (concat (butlast segments)
                                 [(str (last segments) ".md")])
                         (str/join "/"))]
    (if (fs/exists? (fs/file dir target-path))
      target-path
      (let [target-pattern (->> (concat (butlast segments)
                                        [(str "*." (last segments) ".md")])
                                (str/join "/"))]
        (when-some [filename (first (fs/glob dir target-pattern))]
          (str/replace-first filename (str dir "/") ""))))))

(deftype TblsGenerator [opts tmp-dir dbdoc-dir]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [dsn]} opts]
      (try
        ;; tbls seems to have `docPath` option to use different output path than `dbdoc`.
        ;; But it seems to be only available when using yaml config but not passable via CLI,
        ;; so lets use a cwd insted.
        ;;
        ;; Because of that when using `sqlite:` the path must be absolute.
        ;; Could some auto handling or at least an assertion.
        (shell {:dir tmp-dir} "tbls" "doc" "--dsn" dsn "--rm-dist")
        ;; Uncomment to use Mermaid for diagrams:
        ; "--er-format" "mermaid")

        (catch java.io.IOException e
          (throw (ex-info "Something went wrong, please make sure to have `tbls` program installed." {} e))))))
  (resolve-link [_ target]
    (resolve-link dbdoc-dir target))
  (generate [_ {:keys [output-path]}]
    (let [{:keys [title]} opts]
      (fs/move dbdoc-dir output-path)

      (fs/update-file
       (str output-path "/README.md")
       (fn [content]
         (str "---\nsidebar_position: 0\n---\n\n"
              (cond-> content
                title (str/replace-first #"(?m)^# .*$" (str "# " title))))))

      ;; Strip out distracting tbls footer, credits are in the readme
      (doseq [file (fs/glob output-path "*.md")]
        (fs/update-file
         (fs/file file)
         str/replace "---\n\n> Generated by [tbls](https://github.com/k1LoW/tbls)" "")))))

(defn make-generator [opts]
  (let [tmp-dir (str (create-local-temp-dir))
        dbdoc-dir (str tmp-dir "/dbdoc")]
    (->TblsGenerator opts tmp-dir dbdoc-dir)))
