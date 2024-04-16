(ns doc
  (:require
   [babashka.process :refer [shell]]
   [dinodoc.api :as dinodoc]
   ;; TODO: Think about public API
   [dinodoc.impl.javadoc :as javadoc]))

;; Generated assets included by using `staticDirectories` option in `docusaurus.config.js`
(def api-path "static/examples/javadoc/api")

; (apply shell "javadoc -d out" (fs/glob "src" "**.java"))
(shell "javadoc -sourcepath ../java/src/main/java -subpackages demo -d" api-path)

(dinodoc/generate
 {:inputs [{:path "."}]
  :output-path "docs"
  :resolve-apilink #(javadoc/resolve-link api-path %)})

(shutdown-agents)
