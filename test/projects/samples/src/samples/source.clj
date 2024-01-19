(ns ^{:doc "docstring on namespace"
      :deprecated true}
 samples.source)

(defn foo
  "Hello, there is also `Foo` that differs in casing."
  [])

(defn Foo
  "Foo with different case, link does not clash with `foo`."
  [])

(def ^{:dynamic true
       :doc "We need to escape var names otherwise earmuffs are not shown but interpreted as italic."}
  *dynamic-var-example* nil)

(defn _underscore-surrounded_ [])

(defn has->arrow [])

(defn <<
  "Angle brackets in var name"
  [])

(defn deprecated-tag
  "Doc string"
  {:deprecated true}
  [])

(defn deprecated-version
  "Doc string"
  {:deprecated "9.0"}
  [])

(defn added-tag
  {:added true}
  [])

(defn added-version
  {:added "8.0"}
  [])

(defn added-and-deprecated
  {:added "8.0"
   :deprecated true}
  [])

(defmacro a-macro
  "hello"
  [])

(defn links-backticks
  "Link to a var in the current namespace: `foo`

  Link to a qualified var: `samples.crossplatform/some-clj-fn`

  Link to a namespace: `samples.protocols`

  Link to a var containing a special character: `has->arrow`
  "
  [])

(defn links-backticks-in-codeblock
  " In a code block should ideally not be replaced.
  ```clojure
  ;; Link to a var in the current namespace: `foo`

  ;; Link to a qualified var: `samples.crossplatform/some-clj-fn`

  ;; Link to a namespace: `samples.protocols`
  ```"
  [])

(defn links-backticks-multiple-occurences
  "Same link referenced multiple times will get messed up: `foo` and `foo`"
  [])

(defn links-wikilinks
  "Link to a var in the current namespace: [[foo]]

  Link to a qualified var: [[samples.crossplatform/some-clj-fn]]

  Link to a namespace: [[samples.protocols]]

  Link with a title supported by codox: [[samples.crossplatform/some-clj-fn|some-title]]"
  [])
