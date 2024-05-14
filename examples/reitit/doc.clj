(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.api :as dinodoc]))

(dinodoc/generate
 {:inputs (concat
           [{:path "reitit"
             :output-path "."}]
           (fs/list-dir "reitit/modules"))
  :path "reitit"
  :output-path "docs"
  :api-mode :global})

(fs/copy-tree "reitit/doc/images" "docs/images")

;; causes issues with images, dinodoc should replace image links as well
(fs/update-file "docs/misc/performance.md"
                (fn [content]
                  (str/replace content #"\]\(images/" "](../images/")))

(fs/update-file "docs/basics/name-based-routing.md"
                (fn [content]
                  (str/replace content
                               "[`reitit.impl/IntoString`](../api/reitit/impl/#IntoString)"
                               "`reitit.impl/IntoString`")))

(shutdown-agents)
