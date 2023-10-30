(ns dinodoc.impl
  {:no-doc true}
  (:require
   [clojure.string :as str]
   [babashka.fs :as fs]))

(def link-regex #"(\[[^\]]*\]\()([^\)]+)(\))")
(def reference-link-regex #"(\[[^\]]+\]: +)([^\n)]+)")
(def hash-regex #"^([^#]+)(#.*)?$")

(defn path-to-root [path]
  (let [segments (cond-> (count (re-seq #"/" path))
                   (str/starts-with? path "/") dec)]
    (if (pos? segments)
      (str/join "/" (repeat segments ".."))
      ".")))

(defn replace-links [content {:keys [source link-map]}]
  (let [path-to-source (get link-map source)
        _ (assert (some? path-to-source)
                  (pr-str [source link-map]))
        base-path (some-> (fs/parent source) str)
        root-path (path-to-root path-to-source)
        replace-fn (fn [[match prefix href suffix]]
                     (let [path-to-target (-> (if base-path
                                                (str base-path "/" href)
                                                href)
                                              fs/normalize
                                              str)
                           [_ path-to-target hash-part] (re-matches hash-regex path-to-target)
                           replacement (get link-map path-to-target)
                           replacement-path (-> (str root-path "/" replacement)
                                                (str/replace #"^\./" ""))]
                       (if replacement
                         (str prefix replacement-path hash-part suffix)
                         match)))]
    (-> content
        (str/replace link-regex replace-fn)
        (str/replace reference-link-regex replace-fn))))

(defn strip-docusaurus-path [path]
  (str/replace path "?" ""))
