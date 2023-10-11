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

(deftest doc-tree->file-map-test
  (is (= {"a.md" "aa"
          "b.md" "bb"}
         (impl/doc-tree->file-map [["aa" {:file "a.md"}]
                                   ["bb" {:file "b.md"}]])))

  (is (= {"a.md" "aa"
          "b.md" "aa/bb"}
         (impl/doc-tree->file-map [["aa" {:file "a.md"}
                                    ["bb" {:file "b.md"}]]])))

  (is (= {"a.md" "foo/aa"
          "b.md" "bar/bb"}
         (impl/doc-tree->file-map [["foo" {}
                                    ["aa" {:file "a.md"}]]
                                   ["bar" {}
                                    ["bb" {:file "b.md"}]]]))))
