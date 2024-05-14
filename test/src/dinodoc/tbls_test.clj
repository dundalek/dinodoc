(ns dinodoc.tbls-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [dinodoc.fs-helpers :refer [with-temp-dir]]
   [dinodoc.tbls.impl :refer [resolve-link]]))

(deftest resolve-test
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "chinook/Album.md" "")
      (fspit "sakila/public.customer.md" "")

      (testing "basic"
        (is (= "Album.md" (resolve-link (str dir "/chinook") "Album")))
        (is (= "public.customer.md" (resolve-link (str dir "/sakila") "public.customer"))))

      (testing "multiple databases"
        (is (= "chinook/Album.md" (resolve-link dir "chinook:Album")))
        (is (= "sakila/public.customer.md" (resolve-link dir "sakila:customer"))))

      (testing "shorthand omitting postgres public schema"
        (is (= "public.customer.md" (resolve-link (str dir "/sakila") "customer")))
        (is (= "sakila/public.customer.md" (resolve-link dir "sakila:public.customer")))))))
