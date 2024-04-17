(ns dinodoc.impl.rustdoc
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]))

(defn resolve-link [doc-path definition]
  (let [segments (str/split definition #"::")
        module-index-path (str/join "/" (concat segments ["index.html"]))]
    (if (fs/exists? (str doc-path "/" module-index-path))
      module-index-path
      (let [module-path (str/join "/" (butlast segments))
            module-dir (fs/file doc-path module-path)
            pattern (str "*." (last segments) ".html")]
        (when (fs/directory? module-dir)
          (when-some [match (first (fs/list-dir module-dir pattern))]
            (str module-path "/" (fs/file-name match))))))))
