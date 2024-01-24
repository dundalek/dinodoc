(ns dinodoc.statecharts.statecharts-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer [deftest]]
   [dinodoc.approval-helpers :as approval]
   [dinodoc.impl.git-test :as git-test]
   [dinodoc.statecharts :as statecharts]))

(deftest render-machine-test
  (let [approval-file "test/output/machines.md"]
    (git-test/with-main-branch
      (with-open [writer (io/writer approval-file)]
        (binding [*out* writer]
          (statecharts/render-machine-var (requiring-resolve 'example.statecharts/machine))
          (statecharts/render-machine-var (requiring-resolve 'example.statecharts/regions)))))
    (approval/is-same? approval-file)))
