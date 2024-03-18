(ns dinodoc.dokka
  (:require
   [babashka.fs :as fs]
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.string :as str])
  (:import
   (kotlinx.coroutines Dispatchers)
   (org.jetbrains.dokka MainKt)))

; args ["-sourceSet" "-src src/main"
;       "-outputDir" "docs"]
(defn- dokka-main [args]
  (MainKt/main (into-array String args)))

;; Port of MainKt/main as an entrypoint for potential customization
#_(defn dokka-main [args]
    (let [globalArguments (GlobalArguments. (into-array String args))
          configuration (MainKt/initializeConfiguration globalArguments)]
      (.generate (DokkaGenerator. configuration (.getLogger globalArguments)))))

;; By default Dokka finalizes coroutines when it finishes generating which shuts down all threads.
;; We don't want that because we need to run fixups on the output afterwards.
;; There is a `finalizeCoroutines` option which can be set to false, but it is not exposed via CLI.
;; We cannot modify configuration objects because they have immutable fields and only way to modify them is via Kotlin Scope functions. However, I am not aware of a way to use scoped functions from Clojure.
;; As a workaround, we can use JSON config to pass the `finalizeCoroutines` option. The downside is we need to do it via a temporary file on disk.
(defn- dokka [opts]
  (let [config-file (str (fs/create-temp-file))]
    (with-open [writer (io/writer config-file)]
      (json/generate-stream opts writer))
    (try
      (dokka-main [config-file])
      (finally
        (fs/delete config-file)))))

(defn- finalize-coroutines []
  (.shutdown Dispatchers/INSTANCE))

(defn generate [{:keys [source-paths output-path]}]
  (let [;; Options reference: https://kotlinlang.org/docs/dokka-cli.html#json-configuration
        opts {:finalizeCoroutines false
              :outputDir output-path
              :sourceSets [{:sourceSetID {:scopeId "moduleName"
                                          :sourceSetName "main"}
                            :sourceRoots source-paths}]}]
    (fs/delete-tree output-path)
    (fs/create-dirs output-path)
    (dokka opts)
    ;; Replace <br> to make output compatible with MDX
    (doseq [f (fs/glob output-path "**.md")]
      (fs/update-file (fs/file f) str/replace "<br>" "<br/>"))
    ;; Finalize coroutines manually since we set finalizeCoroutines to false
    (finalize-coroutines)))
