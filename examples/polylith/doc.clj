(ns doc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.api :as dinodoc]))

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
                        ;; Example of specifying :source-paths to only include interface.clj files for API docs
                        :source-paths (->> (fs/glob path "**/interface.clj")
                                           (map #(fs/relativize path %)))
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
