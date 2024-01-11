(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.core :as dinodoc]))

(dinodoc/generate
 {:inputs (concat
           [{:path "reitit"
             :output-path "."}]
           (fs/list-dir "reitit/modules"))
  :output-path "docs"
  :api-mode :global
  ;; per-input edit urls seem broken in global api-mode, so git auto detection also does not work
  :github/repo "https://github.com/metosin/reitit"
  :git/branch "master"})

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
