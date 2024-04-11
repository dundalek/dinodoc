(ns doc
  (:require
   [babashka.fs :as fs]
   [dinodoc.antora :as antora]))

(def target-path "examples/modules/ROOT")
(def pages-path (str target-path "/pages"))

(fs/delete-tree pages-path)
(fs/copy-tree "../../website/docs" pages-path)
(antora/transform-directory pages-path)

(spit (str target-path "/nav.adoc") (antora/generate-navigation pages-path))

(shutdown-agents)

