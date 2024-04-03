(ns doc
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [dinodoc.api :as dinodoc]))

(def output-path "docs")

(dinodoc/generate
 {:inputs [{:path "."}]
  :output-path output-path})

;; Convert Markdown to Asciidoc
(doseq [file (fs/glob output-path "**.md")]
  ;; Disabling `auto_identifiers` feature so that explicit heading ids are used to make links to vars work properly.
  (shell "pandoc -t asciidoc-auto_identifiers" (str file) "-o" (str (fs/strip-ext file) ".adoc")))

  ;; https://github.com/asciidoctor/kramdown-asciidoc
  ; (shell "kramdoc" (str file)))

(shutdown-agents)
