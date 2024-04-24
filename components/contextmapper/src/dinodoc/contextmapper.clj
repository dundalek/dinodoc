(ns dinodoc.contextmapper
  (:require
   [dinodoc.contextmapper.impl :as impl]
   [dinodoc.generator :as generator]))

(deftype ContextMapperGenerator [opts]
  generator/Generator
  (prepare-index [_])
  (resolve-link [_ target])
  (generate [_ {:keys [output-path]}]
    (let [{:keys [model-file]} opts]
      (impl/generate {:model-file model-file
                      :output-path output-path}))))

(defn make-generator [opts]
  (->ContextMapperGenerator opts))
