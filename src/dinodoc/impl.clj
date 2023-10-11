(ns dinodoc.impl
  (:require
   [clojure.string :as str]))

(def link-regex #"\[([^\]]*)\]\(([^\)]+)\)")

(defn replace-links [content {:keys [source link-map]}]
  (let [path-to-source (get link-map source)
        _ (assert (some? path-to-source))
        segment-count (dec (count (str/split path-to-source #"/")))
        path-to-root (if (pos? segment-count)
                       (str (->> (repeat segment-count "..")
                                 (str/join "/")))
                       ".")]
    (str/replace content link-regex
                 (fn [[match title href]]
                   (if-some [replacement (get link-map href)]
                     (str "[" title "](" path-to-root "/" replacement ")")
                     match)))))

(defn doc-tree->file-map
  ([doc-tree]
   (doc-tree->file-map doc-tree {} ""))
  ([doc-tree file-map parent-path]
   (reduce (fn [m [label {:keys [file]} & xs]]
             (let [path (str parent-path label)]
               (doc-tree->file-map xs
                                   (cond-> m
                                     file (assoc file path))
                                   (str path "/"))))

           file-map
           doc-tree)))
