(ns ^:no-doc dinodoc.antora.impl
  (:require
   [babashka.fs :as fs]
   [clojure.java.shell :as shell]
   [clojure.string :as str]))

(defn generate-navigation-items [path base-path level]
  (->> (fs/list-dir path)
       (sort-by fs/file-name)
       (mapcat (fn [f]
                 (cond
                   (fs/directory? f)
                   (let [[child other] (fs/glob f "*.adoc")]
                     (if (and (nil? other) (= (some-> child fs/file-name) "index.adoc"))
                       (generate-navigation-items f base-path level)
                       (cons (str level " " (fs/file-name f))
                             (generate-navigation-items f base-path (str level "*")))))

                   (= (fs/extension f) "adoc")
                   [(str level
                         " xref:" (str/replace (str f) base-path "")
                         "[]")])))))

(defn- replace-md-adoc-links [input]
  ;; naive regex implementation, it would be better to replace over AST
  (str/replace input #"\]\(([^)]+)\)"
               (fn [[_ link]]
                 (let [replacement (cond-> link
                                     (not (str/includes? link "://"))
                                     (str/replace #"\.md$" ".adoc"))]
                   (str "](" replacement ")")))))

(defn- replace-heading-ids [input]
  ;; Post-processing to use the `[#id]` syntax instead of legacy `[[id]]`,
  ;; otherwise asciidoctor would not parse ids starting with dash `-`.
  ;;
  ;; Pandoc hardcodes `[[` in output: https://github.com/jgm/pandoc/blob/c29c18a0038b62718cf3133247177e3cb8ebf871/src/Text/Pandoc/Writers/AsciiDoc.hs#L200
  ;; AsciiDoctor checks `BlockAnchorRx` regex https://github.com/asciidoctor/asciidoctor/blob/935a0a3a2fe05ba7d239d8e774ada20df5879599/lib/asciidoctor/parser.rb#L2051
  ;; which cannot start with alphanum or underscore: https://github.com/asciidoctor/asciidoctor/blob/935a0a3a2fe05ba7d239d8e774ada20df5879599/lib/asciidoctor/rx.rb#L165
  (str/replace input #"(?m)^\[\[(.*)\]\]$" "[#$1]"))

(defn md->adoc [input]
  (-> (shell/sh "pandoc" "--from" "markdown"
                ;; Disabling `auto_identifiers` feature so that explicit heading ids are used to make links to vars work properly.
                "--to" "asciidoc-auto_identifiers"
                "--shift-heading-level-by=-1"
                "--standalone"
                :in (replace-md-adoc-links input))
      :out
      replace-heading-ids))
