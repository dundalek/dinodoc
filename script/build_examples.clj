(ns build-examples
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]))

(defn build-and-copy-docs [source-dir target-path]
  (shell {:dir source-dir} "clojure -M:doc")
  (fs/delete-tree target-path)
  (fs/copy-tree (str source-dir "/docs") target-path))

(defn build-dinodoc-docs []
  (let [target-path "website/docs/docs"
        source-dir "."]
    (build-and-copy-docs source-dir target-path)))

(defn build-example-docs [example]
  (let [target-path (str "website/docs/examples/" example)
        source-dir (str "examples/" example)]
    (build-and-copy-docs source-dir target-path)))

(defn copy-test-samples []
  (let [target-path "website/docs/docs/samples"]
    (fs/delete-tree target-path)
    (fs/copy-tree "test/output/docs" target-path)))

(defn copy-antora-example []
  (let [target-path "website/build/examples/antora"]
    (fs/delete-tree target-path)
    (fs/copy-tree "examples/antora/build/site" target-path)))
