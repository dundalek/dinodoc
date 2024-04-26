(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.contextive :as contextive]))

(dinodoc/generate
 {:inputs [{:path "."}
           {:generator (contextive/make-generator {:definitions-file ".contextive/definitions.yml"})
            :output-path "glossary"}]
  :output-path "docs"})

(shutdown-agents)
