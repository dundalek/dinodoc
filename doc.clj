(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.api :as dinodoc]))

(dinodoc/generate
 {:inputs ["."]
  :output-path "docs"
  :git/branch "main"
  :github/repo "https://github.com/dundalek/dinodoc"})

;; Fixup to change absolute URLs to relative links in readme.
;; Having absolute URLs in README.md to make them work when opened on Github.
;; For Docusaurus relative links are better for local preview and for better
;; experience because external links would get opened in a new browser tab.
(fs/update-file "docs/index.md"
                str/replace #"https://dinodoc.pages.dev" "")

(shutdown-agents)
