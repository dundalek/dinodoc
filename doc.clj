(ns doc
  (:require
   [dinodoc.core :as dinodoc]))

(dinodoc/generate
 {:inputs ["."]
  :output-path "docs"
  :git/branch "master"
  :github/repo "https://github.com/dundalek/dinodoc"})

(shutdown-agents)
