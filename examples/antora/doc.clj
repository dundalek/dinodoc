(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.antora :as antora]))

(def output-path "docs/pages")

(dinodoc/generate
 {:inputs [{:path "."}]
  :output-path output-path})

;; Convert Markdown to Asciidoc
(antora/transform-directory output-path)

(spit "docs/nav.adoc" (antora/generate-navigation output-path))

(shutdown-agents)
