(ns dinodoc.statecharts.statecharts-test
  (:require
   [clojure.test :refer [deftest is]]
   [dinodoc.impl.git-test :as git-test]
   [dinodoc.statecharts :as statecharts]))

(deftest render-machine-test
  (let [output (git-test/with-main-branch
                 (with-out-str
                   (statecharts/render-machine-var (requiring-resolve 'example.statecharts/machine))))
        approval-file "test/output/machines.md"]
    ; (spit approval-file output)
    (is (= (slurp approval-file)
           output))))
