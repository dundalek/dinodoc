(ns dinodoc.antora
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]))

(defn- generate-navigation-items [path base-path level]
  (->> (fs/list-dir path)
       (sort-by fs/file-name)
       (mapcat (fn [f]
                 (cond
                   (fs/directory? f)
                   (cons (str level " " (fs/file-name f))
                         (generate-navigation-items f base-path (str level "*")))

                   (= (fs/extension f) "adoc")
                   [(str level
                         " xref:" (str/replace (str f) base-path "")
                         "[]")])))))

(defn generate-navigation [path]
  (let [base-path (str/replace (str path) #"/?$" "/")]
    (->> (generate-navigation-items path base-path "*")
         (str/join "\n"))))
