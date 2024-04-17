(ns doc
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [dinodoc.api :as dinodoc]
   [dinodoc.impl.rustdoc :as rustdoc]))

;; Generated assets included by using `staticDirectories` option in `docusaurus.config.js`
(def rustdoc-path "target/doc")
(def api-path "static/examples/rust/api")

(shell "cargo doc --no-deps")
(fs/delete-tree api-path)
(fs/move rustdoc-path api-path)

(dinodoc/generate
 {:inputs [{:path "."}]
  :output-path "docs"
  :resolve-apilink #(rustdoc/resolve-link api-path %)})

(shutdown-agents)
