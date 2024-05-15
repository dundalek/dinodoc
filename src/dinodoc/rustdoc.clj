(ns dinodoc.rustdoc
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [clojure.string :as str]
   [dinodoc.generator :as generator]
   [dinodoc.impl.fs :refer [create-local-temp-dir]]))

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

(deftype ^:private RustdocGenerator [opts tmp-dir doc-dir]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [manifest-path]} opts]
      (shell "cargo doc --no-deps --manifest-path" manifest-path "--target-dir" tmp-dir)))
  (resolve-link [_ target]
    (resolve-link doc-dir target))
  (generate [_ {:keys [output-path]}]
    (fs/move doc-dir output-path)
    (fs/delete-tree tmp-dir)))

(defn make-generator [opts]
  (let [tmp-dir (str (create-local-temp-dir))
        doc-dir (str tmp-dir "/doc")]
    (->RustdocGenerator opts tmp-dir doc-dir)))
