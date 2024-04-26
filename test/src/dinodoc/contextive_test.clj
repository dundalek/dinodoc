(ns dinodoc.contextive-test
  (:require
   [clojure.test :refer [deftest]]
   [dinodoc.contextive :as contextive]
   [dinodoc.api :as dinodoc]
   [dinodoc.approval-helpers :as approval]))

(deftest generator-test
  (let [output-path "test/resources/contextive/output"]
    (dinodoc/generate
     {:inputs [{:path "test/resources/contextive"
                :output-path "."}
               {:generator (contextive/make-generator
                            {:definitions-file "test/resources/contextive/definitions.yml"})
                :output-path "glossary"}]
      :output-path output-path
      :github/repo "repo"
      :git/branch "main"})

    (approval/is-same? output-path)))
