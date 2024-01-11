(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [quickdoc.api]
   [dinodoc.core :as dinodoc]))

(def doc-tree [["Polylith" {:file "readme.md"}]])

(dinodoc/generate
 {:output-path "docs"
  :inputs (concat
           [{:path "polylith"
             :output-path "."
             :doc-tree doc-tree}]
           (->> (fs/list-dir "polylith/components")
                (map (fn [path]
                       {:path path
                        :output-path (str "Components/" (fs/file-name path))}))))
  :github/repo "https://github.com/polyfy/polylith"
  :git/branch "master"})

(fs/copy-tree "polylith/images" "docs/images")

(fs/update-file "docs/index.md"
                (fn [input]
                  (-> input
                      (str/replace #"(<img[^>]+\")>" "$1/>")
                      (str/replace #"src=\"(images/[^\"]+)\"" "src={require(\"./$1\").default}"))))

(shutdown-agents)
