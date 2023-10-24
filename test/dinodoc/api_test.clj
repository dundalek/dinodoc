(ns dinodoc.api-test
  (:require
   [clojure.java.shell :refer [sh]]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [dinodoc.core :as dinodoc]
   [dinodoc.fs-helpers :as fsh :refer [fsdata with-temp-dir]]))

(deftest generate-approval-test
  ;; Specifying different repos for different inputs in global mode is broken, need to fix later
  (dinodoc/generate
   {:paths [{:path "test-projects/promesa"
             :github/repo "https://github.com/funcool/promesa"}
            {:path "test-projects/codox/example"
             :github/repo "https://github.com/weavejester/codox"}
            {:path "test-projects/samples"
             :github/repo "https://github.com/dundalek/dinodoc"}]
    :outdir "test-output/docs"
    :api-docs :global
    :git/branch "master"})
  ;; We compare if output is the same as committed state, git status returns empty output if there are no changes.
  ;; If implementation changes and expected state needs to be updated, commit the changes after inspecting the diff.
  (is (= {:exit 0
          :err ""
          :out ""}
         (sh "git" "status" "--porcelain" "test-output"))))

(deftest generate-foo
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [outdir (str dir "/docs")]
        (fspit "src/example/main.clj" "(ns example.main)\n(defn foo [])")
        (dinodoc/generate {:paths [{:path dir
                                    :outdir "."}]
                           :outdir outdir
                           :github/repo "repo"
                           :git/branch "main"})
        (is (str/includes?
             (get-in (fsdata outdir) ["api" "example" "main" "index.md"])
             "\n### foo {#foo}\n"))))))

(deftest generate-blank
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [outdir (str dir "/docs")]
        (fspit "src/example/main.clj" "(ns example.main)\n")
        (dinodoc/generate {:paths [{:path dir
                                    :outdir "."}]
                           :outdir outdir
                           :github/repo "repo"
                           :git/branch "main"})
        (testing "No file is created if there are no vars"
          (is (= {} (fsdata outdir))))))))

(deftest generate-api-mode-global
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "a/src/example/a_main.clj" "(ns example.a-main)\n(defn foo [])")
      (fspit "b/src/example/b_main.clj" "(ns example.b-main)\n(defn bar [])")
      (let [outdir (str dir "/docs-separate")
            _ (dinodoc/generate {:paths [(str dir "/a")
                                         (str dir "/b")]
                                 :outdir outdir
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata outdir)]
        (is (str/includes? (get-in data ["a" "api" "example" "a-main" "index.md"])
                           "\n### foo {#foo}\n"))
        (is (str/includes? (get-in data ["b" "api" "example" "b-main" "index.md"])
                           "\n### bar {#bar}\n")))
      (let [outdir (str dir "/docs-global")
            _ (dinodoc/generate {:paths [(str dir "/a")
                                         (str dir "/b")]
                                 :outdir outdir
                                 :api-docs :global
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata outdir)]
        (is (str/includes? (get-in data ["api" "example" "a-main" "index.md"])
                           "\n### foo {#foo}\n"))
        (is (str/includes? (get-in data ["api" "example" "b-main" "index.md"])
                           "\n### bar {#bar}\n"))))))

(deftest generate-doc-tree
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "README.md" "# This is readme")
      (fspit "doc/a.md" "# Page A")
      (fspit "doc/b.md" "# Page B")
      (let [doc-tree [["Hello" {:file "README.md"}]
                      ;; "B" is renamed  and goes before "A" so it will have sidebar_position set accordingly
                      ["The B" {:file "doc/b.md"}]]
                      ;; "A" is not present, but will be added by auto-discovering
            expected {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/README.md, sidebar_label: Hello}\n---\n\n# This is readme"
                      "the-b.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: The B}\n---\n\n# Page B"
                      "a.md" "---\n{sidebar_position: 2, custom_edit_url: repo/tree/main/doc/a.md}\n---\n\n# Page A"}]

        (testing "Auto-discovering articles"
          (let [outdir (str dir "/docs-auto-discovered")]
            (dinodoc/generate {:paths [{:path dir
                                        :outdir "."}]
                               :outdir outdir
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/README.md}\n---\n\n# This is readme"
                    "a.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/a.md}\n---\n\n# Page A"
                    "b.md" "---\n{sidebar_position: 2, custom_edit_url: repo/tree/main/doc/b.md}\n---\n\n# Page B"}
                   (fsdata outdir)))))

        (testing "Curating articles with :doc-tree option"
          (let [outdir (str dir "/docs-doc-tree")]
            (dinodoc/generate {:paths [{:path dir
                                        :outdir "."
                                        :doc-tree doc-tree}]
                               :outdir outdir
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= expected (fsdata outdir)))))

        (testing "Curating articles using cljdoc.edn"
          (let [outdir (str dir "/docs-cljdoc")]
            (fspit "doc/cljdoc.edn" (pr-str {:cljdoc.doc/tree doc-tree}))
            (dinodoc/generate {:paths [{:path dir
                                        :outdir "."}]
                               :outdir outdir
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= expected (fsdata outdir)))))))))

(deftest generate-doc-tree-with-nesting
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "# Page A")
      (fspit "doc/b.md" "# Page B")
      (fspit "doc/c.md" "# Page C")
      (fspit "doc/d.md" "# Page D")
      (testing "nested"
        (let [outdir (str dir "/docs-nested")
              doc-tree [["Top-level B" {:file "doc/b.md"}
                         ["The A" {:file "doc/a.md"}]
                         ["The C" {:file "doc/c.md"}]]]]
          (dinodoc/generate {:paths [{:path dir
                                      :outdir "."
                                      :doc-tree doc-tree}]
                             :outdir outdir
                             :github/repo "repo"
                             :git/branch "main"})
          (is (= {"d.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/d.md}\n---\n\n# Page D"
                  "index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: Top-level B}\n---\n\n# Page B"
                  "top-level-b" {"the-a.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/a.md, sidebar_label: The A}\n---\n\n# Page A"
                                 "the-c.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/c.md, sidebar_label: The C}\n---\n\n# Page C"}}
                 (fsdata outdir)))))
      (testing "more-nesting"
        (let [outdir (str dir "/docs-more-nesting")
              doc-tree [["Top-level B" {:file "doc/b.md"}
                         ["The A" {:file "doc/a.md"}
                          ["The C" {:file "doc/c.md"}]]]]]
          (dinodoc/generate {:paths [{:path dir
                                      :outdir "."
                                      :doc-tree doc-tree}]
                             :outdir outdir
                             :github/repo "repo"
                             :git/branch "main"})
          (is (= {"d.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/d.md}\n---\n\n# Page D"
                  "index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: Top-level B}\n---\n\n# Page B"
                  "top-level-b" {"the-a" {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/a.md, sidebar_label: The A}\n---\n\n# Page A"
                                          "the-c.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/c.md, sidebar_label: The C}\n---\n\n# Page C"}}}
                 (fsdata outdir))))))))

(deftest generate-doc-tree-categories-without-content
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "# Page A")
      (fspit "doc/b.md" "# Page B")
      (fspit "doc/c.md" "# Page C")
      (fspit "doc/d.md" "# Page D")
      (let [doc-tree [["Top-level B" {:file "doc/b.md"}
                       ["A Category" {}
                        ["The A" {:file "doc/a.md"}]]
                       ["C Category" {}
                        ["The C" {:file "doc/c.md"}]]
                       ["The D" {:file "doc/d.md"}]]]]
        (testing "nested"
          (let [outdir (str dir "/docs")]
            (dinodoc/generate {:paths [{:path dir
                                        :outdir "."
                                        :doc-tree doc-tree}]
                               :outdir outdir
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: Top-level B}\n---\n\n# Page B"
                    "top-level-b" {"a-category" {"_category_.json" "{\"position\":0,\"label\":\"A Category\"}"
                                                 "the-a.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/a.md, sidebar_label: The A}\n---\n\n# Page A"}
                                   "c-category" {"_category_.json" "{\"position\":1,\"label\":\"C Category\"}"
                                                 "the-c.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/c.md, sidebar_label: The C}\n---\n\n# Page C"}
                                   "the-d.md" "---\n{sidebar_position: 2, custom_edit_url: repo/tree/main/doc/d.md, sidebar_label: The D}\n---\n\n# Page D"}}
                   (fsdata outdir)))))))))

(deftest generate-autodiscover-articles
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/A File.md" "File A")
      (fspit "doc/B Question?.md" "File B")
      (testing "nested"
        (let [outdir (str dir "/docs")]
          (dinodoc/generate {:paths [{:path dir
                                      :outdir "."}]
                             :outdir outdir
                             :github/repo "repo"
                             :git/branch "main"})
          (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/A File.md}\n---\n\nFile A"
                  ;; For autodiscovered articles we preserver the filename (not slugifying it).
                  ;; This is to make sure that articles that do not specify title like `# Title` will get more human readable label in the sidebar.
                  ;; But Docusaurus does not like some special characters like `?` so we need to do some stripping
                  "B Question.md" "---\n{sidebar_position: 1, custom_edit_url: 'repo/tree/main/doc/B Question?.md'}\n---\n\nFile B"}
                 (fsdata outdir))))))))

(deftest generate-link-fixer
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "[B](./b.md)")
      (fspit "doc/b.md" "[A](a.md)")

      (testing "same level"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree [["The A" {:file "doc/a.md"}]
                                                       ["The B" {:file "doc/b.md"}]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          (is (str/includes? (get-in data ["index.md"]) "[B](the-b.md)"))
          ; (is (str/includes? (get-in data ["the-b.md"]) "[B](index.md)"))
          ;; bug
          (is (str/includes? (get-in data ["the-b.md"]) "[A](the-a.md)"))))

      (testing "b nested down"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree [["The A" {:file "doc/a.md"}]
                                                       ["nested" {}
                                                        ["The B" {:file "doc/b.md"}]]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          (is (str/includes? (get-in data ["index.md"]) "[B](nested/the-b.md)"))
          ; ; (is (str/includes? (get-in data ["nested" "the-b.md"]) "[B](../index.md)"))
          ; ;; bug
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../the-a.md)"))))

      (testing "b nested up"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree
                                            [["nested" {}
                                              ["The A" {:file "doc/a.md"}]]
                                             ["The B" {:file "doc/b.md"}]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../the-b.md)"))
          (is (str/includes? (get-in data ["the-b.md"]) "nested/the-a.md"))))

      (testing "both nested"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree [["nested" {}
                                                        ["The A" {:file "doc/a.md"}]
                                                        ["The B" {:file "doc/b.md"}]]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          ;; this could be normalized to remove the `nested` segment
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../nested/the-b.md)"))
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../nested/the-a.md)")))))))

(deftest generate-link-fixer-with-nested
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "[B](nested/b.md)")
      (fspit "doc/nested/b.md" "[A](../a.md)")

      (testing "same level"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree [["The A" {:file "doc/a.md"}]
                                                       ["The B" {:file "doc/nested/b.md"}]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          (is (str/includes? (get-in data ["index.md"]) "[B](the-b.md)"))
          ; (is (str/includes? (get-in data ["the-b.md"]) "[B](index.md)"))
          ;; bug
          (is (str/includes? (get-in data ["the-b.md"]) "[A](the-a.md)"))))

      (testing "b nested down"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree [["The A" {:file "doc/a.md"}]
                                                       ["nested" {}
                                                        ["The B" {:file "doc/nested/b.md"}]]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          (is (str/includes? (get-in data ["index.md"]) "[B](nested/the-b.md)"))
          ; ; (is (str/includes? (get-in data ["nested" "the-b.md"]) "[B](../index.md)"))
          ; ;; bug
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../the-a.md)"))))

      (testing "b nested up"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree
                                            [["nested" {}
                                              ["The A" {:file "doc/a.md"}]]
                                             ["The B" {:file "doc/nested/b.md"}]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../the-b.md)"))
          (is (str/includes? (get-in data ["the-b.md"]) "nested/the-a.md"))))

      (testing "both nested"
        (let [outdir (str dir "/docs-same-level")
              _ (dinodoc/generate {:paths [{:path dir
                                            :outdir "."
                                            :doc-tree [["nested" {}
                                                        ["The A" {:file "doc/a.md"}]
                                                        ["The B" {:file "doc/nested/b.md"}]]]}]
                                   :outdir outdir
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata outdir)]
          ;; this could be normalized to remove the `nested` segment
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../nested/the-b.md)"))
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../nested/the-a.md)")))))))
