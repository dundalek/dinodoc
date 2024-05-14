(ns dinodoc.statecharts.statecharts-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer [deftest]]
   [dinodoc.approval-helpers :as approval]
   [dinodoc.impl.git :as git]
   [dinodoc.impl.git-test :as git-test]
   [dinodoc.statecharts :as statecharts]))

(deftest render-machine-test
  (let [approval-file "test/output/machines.md"]
    (with-open [writer (io/writer approval-file)]
      (binding [*out* writer]
        (git-test/with-main-branch
          (statecharts/render-machine-var (requiring-resolve 'example.statecharts/machine))
          (statecharts/render-machine-var (requiring-resolve 'example.statecharts/regions)))
        (with-redefs [git/detect-repo-info (fn [_])]
          (statecharts/render-machine-var (requiring-resolve 'example.statecharts/dog-walk)))))
    (approval/is-same? approval-file)))
