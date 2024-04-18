(ns dinodoc.impl.tbls
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]))

(defn resolve-link [dir target]
  (let [segments (str/split target #":")
        target-path (->> (concat (butlast segments)
                                 [(str (last segments) ".md")])
                         (str/join "/"))]
    (if (fs/exists? (fs/file dir target-path))
      target-path
      (let [target-pattern (->> (concat (butlast segments)
                                        [(str "*." (last segments) ".md")])
                                (str/join "/"))]
        (when-some [filename (first (fs/glob dir target-pattern))]
          (str/replace-first filename (str dir "/") ""))))))
