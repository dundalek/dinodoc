(ns dinodoc.structurizr-test
  (:require
   [clojure.test :refer [deftest]]
   [dinodoc.api :as dinodoc]
   [dinodoc.approval-helpers :as approval-helpers]
   [dinodoc.structurizr :as structurizr]))

;; For now assuming tests are run from repo root.
;; Starting with an approval tests to catch regressions, in the future when changes are made should add more granular tests.

;; I noticed complex examples from DSL tends to be flaky because elements can end up in non-deterministic order.
;; Therefore the DSL test is for a small example and JSON which should be determistic tests larger example.

(deftest generate-from-dsl
  (let [output-path "components/structurizr/test/output/dsl-getting-started"]
    (dinodoc/generate
     {:inputs [{:generator (structurizr/make-generator
                            {:workspace-file "examples/structurizr/examples/dsl/getting-started/workspace.dsl"})}]
      :output-path output-path})
    (approval-helpers/is-same? output-path)))

(deftest generate-from-json
  (let [output-path "components/structurizr/test/output/json-big-bank-plc"]
    (dinodoc/generate
     {:inputs [{:generator (structurizr/make-generator
                            {:workspace-file "examples/structurizr/examples/json/big-bank-plc/workspace.json"})
                :output-path "systems"}]
      :output-path output-path})
    (approval-helpers/is-same? output-path)))
