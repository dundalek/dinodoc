(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.structurizr :as structurizr]))

(let [base-path "/examples/structurizr"]
  (dinodoc/generate
   {:output-path "docs"
    :inputs [{:generator (structurizr/make-generator
                          {:workspace-file "examples/dsl/big-bank-plc/workspace.dsl"
                           :base-path base-path})}
             {:generator (structurizr/make-generator
                          {:workspace-file "examples/dsl/financial-risk-system/workspace.dsl"
                           :base-path base-path})}]}))

(shutdown-agents)
