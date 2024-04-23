(ns dinodoc.impl.fs
  (:require
   [babashka.fs :as fs]))

(def ^:dynamic *tmp-dir* ".cache")

(defn create-local-temp-dir []
  ;; Moving generated files from /tmp can cause issues since it can be on a different filesystem.
  ;; Helper for using the relative local directory instead.
  (fs/create-dirs *tmp-dir*)
  (fs/create-temp-dir {:dir *tmp-dir*}))
