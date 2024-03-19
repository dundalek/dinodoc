(ns experiments
  (:require
   [babashka.fs :as fs]
   [cheshire.core :as json]
   [clojure.java.io :as io])
  (:import
   (kotlinx.coroutines Dispatchers)
   (org.jetbrains.dokka MainKt GlobalArguments DokkaGenerator Timer)
   (org.jetbrains.dokka.base.generation SingleModuleGeneration)))

;; Exploration of the underlying data model
;; https://kotlin.github.io/dokka/1.9.20/developer_guide/architecture/architecture_overview/

;; Port of org.jetbrains.dokka.base.generation.SingleModuleGeneration/generate
(defn generate [generation timer context]
    ; (.generate generation timer))
  (let [_ (.report timer "Validity check")
        _ (.validityCheck generation context)

            ; // Step 1: translate sources into documentables & transform documentables (change internally)
        _ (.report timer "Creating documentation models")
        modulesFromPlatforms (.createDocumentationModels generation)

        _ (.report timer "Transforming documentation model before merging")
        transformedDocumentationBeforeMerge (.transformDocumentationModelBeforeMerge generation modulesFromPlatforms)

        _ (.report timer "Merging documentation models")
        transformedDocumentationAfterMerge (.mergeDocumentationModels generation transformedDocumentationBeforeMerge)

        _ (.report timer "Transforming documentation model after merging")
        transformedDocumentation (.transformDocumentationModelAfterMerge generation transformedDocumentationAfterMerge)

        _ (.report timer "Creating pages")
        pages (.createPages generation transformedDocumentation)

        _ (.report timer "Transforming pages")
        transformedPages (.transformPages generation pages)]

    ;; def as vars for further REPL exploration in the comment block below
    (def docs transformedDocumentation)
    (def pg transformedPages)))

;; Port of org.jetbrains.dokka.MainKt/main as an entrypoint for potential customization

(defn dokka-main [args]
  (let [globalArguments (GlobalArguments. (into-array String args))
        configuration (MainKt/initializeConfiguration globalArguments)
        logger (.getLogger globalArguments)
        generator (DokkaGenerator. configuration logger)
        context (.initializePlugins generator configuration logger [])
        generation (SingleModuleGeneration. context)
        timer (Timer. (System/currentTimeMillis) logger)]
    (generate generation timer context)))

;; By default Dokka finalizes coroutines when it finishes generating which shuts down all threads.
;; We don't want that because we need to run fixups on the output afterwards.
;; There is a `finalizeCoroutines` option which can be set to false, but it is not exposed via CLI.
;; We cannot modify configuration objects because they have immutable fields and only way to modify them is via Kotlin Scope functions. However, I am not aware of a way to use scoped functions from Clojure.
;; As a workaround, we can use JSON config to pass the `finalizeCoroutines` option. The downside is we need to do it via a temporary file on disk.
(defn dokka [opts]
  (let [config-file (str (fs/create-temp-file))]
    (with-open [writer (io/writer config-file)]
      (json/generate-stream opts writer))
    (try
      (dokka-main [config-file])
      (finally
        (fs/delete config-file)))))

(defn finalize-coroutines []
  (.shutdown Dispatchers/INSTANCE))

(comment
  (let [target-path "docs"
        source-paths ["../../examples/kotlin/src/main"]
        ;; Options reference: https://kotlinlang.org/docs/dokka-cli.html#json-configuration
        opts {:finalizeCoroutines false
              :outputDir target-path
              :sourceSets [{:sourceSetID {:scopeId "moduleName"
                                          :sourceSetName "main"}
                            :sourceRoots source-paths}]}]
    ; (fs/delete-tree target-path)
    ; (fs/create-dirs target-path)
    (dokka opts))
    ;; Replace <br> to make output compatible with MDX
    ; (doseq [f (fs/glob target-path "**.md")]
    ;   (fs/update-file (fs/file f) str/replace "<br>" "<br/>")))
    ;; Finalize coroutines manually since we set finalizeCoroutines to false
    ; (finalize-coroutines)))

  (require '[clojure.reflect :refer [reflect]])

  ;; DModule
  (reflect docs)

  (.getName docs)

  ;; DPackage
  (def pkg (first (.getChildren docs)))

  ;; "demo////PointingToDeclaration/"
  (.getDri pkg)

  ;; can look up by kind, or getChildren that returns all in collection
  (count (.getFunctions pkg))
  (count (.getProperties pkg))
  (count (.getClasslikes pkg))
  (count (.getTypealiases pkg))

  (.getFunctions pkg)
  (.getClasslikes pkg)

  (->> (.getChildren pkg)
       (map #(.getDri %)))

  ;; Pages
  ;; org.jetbrains.dokka.pages.ModulePageNode
  (class pg)

  (count (.getChildren pg))

  ;; org.jetbrains.dokka.pages.ContentGroup
  (.getContent pg)

  ;; org.jetbrains.dokka.pages.PackagePageNode
  (first (.getChildren pg))
  (count (.getChildren (first (.getChildren pg)))))
