(ns dinodoc.antora
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.antora.impl :as impl]))

(defn generate-navigation
  "Given a directory with .adoc files returns a string of auto-generated AsciiDoc content to be used for a navigation sidebar tree."
  [path]
  (let [base-path (str/replace (str path) #"/?$" "/")]
    (->> (impl/generate-navigation-items path base-path "*")
         (str/join "\n"))))

(defn transform-directory
  "Transforms .md files in a given directory to .adoc files that can be rendered as pages with Antora."
  [path]
  (doseq [file (fs/glob path "**.md")]
    (->> (slurp (fs/file file))
         (impl/md->adoc)
         (spit (str (fs/strip-ext file) ".adoc")))))
