(ns dinodoc.contextmapper
  (:require
   [dinodoc.contextmapper.impl :as impl]
   [dinodoc.generator :as generator]))

(deftype ^:private ContextMapperGenerator [opts ^:volatile-mutable jmodel ^:volatile-mutable index]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [model-file]} opts]
      (set! jmodel (impl/load-model model-file))
      (set! index (impl/build-index jmodel))))
  (resolve-link [_ target]
    (get index target))
  (generate [_ {:keys [output-path]}]
    (impl/render-model {:jmodel jmodel
                        :output-path output-path})))

(defn make-generator
  "Options:

- `:model-file` - path to .cml file"
  [opts]
  (->ContextMapperGenerator opts nil nil))
