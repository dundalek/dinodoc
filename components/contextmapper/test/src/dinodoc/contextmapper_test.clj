(ns dinodoc.contextmapper-test
  (:require
   [clojure.test :refer [deftest]]
   [dinodoc.api :as dinodoc]
   [dinodoc.approval-helpers :as approval-helpers]
   [dinodoc.contextmapper :as contextmapper]))

(deftest minimal-model
  (let [output-path "components/contextmapper/test/output/minimal"]
    (dinodoc/generate
     {:inputs [{:generator (contextmapper/make-generator
                            {:model-file "components/contextmapper/test/resources/minimal.cml"})}]
      :output-path output-path})
    (approval-helpers/is-same? output-path)))

(deftest ^{:skip-ci "Likely a different version of Graphviz."}
  example-model
  (let [output-path "components/contextmapper/test/output/example"]
    (dinodoc/generate
     {:inputs [{:generator (contextmapper/make-generator
                            {:model-file "components/contextmapper/test/resources/example.cml"})}]
      :output-path output-path})
    (approval-helpers/is-same? output-path)))
