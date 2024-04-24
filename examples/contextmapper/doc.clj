(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.contextmapper :as contextmapper]))

(dinodoc/generate
 {:output-path "docs"
  :inputs [{:input "."}
           {:generator (contextmapper/make-generator
                        {:model-file "examples/Insurance-Example-Stage-5.cml"})
            :output-path "insurance-map"}]})

(shutdown-agents)
