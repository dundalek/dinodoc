(ns dinodoc.approval-helpers
  (:require
   [clojure.java.shell :refer [sh]]
   [clojure.test :refer [is]]))

;; We compare if output is the same as committed state, git status returns empty output if there are no changes.
;; If implementation changes and expected state needs to be updated, commit the changes after inspecting the diff.
(defmacro is-same? [path]
  `(is (= {:exit 0 :err "" :out ""}
          (sh "git" "status" "--porcelain" ~path))))
