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

(defn md->adoc [input]
  (-> (shell/sh "pandoc" "--from" "markdown" "--to" "asciidoc-auto_identifiers"
                "--shift-heading-level-by=-1"
                "--standalone"
                :in input)
      :out
      ;; Post-processing to use the `[#id]` syntax instead of legacy `[[id]]`,
      ;; otherwise asciidoctor would not parse ids starting with dash `-`.
      (str/replace #"(?m)^\[\[(.*)\]\]$" "[#$1]")))
