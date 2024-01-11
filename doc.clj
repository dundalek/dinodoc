(ns doc
  (:require
   [dinodoc.api :as dinodoc]))

(dinodoc/generate
 {:inputs ["."]
  :output-path "docs"
  :git/branch "main"
  :github/repo "https://github.com/dundalek/dinodoc"})

(shutdown-agents)
