(ns dinodoc.antora-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [dinodoc.antora :as antora]
   [dinodoc.fs-helpers :as fsh :refer [fsdata with-temp-dir]]))

(deftest generate-navigation-basic
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "b.adoc" "")
      (fspit "a.adoc" "")
      (is (= "* xref:a.adoc[]\n* xref:b.adoc[]"
             (antora/generate-navigation dir))))))

(deftest generate-navigation-ignored-files
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "a.adoc" "")
      (fspit "b.txt" "")
      (testing "ignores non-adoc files"
        (is (= "* xref:a.adoc[]"
               (antora/generate-navigation dir)))))))

(deftest generate-navigation-nested
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "a.adoc" "")
      (fspit "b/b1.adoc" "")
      (fspit "b/b2.adoc" "")
      (fspit "c.adoc" "")
      (is (= "* xref:a.adoc[]\n* b\n** xref:b/b1.adoc[]\n** xref:b/b2.adoc[]\n* xref:c.adoc[]"
             (antora/generate-navigation dir))))))

(deftest md->adoc-test
  (is (= "= h1\n\n" (antora/md->adoc "# h1\n\n")))
  (is (= "link:example.com[Example]\n" (antora/md->adoc "[Example](example.com)\n")))

  ;; it would be better if the output did not includ heading id `[#h2]` when not explicitly defined
  (is (= "= h1\n\n[#h2]\n== h2\n" (antora/md->adoc "# h1\n\n## h2\n")))
  (is (= "[#h2]\n== h2\n" (antora/md->adoc "## h2\n"))))

(deftest md->adoc-test-heading-ids
  (is (= "[#greet]\n== greet\n" (antora/md->adoc "## greet {#greet}")))
  (is (= "[#-main]\n== -main\n" (antora/md->adoc "## -main {#-main}"))))

(deftest md->adoc-md-links-fixup
  (is (= "link:foo.adoc[]\n" (antora/md->adoc "[](foo.md)")))
  (is (= "https://example/foo.md[]\n" (antora/md->adoc "[](https://example/foo.md)"))))

(deftest transform-directory
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [foo-content "## foo {#foo}"]
        (fspit "foo.md" foo-content)
        (antora/transform-directory dir)
        (is (= {"foo.md" foo-content
                "foo.adoc" "[#foo]\n== foo\n"}
               (fsdata dir)))))))
