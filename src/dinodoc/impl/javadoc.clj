(ns dinodoc.impl.javadoc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [hickory.core :as h]
   [hickory.select :as hs]))

(defn segments->path [segments]
  (str (str/join "/" segments)
       ".html"))

(defn file->id-map [file]
  (let [tree (-> (slurp file)
                 h/parse h/as-hickory)]
    (->> (hs/select (hs/attr :id) tree)
         (map (comp :id :attrs))
         (keep (fn [html-id]
                 (when-some [[match id] (re-matches #"(.*)\(.*\)" html-id)]
                   [id match])))
         (into {}))))

(defn resolve-link [javadoc-path definition]
  (let [segments (str/split definition #"\.")
        target (segments->path segments)]
    (if (fs/exists? (fs/file javadoc-path target))
      target
      (let [target (segments->path (butlast segments))
            target-file (fs/file javadoc-path target)]
        (when (fs/exists? target-file)
          ;; Possible optimization: could cache/memoize by file
          (let [id-map (file->id-map target-file)]
            (when-some [anchor (get id-map (last segments))]
              (str target "#" anchor))))))))

(comment
  (def tree (-> (h/parse "<a href=\"foo\">foo</a>")
                (h/as-hickory)))

  (def tree (-> (slurp (fs/file javadoc-path "demo/Greeter.html"))
                h/parse h/as-hickory))

  (->> (hs/select (hs/attr :id) tree)
       (map (comp :id :attrs))
       (keep (fn [html-id]
               (when-some [[match id] (re-matches #"(.*)\(.*\)" html-id)]
                 [id match])))
       (into {})))
