(ns dinodoc.impl.javadoc-test
  (:require
   [clojure.test :refer [deftest is]]
   [dinodoc.impl.javadoc :as javadoc]))

(def javadoc-path "examples/java/out")

(deftest foo
  (is (= "demo/Greeter.html" (javadoc/resolve javadoc-path "demo.Greeter")))

  (is (= nil (javadoc/resolve javadoc-path "demo.NonExisting")))

  (is (= "demo/Greeter.html#greet(java.lang.String)" (javadoc/resolve javadoc-path "demo.Greeter.greet")))
  ; (is (= "demo/Greeter.html#greet(java.lang.String)" (javadoc/resolve javadoc-path "demo.Greeter#greet")))

  ;; what about multiple arities?

  #_(testing "non-qualified"
      (is (= "demo/Greeter.html" (javadoc/resolve javadoc-path "Greeter")))))

  ;; warn when multiple candidates?

