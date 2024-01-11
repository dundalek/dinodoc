#!/usr/bin/env bb
;; -*- clojure -*-
;; vim: set filetype=clojure:
(ns build-examples
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]))

(defn build-and-copy-docs [source-dir target-path]
  (shell {:dir source-dir} "clojure -M:doc")
  (fs/delete-tree target-path)
  (fs/copy-tree (str source-dir "/docs") target-path))

(defn -main [& _args]
  (let [target-path "website/docs/docs"
        source-dir "."]
    (build-and-copy-docs source-dir target-path))
  (doseq [example ["polylith" "promesa" "reitit" "ring"]]
    (let [target-path (str "website/docs/examples/" example)
          source-dir (str "examples/" example)]
      (build-and-copy-docs source-dir target-path))))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
