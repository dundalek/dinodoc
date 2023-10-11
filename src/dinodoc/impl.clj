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
