(ns dinodoc.fs-helpers-test
  (:require
   [babashka.fs :as fs]
   [clojure.test :refer [deftest is testing]]
   [dinodoc.fs-helpers :as fsh]))

(deftest with-temp-dir-test
  (fsh/with-temp-dir
    (fn [{:keys [fspit fslurp]}]
      (testing "basic file helper"
        (fspit "file.txt" "foo")
        (is (= "foo" (fslurp "file.txt"))))

      (testing "automatically creates nested directories"
        (fspit "nested/file.txt" "bar")
        (is (= "bar" (fslurp "nested/file.txt")))))))

(deftest fsdata-test
  (fsh/with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "file.txt" "foo")
      (fspit "nested/file.txt" "bar")
      (fs/create-dir (str dir "/emptydir"))

      (is (= {"file.txt" "foo"
              "nested" {"file.txt" "bar"}
              "emptydir" {}}
             (fsh/fsdata dir)))
      (is (= {} (fsh/fsdata (str dir "/emptydir")))))))
