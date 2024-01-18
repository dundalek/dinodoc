(ns doc
  (:require
   [dinodoc.structurizr :as structurizr]))

(def workspace-files
  ["examples/dsl/big-bank-plc/workspace.dsl"
   "examples/dsl/financial-risk-system/workspace.dsl"]
   ; "examples/dsl/microservices/workspace.dsl"]
  #_(fs/glob "." "examples/dsl/*/workspace.dsl"))

(doseq [workspace-file workspace-files]
  (println "Generating:" (str workspace-file))
  (structurizr/generate
   {:workspace-file (str workspace-file)
    :output-path "docs"
    :base-path "/examples/structurizr"}))
