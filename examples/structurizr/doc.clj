(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.structurizr :as structurizr]))

(dinodoc/generate
 {:output-path "docs"
  :inputs [{:path "."}
           {:generator (structurizr/make-generator
                        {:workspace-file "examples/dsl/big-bank-plc/workspace.dsl"})
            :output-path "big-bank-plc"}
           {:generator (structurizr/make-generator
                        {:workspace-file "examples/dsl/financial-risk-system/workspace.dsl"})
            :output-path "financial-risk-system"}]})

(shutdown-agents)
