(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.core :as dinodoc]
   [quickdoc.api]))

(def doc-tree [["Ring" {:file "wiki/Home.md"}]])

(dinodoc/generate
 {:output-path "docs"
  :inputs (concat
           [{:path "."
             :output-path "."
             :doc-path "wiki"
             :doc-tree doc-tree
             :edit-url-fn (fn [filename]
                            (str "https://github.com/ring-clojure/ring/"
                                 (fs/strip-ext filename)))}]
           (->> (fs/glob "." "projects/ring/ring*")
                (map (fn [path]
                       {:path path
                        :output-path (str "ring/" (fs/file-name path))})))
           (fs/glob "." "projects/*"))})

;; Strip out badge from the title which shows in the sidebar
(fs/update-file "docs/ring-defaults/index.md"
                str/replace #"# Ring-Defaults[^\n]*" "# Ring-Defaults")
;; Fix unquouted HTML tags that interfere with docusaurus MDX renderer
(fs/update-file "docs/ring-headers/api/ring/middleware/x-headers/index.md"
                str/replace #"<frame>, <iframe> or <object>" "`<frame>`, `<iframe>` or `<object>`")
(fs/update-file "docs/ring/ring-core/api/ring/util/parsing/index.md"
                str/replace #"<any" "&lt;any")

(shutdown-agents)
