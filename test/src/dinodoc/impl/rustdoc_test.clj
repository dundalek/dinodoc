(ns dinodoc.impl.rustdoc-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [dinodoc.fs-helpers :refer [with-temp-dir]]
   [dinodoc.impl.rustdoc :as rustdoc]))

(deftest resolve-test
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "sample_rs/greeting/index.html" "")
      (fspit "sample_rs/greeting/fn.greet.html" "")

      (testing "resolves modules"
        (is (= "sample_rs/greeting/index.html" (rustdoc/resolve-link dir "sample_rs::greeting"))))

      (testing "resolves functions"
        (is (= "sample_rs/greeting/fn.greet.html" (rustdoc/resolve-link dir "sample_rs::greeting::greet"))))

      (testing "no link for missing definitions"
        (is (= nil (rustdoc/resolve-link dir "sample_rs::greeting::non_existing_fn")))
        (is (= nil (rustdoc/resolve-link dir "sample_rs::non_existing_module::greet")))))))
