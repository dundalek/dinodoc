(ns
 ^:no-doc
 dinodoc.impl.cljapi
  (:require
   [babashka.fs :as fs]
   [dinodoc.generator :as generator]
   [dinodoc.impl.core :as impl]
   [dinodoc.impl.quickdoc.api :as qd]
   [dinodoc.impl.quickdoc.impl :as qimpl]))

(deftype CljapiGenerator [opts ^:volatile-mutable resolve-link-fn ^:volatile-mutable analysis]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [source-paths]} opts
          analysis' (impl/run-analysis source-paths)]
      (set! analysis analysis')
      (set! resolve-link-fn
            (let [format-href (fn [target-ns target-var]
                                (let [formatted-ns (qimpl/absolute-namespace-link target-ns)]
                                  (qimpl/format-href formatted-ns target-var)))]
              (qimpl/make-link-resolver (impl/make-ns->vars analysis) nil format-href)))))
  (resolve-link [_ target]
    (resolve-link-fn target))
  (generate [_ {:keys [output-path]}]
    (let [{:keys [path var-source-fn]} opts]
      (println "Generating" path)
      (qd/quickdoc
       {:analysis analysis
        :outdir output-path
        :var-source-fn var-source-fn})

      (when (fs/exists? output-path)
        (spit (str output-path "/_category_.json")
              "{\"label\":\"API\"}")))))

(defn make-generator [opts]
  (->CljapiGenerator opts nil nil))
