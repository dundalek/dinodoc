(ns dinodoc.antora
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [dinodoc.antora.impl :as impl]))

(defn generate-navigation [path]
  (let [base-path (str/replace (str path) #"/?$" "/")]
    (->> (impl/generate-navigation-items path base-path "*")
         (str/join "\n"))))

(defn transform-directory [path]
  (doseq [file (fs/glob path "**.md")]
    (->> (slurp (fs/file file))
         (impl/md->adoc)
         (spit (str (fs/strip-ext file) ".adoc")))))
