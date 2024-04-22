(ns dinodoc.contextmapper-test
  (:require
   [clojure.test :refer [deftest is]]
   [dinodoc.contextmapper :as contextmapper]
   [dinodoc.approval-helpers :as approval-helpers]))

(deftest minimal-model
  (let [output-path "components/contextmapper/test/output/minimal"]
    (contextmapper/generate
     {:model-file "components/contextmapper/test/resources/minimal.cml"
      :output-path output-path})
    (approval-helpers/is-same? output-path)))

(deftest ^{:skip-ci "Likely a different version of Graphviz."}
  example-model
  (let [output-path "components/contextmapper/test/output/example"]
    (contextmapper/generate
     {:model-file "components/contextmapper/test/resources/example.cml"
      :output-path output-path})
    (approval-helpers/is-same? output-path)))
