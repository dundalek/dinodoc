(ns dinodoc.structurizr-test
  (:require
   [clojure.test :refer [deftest]]
   [dinodoc.approval-helpers :as approval-helpers]
   [dinodoc.structurizr :as structurizr]))

;; For now assuming tests are run from repo root.
;; Starting with an approval tests to catch regressions, in the future when changes are made should add more granular tests.

(deftest generate-from-dsl
  (let [output-path "components/structurizr/test/output/from-dsl"]
    (structurizr/generate
     {:workspace-file "examples/structurizr/examples/dsl/big-bank-plc/workspace.dsl"
      :output-path output-path})
    (approval-helpers/is-same? output-path)))

(deftest generate-from-json
  (let [output-path "components/structurizr/test/output/from-json"]
    (structurizr/generate
     {:workspace-file "examples/structurizr/examples/json/getting-started/workspace.json"
      :output-path output-path})
    (approval-helpers/is-same? output-path)))
