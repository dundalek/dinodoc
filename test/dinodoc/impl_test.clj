(ns dinodoc.impl-test
  (:require
   [clojure.test :refer [deftest is]]
   [dinodoc.impl :as impl]))

(deftest replace-links-test
  (is (= "[title](./b.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "a.md"
                                                         "b.md" "b.md"}})))
  (is (= "[title](b.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "a.md"}})))
  (is (= "[title](../b.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "foo/aa.md"
                                                         "b.md" "b.md"}})))
  (is (= "[title](./bar/bb.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "a.md"
                                                         "b.md" "bar/bb.md"}})))
  (is (= "[title](../bar/bb.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "foo/aa.md"
                                                         "b.md" "bar/bb.md"}}))))
