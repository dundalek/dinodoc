(ns dinodoc.api-test
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [dinodoc.api :as dinodoc]
   [dinodoc.approval-helpers :as approval]
   [dinodoc.contextmapper :as contextmapper]
   [dinodoc.fs-helpers :as fsh :refer [fsdata with-temp-dir]]
   [dinodoc.impl.fs :as fsi]
   [dinodoc.impl.javadoc :as javadoc]
   [dinodoc.impl.rustdoc :as rustdoc]
   [dinodoc.impl.tbls :as tbls]
   [readme]))

(defn- naively-strip-front-matter [s]
  (str/replace s #"(?s).*---\n\n" ""))

;; Non-deterministic selection of clj/cljs docstrings, skip in CI for now
(deftest ^:skip-ci generate-approval-test
  ;; Specifying different repos for different inputs in global mode is broken, need to fix later
  (dinodoc/generate
   {:inputs [{:path "test/projects/promesa"
              :github/repo "https://github.com/funcool/promesa"}
             {:path "test/projects/codox/example"
              :github/repo "https://github.com/weavejester/codox"}
             {:path "test/projects/samples"
              :github/repo "https://github.com/dundalek/dinodoc"}]
    :output-path "test/output/docs"
    :api-mode :global
    :git/branch "master"})
  (approval/is-same? "test/output/docs"))

(deftest generate-foo
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [output-path (str dir "/docs")]
        (fspit "src/example/main.clj" "(ns example.main)\n(defn foo [])")
        (dinodoc/generate {:inputs [{:path dir
                                     :output-path "."}]
                           :output-path output-path
                           :github/repo "repo"
                           :git/branch "main"})
        (is (str/includes?
             (get-in (fsdata output-path) ["api" "example" "main" "index.md"])
             "\n### foo {#foo}\n"))
        (is (str/includes?
             (get-in (fsdata output-path) ["api" "example" "main" "index.md"])
             "[source](repo/blob/main/src/example/main.clj#L2-L2)"))))))

(deftest generate-without-git-repo
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [output-path (str dir "/docs")]
        (fspit "src/example/main.clj" "(ns example.main)\n(defn foo [])")
        (dinodoc/generate {:inputs [{:path dir
                                     :output-path "."}]
                           :output-path output-path})
        (is (str/includes?
             (get-in (fsdata output-path) ["api" "example" "main" "index.md"])
             "\n### foo {#foo}\n"))
        (is (not (str/includes?
                  (get-in (fsdata output-path) ["api" "example" "main" "index.md"])
                  "[source](")))))))

(deftest generate-blank
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [output-path (str dir "/docs")]
        (fspit "src/example/main.clj" "(ns example.main)\n")
        (dinodoc/generate {:inputs [{:path dir
                                     :output-path "."}]
                           :output-path output-path
                           :github/repo "repo"
                           :git/branch "main"})
        (testing "No file is created if there are no vars"
          (is (= {} (fsdata output-path))))))))

(deftest generate-customize-doc-path
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [output-path (str dir "/docs")]
        (fspit "articles/a.md" "Lorem ipsum")
        (dinodoc/generate {:inputs [{:path dir
                                     :output-path "."
                                     :doc-path "articles"}]
                           :output-path output-path
                           :github/repo "repo"
                           :git/branch "main"})
        (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/articles/a.md}\n---\n\nLorem ipsum"}
               (fsdata output-path)))))))

(deftest generate-customize-source-paths
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [output-path (str dir "/docs")]
        (fspit "src-clj/example/server.clj" "(ns example.server)\n(defn foo [])")
        (fspit "src-cljs/example/client.cljs" "(ns example.client)\n(defn bar [])")
        (dinodoc/generate {:inputs [{:path dir
                                     :output-path "."
                                     :source-paths ["src-clj" "src-cljs"]}]
                           :output-path output-path
                           :github/repo "repo"
                           :git/branch "main"})
        (is (str/includes?
             (get-in (fsdata output-path) ["api" "example" "server" "index.md"])
             "\n### foo {#foo}\n"))
        (is (str/includes?
             (get-in (fsdata output-path) ["api" "example" "client" "index.md"])
             "\n### bar {#bar}\n"))))))

(deftest generate-api-mode-global
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "a/src/example/a_main.clj" "(ns example.a-main)\n(defn foo [])")
      (fspit "b/src/example/b_main.clj" "(ns example.b-main)\n(defn bar [])")
      (let [output-path (str dir "/docs-separate")
            _ (dinodoc/generate {:inputs [(str dir "/a")
                                          (str dir "/b")]
                                 :output-path output-path
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata output-path)]
        (is (str/includes? (get-in data ["a" "api" "example" "a-main" "index.md"])
                           "\n### foo {#foo}\n"))
        (is (str/includes? (get-in data ["b" "api" "example" "b-main" "index.md"])
                           "\n### bar {#bar}\n")))
      (let [output-path (str dir "/docs-global")
            _ (dinodoc/generate {:inputs [(str dir "/a")
                                          (str dir "/b")]
                                 :output-path output-path
                                 :api-mode :global
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata output-path)]
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
          (let [output-path (str dir "/docs-auto-discovered")]
            (dinodoc/generate {:inputs [{:path dir
                                         :output-path "."}]
                               :output-path output-path
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/README.md}\n---\n\n# This is readme"
                    "a.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/a.md}\n---\n\n# Page A"
                    "b.md" "---\n{sidebar_position: 2, custom_edit_url: repo/tree/main/doc/b.md}\n---\n\n# Page B"}
                   (fsdata output-path)))))

        (testing "Curating articles with :doc-tree option"
          (let [output-path (str dir "/docs-doc-tree")]
            (dinodoc/generate {:inputs [{:path dir
                                         :output-path "."
                                         :doc-tree doc-tree}]
                               :output-path output-path
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= expected (fsdata output-path)))))

        (testing "Curating articles using cljdoc.edn"
          (let [output-path (str dir "/docs-cljdoc")]
            (fspit "doc/cljdoc.edn" (pr-str {:cljdoc.doc/tree doc-tree}))
            (dinodoc/generate {:inputs [{:path dir
                                         :output-path "."}]
                               :output-path output-path
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= expected (fsdata output-path)))))))))

(deftest generate-doc-tree-with-nesting
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "# Page A")
      (fspit "doc/b.md" "# Page B")
      (fspit "doc/c.md" "# Page C")
      (fspit "doc/d.md" "# Page D")
      (testing "nested"
        (let [output-path (str dir "/docs-nested")
              doc-tree [["Top-level B" {:file "doc/b.md"}
                         ["The A" {:file "doc/a.md"}]
                         ["The C" {:file "doc/c.md"}]]]]
          (dinodoc/generate {:inputs [{:path dir
                                       :output-path "."
                                       :doc-tree doc-tree}]
                             :output-path output-path
                             :github/repo "repo"
                             :git/branch "main"})
          (is (= {"d.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/d.md}\n---\n\n# Page D"
                  "index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: Top-level B}\n---\n\n# Page B"
                  "top-level-b" {"the-a.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/a.md, sidebar_label: The A}\n---\n\n# Page A"
                                 "the-c.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/c.md, sidebar_label: The C}\n---\n\n# Page C"}}
                 (fsdata output-path)))))
      (testing "more-nesting"
        (let [output-path (str dir "/docs-more-nesting")
              doc-tree [["Top-level B" {:file "doc/b.md"}
                         ["The A" {:file "doc/a.md"}
                          ["The C" {:file "doc/c.md"}]]]]]
          (dinodoc/generate {:inputs [{:path dir
                                       :output-path "."
                                       :doc-tree doc-tree}]
                             :output-path output-path
                             :github/repo "repo"
                             :git/branch "main"})
          (is (= {"d.md" "---\n{sidebar_position: 1, custom_edit_url: repo/tree/main/doc/d.md}\n---\n\n# Page D"
                  "index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: Top-level B}\n---\n\n# Page B"
                  "top-level-b" {"the-a" {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/a.md, sidebar_label: The A}\n---\n\n# Page A"
                                          "the-c.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/c.md, sidebar_label: The C}\n---\n\n# Page C"}}}
                 (fsdata output-path))))))))

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
          (let [output-path (str dir "/docs")]
            (dinodoc/generate {:inputs [{:path dir
                                         :output-path "."
                                         :doc-tree doc-tree}]
                               :output-path output-path
                               :github/repo "repo"
                               :git/branch "main"})
            (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/b.md, sidebar_label: Top-level B}\n---\n\n# Page B"
                    "top-level-b" {"a-category" {"_category_.json" "{\"position\":0,\"label\":\"A Category\"}"
                                                 "the-a.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/a.md, sidebar_label: The A}\n---\n\n# Page A"}
                                   "c-category" {"_category_.json" "{\"position\":1,\"label\":\"C Category\"}"
                                                 "the-c.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/c.md, sidebar_label: The C}\n---\n\n# Page C"}
                                   "the-d.md" "---\n{sidebar_position: 2, custom_edit_url: repo/tree/main/doc/d.md, sidebar_label: The D}\n---\n\n# Page D"}}
                   (fsdata output-path)))))))))

(deftest generate-autodiscover-articles
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/A File.md" "File A")
      (fspit "doc/B Question?.md" "File B")
      (testing "nested"
        (let [output-path (str dir "/docs")]
          (dinodoc/generate {:inputs [{:path dir
                                       :output-path "."}]
                             :output-path output-path
                             :github/repo "repo"
                             :git/branch "main"})
          (is (= {"index.md" "---\n{sidebar_position: 0, custom_edit_url: repo/tree/main/doc/A File.md}\n---\n\nFile A"
                  ;; For autodiscovered articles we preserver the filename (not slugifying it).
                  ;; This is to make sure that articles that do not specify title like `# Title` will get more human readable label in the sidebar.
                  ;; But Docusaurus does not like some special characters like `?` so we need to do some stripping
                  "B Question.md" "---\n{sidebar_position: 1, custom_edit_url: 'repo/tree/main/doc/B Question?.md'}\n---\n\nFile B"}
                 (fsdata output-path))))))))

(deftest generate-link-fixer
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "[B](./b.md)")
      (fspit "doc/b.md" "[A](a.md)")

      (testing "same level"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree [["The A" {:file "doc/a.md"}]
                                                        ["The B" {:file "doc/b.md"}]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          (is (str/includes? (get-in data ["index.md"]) "[B](the-b.md)"))
          (is (str/includes? (get-in data ["the-b.md"]) "[A](index.md)"))))

      (testing "b nested down"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree [["The A" {:file "doc/a.md"}]
                                                        ["nested" {}
                                                         ["The B" {:file "doc/b.md"}]]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          (is (str/includes? (get-in data ["index.md"]) "[B](nested/the-b.md)"))
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../index.md)"))))

      (testing "b nested up"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree
                                             [["nested" {}
                                               ["The A" {:file "doc/a.md"}]]
                                              ["The B" {:file "doc/b.md"}]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../the-b.md)"))
          (is (str/includes? (get-in data ["the-b.md"]) "nested/the-a.md"))))

      (testing "both nested"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree [["nested" {}
                                                         ["The A" {:file "doc/a.md"}]
                                                         ["The B" {:file "doc/b.md"}]]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          ;; this could be normalized to remove the `nested` segment
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../nested/the-b.md)"))
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../nested/the-a.md)")))))))

(deftest generate-link-fixer-with-nested
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "doc/a.md" "[B](nested/b.md)")
      (fspit "doc/nested/b.md" "[A](../a.md)")

      (testing "same level"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree [["The A" {:file "doc/a.md"}]
                                                        ["The B" {:file "doc/nested/b.md"}]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          (is (str/includes? (get-in data ["index.md"]) "[B](the-b.md)"))
          (is (str/includes? (get-in data ["the-b.md"]) "[A](index.md)"))))

      (testing "b nested down"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree [["The A" {:file "doc/a.md"}]
                                                        ["nested" {}
                                                         ["The B" {:file "doc/nested/b.md"}]]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          (is (str/includes? (get-in data ["index.md"]) "[B](nested/the-b.md)"))
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../index.md)"))))

      (testing "b nested up"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree
                                             [["nested" {}
                                               ["The A" {:file "doc/a.md"}]]
                                              ["The B" {:file "doc/nested/b.md"}]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../the-b.md)"))
          (is (str/includes? (get-in data ["the-b.md"]) "nested/the-a.md"))))

      (testing "both nested"
        (let [output-path (str dir "/docs-same-level")
              _ (dinodoc/generate {:inputs [{:path dir
                                             :output-path "."
                                             :doc-tree [["nested" {}
                                                         ["The A" {:file "doc/a.md"}]
                                                         ["The B" {:file "doc/nested/b.md"}]]]}]
                                   :output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"})
              data (fsdata output-path)]
          ;; this could be normalized to remove the `nested` segment
          (is (str/includes? (get-in data ["nested" "the-a.md"]) "[B](../nested/the-b.md)"))
          (is (str/includes? (get-in data ["nested" "the-b.md"]) "[A](../nested/the-a.md)")))))))

(deftest generate-namespace-links
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "xyz/doc/a.md" "lorem `example.main/foo` ipsum `example.main/bar` dolor")
      (fspit "xyz/doc/b.md" "lorem [[example.main/foo]] ipsum [[example.main/bar]] dolor")
      (fspit "xyz/src/example/main.clj" "(ns example.main)\n(defn foo [])")
      (fspit "abc/doc/a.md" "lorem `example.main/foo` ipsum `example.main/bar` dolor")
      (fspit "abc/doc/b.md" "lorem [[example.main/foo]] ipsum [[example.main/bar]] dolor")
      (let [output-path (str dir "/docs")
            _ (dinodoc/generate {:inputs [{:path (str dir "/xyz")}
                                          {:path (str dir "/abc")}]
                                 :output-path output-path
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata output-path)]
        (testing "backtick links"
          (is (str/includes?
               (get-in data ["xyz" "index.md"])
               "lorem [`example.main/foo`](./api/example/main/#foo) ipsum"))
          (is (str/includes?
               (get-in data ["xyz" "index.md"])
               "ipsum `example.main/bar` dolor")))

        (testing "wiki links"
          (is (str/includes?
               (get-in data ["xyz" "b.md"])
               "lorem [`example.main/foo`](./api/example/main/#foo) ipsum"))
          (is (str/includes?
               (get-in data ["xyz" "b.md"])
               "ipsum [[example.main/bar]] dolor")))

        (testing "links from separate are input not replaced"
             ;; Should we implement linking across inputs?
             ;; Would likely need to figure some disambiguation in case of conflict.
          (is (str/includes?
               (get-in data ["abc" "index.md"])
               "ipsum `example.main/bar` dolor"))
          (is (str/includes?
               (get-in data ["abc" "b.md"])
               "ipsum [[example.main/bar]] dolor")))))))

(deftest generate-namespace-links-global-mode
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "xyz/src/example/main.clj" "(ns example.main)\n(defn foo [])")
      (fspit "abc/doc/a.md" "lorem `example.main/foo` ipsum `example.main/bar` dolor")
      (fspit "abc/doc/b.md" "lorem [[example.main/foo]] ipsum [[example.main/bar]] dolor")
      (let [output-path (str dir "/docs")
            _ (dinodoc/generate {:inputs [{:path (str dir "/xyz")}
                                          {:path (str dir "/abc")
                                           :doc-tree [["a" {:file "doc/a.md"}]
                                                      ["nested" {}
                                                       ["b" {:file "doc/b.md"}]]]}]
                                 :api-mode :global
                                 :output-path output-path
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata output-path)]

        (testing "backtick links"
          (is (str/includes?
               (get-in data ["abc" "index.md"])
               "lorem [`example.main/foo`](../api/example/main/#foo) ipsum"))
          (is (str/includes?
               (get-in data ["abc" "index.md"])
               "ipsum `example.main/bar` dolor")))

        (testing "wiki links"
          (is (str/includes?
               (get-in data ["abc" "nested" "b.md"])
               "lorem [`example.main/foo`](../../api/example/main/#foo) ipsum"))
          (is (str/includes?
               (get-in data ["abc" "nested" "b.md"])
               "ipsum [[example.main/bar]] dolor")))))))

(deftest links-to-api-modes
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "src/example/main.clj" "(ns example.main)\n(defn foo [])")
      (fspit "README.md" "# This is readme")
      (fspit "doc/a.md" "lorem [[example.main/foo]] ipsum")
      (let [opts {:inputs [{:path dir
                            :output-path "."}]
                  :github/repo "repo"
                  :git/branch "main"}]
        (doseq [[label opts]
                [["default-mode" opts]
                 ["global-mode" (assoc opts :api-mode :global)]]]
          (let [output-path (str dir "/" label)
                _ (dinodoc/generate (assoc opts :output-path output-path))
                data (fsdata output-path)]
            (testing label
              (is (str/includes?
                   (get-in data ["a.md"])
                   "lorem [`example.main/foo`](./api/example/main/#foo) ipsum"))
              (is (str/includes?
                   (get-in data ["api" "example" "main" "index.md"])
                   "### foo {#foo}")))))))))

(deftest multiple-links-issue
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "src/example/main.clj" "(ns example.main)\n(defn foo [])")
      (fspit "README.md" "`example.main/foo` `example.main/foo`")
      (let [output-path (str dir "/docs")
            _ (dinodoc/generate {:inputs [{:path dir
                                           :output-path "."}]
                                 :output-path output-path
                                 :github/repo "repo"
                                 :git/branch "main"})
            data (fsdata output-path)]
        (is (= "[`example.main/foo`](./api/example/main/#foo) [`example.main/foo`](./api/example/main/#foo)" (naively-strip-front-matter (get-in data ["index.md"]))))))))

(deftest ^{:skip-ci "tbls not available in ubuntu"} generator-linking
  (binding [fsi/*tmp-dir* "/tmp"]
    (with-temp-dir
      (fn [{:keys [dir fspit]}]
        (fspit "README.md"
               (str "- java: [[demo.Greeter.greet]]\n"
                    "- rust: [[example::greeting::greet]]\n"
                    "- dbschema: [[Album]]\n"
                    "- namespaced dbschema: [[chinook:Album]]\n"
                    "- contextmapper domain: [[DomainA]]\n"
                    "- contextmapper context: [[ContextB]]\n"))
        (let [output-path (str dir "/docs")
              _ (dinodoc/generate {:output-path output-path
                                   :github/repo "repo"
                                   :git/branch "main"
                                   :inputs [{:path dir
                                             :output-path "."}
                                            {:output-path "javadoc"
                                             :generator (javadoc/make-generator
                                                         {:sourcepath "examples/java/src/main/java"
                                                          :subpackages "demo"})}
                                            {:output-path "rustdoc"
                                             :generator (rustdoc/make-generator
                                                         {:manifest-path "examples/rust/Cargo.toml"})}
                                            {:output-path "dbschema"
                                             :generator (tbls/make-generator
                                                         {:dsn (str "sqlite:" (fs/absolutize "examples/dbschema/chinook/ChinookDatabase/DataSources/Chinook_Sqlite.sqlite"))
                                                          :title "_REPLACED_TITLE_SENTINEL_"
                                                          :UNSTABLE_prefix "chinook"})}
                                            {:output-path "contextmapper"
                                             :generator (contextmapper/make-generator
                                                         {:model-file "components/contextmapper/test/resources/example.cml"})}]})
              data (fsdata output-path)]
          (is (= ["- java: [`demo.Greeter.greet`](pathname://./javadoc/demo/Greeter.html#greet(java.lang.String))"
                  "- rust: [`example::greeting::greet`](pathname://./rustdoc/example/greeting/fn.greet.html)"
                  "- dbschema: [`Album`](./dbschema/Album.md)"
                  "- namespaced dbschema: [`chinook:Album`](./dbschema/Album.md)"
                  "- contextmapper domain: [`DomainA`](./contextmapper/domains/DomainA/)"
                  "- contextmapper context: [`ContextB`](./contextmapper/contexts/ContextB/)"]
                 (-> (get-in data ["index.md"])
                     (naively-strip-front-matter)
                     (str/split-lines))))
          (is (str/includes?
               (get-in data ["dbschema" "README.md"])
               "# _REPLACED_TITLE_SENTINEL_")))))))
