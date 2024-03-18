(ns doc
  (:require [dinodoc.dokka :as dokka]))

(dokka/generate
 {:output-path "docs"
  :source-paths ["src/main"]})
