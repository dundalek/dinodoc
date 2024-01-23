(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [dinodoc.statecharts :as statecharts]))

(fs/delete-tree "docs")
(fs/create-dirs "docs")

(with-open [writer (io/writer "docs/index.md")]
  (binding [*out* writer]
    (println "---")
    (println "title: Statecharts")
    (println "---")
    (statecharts/render-machine-var
     (requiring-resolve 'example.statecharts/machine)
     {:filename-add-prefix "examples/statecharts/src/"})))

(shutdown-agents)
