(ns dinodoc.contextmapper
  (:require [dinodoc.contextmapper.impl :as impl]))

(defn generate [{:keys [model-file output-path]}]
  (let [jmodel (impl/load-model model-file)]
    (impl/render-model {:jmodel jmodel
                        :output-path output-path})))
