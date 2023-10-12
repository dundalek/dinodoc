(ns dinodoc.api-test
  (:require
   [clojure.java.shell :refer [sh]]
   [clojure.test :refer [deftest is]]
   [dinodoc.core :as dinodoc]))

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
