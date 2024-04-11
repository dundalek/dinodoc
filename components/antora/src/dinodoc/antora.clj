(ns dinodoc.antora
  (:require
   [babashka.fs :as fs]
   [clojure.java.shell :as shell]
   [clojure.string :as str]))

(defn- generate-navigation-items [path base-path level]
  (->> (fs/list-dir path)
       (sort-by fs/file-name)
       (mapcat (fn [f]
                 (cond
                   (fs/directory? f)
                   (cons (str level " " (fs/file-name f))
                         (generate-navigation-items f base-path (str level "*")))

                   (= (fs/extension f) "adoc")
                   [(str level
                         " xref:" (str/replace (str f) base-path "")
                         "[]")])))))

(defn generate-navigation [path]
  (let [base-path (str/replace (str path) #"/?$" "/")]
    (->> (generate-navigation-items path base-path "*")
         (str/join "\n"))))

(defn- replace-md-adoc-links [input]
  ;; naive regex implementation, it would be better to replace over AST
  (str/replace input #"\]\(([^)]+)\)"
               (fn [[_ link]]
                 (let [replacement (cond-> link
                                     (not (str/includes? link "://"))
                                     (str/replace #"\.md$" ".adoc"))]
                   (str "](" replacement ")")))))

(defn md->adoc [input]
  (-> (shell/sh "pandoc" "--from" "markdown"
                ;; Disabling `auto_identifiers` feature so that explicit heading ids are used to make links to vars work properly.
                "--to" "asciidoc-auto_identifiers"
                "--shift-heading-level-by=-1"
                "--standalone"
                :in (replace-md-adoc-links input))
      :out
      ;; Post-processing to use the `[#id]` syntax instead of legacy `[[id]]`,
      ;; otherwise asciidoctor would not parse ids starting with dash `-`.
      (str/replace #"(?m)^\[\[(.*)\]\]$" "[#$1]")))

(defn transform-directory [path]
  (doseq [file (fs/glob path "**.md")]
    (->> (slurp (fs/file file))
         (md->adoc)
         (spit (str (fs/strip-ext file) ".adoc")))))
