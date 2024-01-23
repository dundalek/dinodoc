(ns dinodoc.impl.git-test
  (:require
   [clojure.test :refer [deftest is]]
   [dinodoc.impl.git :as git]))

;; mocking current branch so that test does not fail when being switched to other branch during development
(defmacro with-main-branch [& body]
  `(with-redefs [git/current-branch (fn [_#] "main")]
     ~@body))

(deftest detect-repo-info
  (is (= {:branch "main"
          :url "https://github.com/dundalek/dinodoc"}
         (with-main-branch
           (git/detect-repo-info ".")))))

(deftest remote-url-to-web
  (is (= "https://github.com/borkdude/quickdoc"
         (git/remote-url-to-web "https://github.com/borkdude/quickdoc")))
  (is (= "https://github.com/borkdude/quickdoc"
         (git/remote-url-to-web "https://github.com/borkdude/quickdoc.git")))
  (is (= "https://github.com/borkdude/quickdoc"
         (git/remote-url-to-web "git@github.com:borkdude/quickdoc.git"))))
