(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.api :as dinodoc]))

(dinodoc/generate
 {:inputs [{:path "promesa"
            :output-path "."}]
  :output-path "docs"})

(fs/update-file "docs/changelog.md"
                #(str/replace % "[`promesa.impl`](./api/promesa/impl/" "`promesa.impl`"))

;; Fixups for MDX compatibility, for own documentation would fix source markdown articles directly
(fs/update-file "docs/changelog.md"
                #(str/replace % "JDK <= 8" "JDK \\<= 8"))

(fs/update-file "docs/api/promesa/core/index.md"
                #(str/replace % "(js/fetch #js {...})" "(js/fetch #js \\{...\\})"))

(fs/update-file "docs/api/promesa/exec/csp/index.md"
                #(str/replace % "(sp/<! out)" "(sp/\\<! out)"))

(shutdown-agents)
