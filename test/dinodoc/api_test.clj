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
