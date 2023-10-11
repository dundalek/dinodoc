(ns dinodoc.impl-test
  (:require
   [clojure.test :refer [deftest is]]
   [dinodoc.impl :as impl]))

(deftest path-to-root-test
  (is (= "." (impl/path-to-root "/controllers.md")))
  (is (= ".." (impl/path-to-root "/frontend/controllers.md")))
  (is (= "../.." (impl/path-to-root "/frontend/foo/controllers.md")))

  (is (= "." (impl/path-to-root "controllers.md")))
  (is (= ".." (impl/path-to-root "frontend/controllers.md")))
  (is (= "../.." (impl/path-to-root "frontend/foo/controllers.md"))))

(deftest replace-links-test
  (is (= "[title](b.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "a.md"
                                                         "b.md" "b.md"}})))
  (is (= "[title](b.md)"
         (impl/replace-links "[title](./b.md)" {:source "a.md"
                                                :link-map {"a.md" "a.md"
                                                           "b.md" "b.md"}})))
  (is (= "[title](b.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "a.md"}})))
  (is (= "[title](../b.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "foo/aa.md"
                                                         "b.md" "b.md"}})))
  (is (= "[title](bar/bb.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "a.md"
                                                         "b.md" "bar/bb.md"}})))
  (is (= "[title](../bar/bb.md)"
         (impl/replace-links "[title](b.md)" {:source "a.md"
                                              :link-map {"a.md" "foo/aa.md"
                                                         "b.md" "bar/bb.md"}})))

  (is (= "[title](../bar/bb.md)"
         (impl/replace-links "[title](./b.md)" {:source "doc/a.md"
                                                :link-map {"doc/a.md" "foo/aa.md"
                                                           "doc/b.md" "bar/bb.md"}})))
  (is (= "[title](../bar/bb.md)"
         (impl/replace-links "[title](../b.md)" {:source "aa/a.md"
                                                 :link-map {"aa/a.md" "aa/a.md"
                                                            "b.md" "bar/bb.md"}})))
  (is (= "[title](bar/bb.md#hash)"
         (impl/replace-links "[title](b.md#hash)" {:source "a.md"
                                                   :link-map {"a.md" "a.md"
                                                              "b.md" "bar/bb.md"}})))
  (is (= "[title][ref]\n\n[ref]: bb.md\n"
         (impl/replace-links "[title][ref]\n\n[ref]: b.md\n"
                             {:source "a.md"
                              :link-map {"a.md" "a.md"
                                         "b.md" "bb.md"}})))
  (is (= "[title][ref]\n\n[ref]: bb.md"
         (impl/replace-links "[title][ref]\n\n[ref]: b.md"
                             {:source "a.md"
                              :link-map {"a.md" "a.md"
                                         "b.md" "bb.md"}}))))

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
                                    ["bb" {:file "b.md"}]]])))
  (is (= {"a.md" nil}
         (impl/doc-tree->file-map [[nil {:file "a.md"}]]))))
