(ns doc
  (:require
   [dinodoc.contextmapper :as contextmapper]))

(contextmapper/generate
 {:output-path "docs"
  :model-file "examples/Insurance-Example-Stage-5.cml"})
