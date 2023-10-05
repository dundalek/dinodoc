(ns quickdoc.impl-test
  (:require
   [clojure.string :as str]
   [clojure.test :as t :refer [deftest is testing]]
   [quickdoc.impl :as impl :refer [var-source var-summary]]))

(deftest var-summary-test
  (is (= nil (var-summary {})))
  (is (= nil (var-summary {:doc ""})))
  (is (= nil (var-summary {:doc " "})))
  (is (= nil (var-summary {:doc " \n\t \r"})))
  (is (= "3 5 7 9." (var-summary {:doc "  3\n5\t7 \n9 "})))
  (is (= "3 5 7 9." (var-summary {:doc "\n 3\n5\t7 \n9 "})))
  (is (= "." (var-summary {:doc ". 345"})))
  (is (=  "12 45." (var-summary {:doc "12 45"})))

  (is (= "1 3." (var-summary {:doc "1 3. 6. 9."})))
  (is (= "1 3.5 7." (var-summary {:doc "1 3.5 7"})))
  (is (= "1 3.5 7." (var-summary {:doc "1 3.5 7."})))
  (is (= "1 3.5." (var-summary {:doc "1 3.5. 8."})))
  (is (= "1 <code>4</code>." (var-summary {:doc "1 `4`. 8"}))))

(deftest var-source-test
  (is (= "https://github.com/babashka/process/blob/master/src/babashka/process.cljc#L158-L163"
         (var-source
          {:filename "src/babashka/process.cljc"
           :row      158
           :end-row  163}
          {:github/repo "https://github.com/babashka/process"
           :git/branch  "master"})))
  (is (= "https://github.com/foo/blob/main/prefix/src/bar/baz.clj#L1-L10"
         (var-source
          {:filename "src/bar/baz.clj"
           :row      1
           :end-row  10}
          {:github/repo         "https://github.com/foo"
           :git/branch          "main"
           :filename-add-prefix "prefix/"})))
  (is (= "https://github.com/foo/blob/main/src/bar/baz.clj#L1-L10"
         (var-source
          {:filename "extra/src/bar/baz.clj"
           :row      1
           :end-row  10}
          {:github/repo         "https://github.com/foo"
           :git/branch          "main"
           :filename-remove-prefix "extra/"})))
  (is (= "https://github.com/foo/blob/main/src/bar/baz.clj#L1-L10"
         (var-source
          {:filename "SRC/BAR/BAZ.CLJ"
           :row      1
           :end-row  10}
          {:github/repo "https://github.com/foo"
           :git/branch  "main"
           :filename-fn str/lower-case})))
  (is (= "http://example.com/main/src/bar/baz.clj;1,2-10,4"
         (var-source
          {:filename "src/bar/baz.clj"
           :row      1
           :col      2
           :end-row  10
           :end-col  4}
          {:source-uri "{repo}{branch}/{filename};{row},{col}-{end-row},{end-col}"
           :github/repo "http://example.com/"
           :git/branch "main"}))))

(deftest var-pattern-test
  (is (= [["`backtick-link`" "backtick-link"]
          ["[[wikilink]]" "wikilink"]]
         (impl/extract-var-links impl/backticks-and-wikilinks-pattern
                                 "aaa `backtick-link` bbb [[wikilink]] ccc"))))

(deftest escape-markdown-test
  (is (= "\\*abc\\*" (impl/escape-markdown "*abc*")))
  (is (= "\\\\\\`\\*\\_\\{\\}\\[\\]&lt;&gt;\\(\\)\\#\\+\\-\\.\\!\\|" (impl/escape-markdown "\\`*_{}[]<>()#+-.!|"))))

(deftest namespace-link-test
  ;; These could be normalized to include less `../`, but browser will handle these fine
  (is (= "../../../promesa/core/" (impl/namespace-link 'promesa.exec.csp 'promesa.core)))
  (is (= "../../promesa/core/" (impl/namespace-link 'promesa.core 'promesa.core)))
  (is (= "../../promesa/exec/csp/" (impl/namespace-link 'promesa.exec 'promesa.exec.csp))))

(deftest format-href-test
  (is (= "a#b" (impl/format-href "a" "b")))
  (is (= "a" (impl/format-href "a" nil)))
  (is (= "#b" (impl/format-href nil "b"))))

(deftest format-docstring-test
  (let [ns->vars {'a.b #{'x->y}}
        opts {:var-regex impl/backticks-and-wikilinks-pattern}]
    (testing "non existing var"
      (is (= "abc `a.b/not-found`"
             (impl/format-docstring ns->vars 'a.b "abc `a.b/not-found`" opts))))

    (testing "qualified var link"
      (is (= "some text [`a.b/x->y`](../../a/b/#x--GT-y) other text"
             (impl/format-docstring ns->vars 'a.b "some text `a.b/x->y` other text" opts))))

    (testing "namespace link"
      (is (= "some text [`a.b`](../../a/b/) other text"
             (impl/format-docstring ns->vars 'a.b "some text `a.b` other text" opts))))

    (testing "unqualified var within current namespace link"
      (is (= "some text [`x->y`](#x--GT-y) other `not-found`"
             (impl/format-docstring ns->vars 'a.b "some text `x->y` other `not-found`" opts))))))
