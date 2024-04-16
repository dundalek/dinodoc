(ns dinodoc.impl.javadoc-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is]]
   [dinodoc.api :as dinodoc]
   [dinodoc.fs-helpers :refer [fsdata with-temp-dir]]
   [dinodoc.impl.javadoc :as javadoc]))

; (def javadoc-path "examples/java/out")

(deftest resolve-test
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (let [javadoc-path dir]
        (fspit "demo/Greeter.html" "<body><section id=\"greet(java.lang.String)\">")

        (is (= "demo/Greeter.html" (javadoc/resolve javadoc-path "demo.Greeter")))

        (is (= nil (javadoc/resolve javadoc-path "demo.NonExisting")))

        (is (= "demo/Greeter.html#greet(java.lang.String)" (javadoc/resolve javadoc-path "demo.Greeter.greet")))))
    ; (is (= "demo/Greeter.html#greet(java.lang.String)" (javadoc/resolve javadoc-path "demo.Greeter#greet")))

    ;; what about multiple arities?

    #_(testing "non-qualified"
        (is (= "demo/Greeter.html" (javadoc/resolve javadoc-path "Greeter"))))))

    ;; warn when multiple candidates?

(deftest generate-with-resolution
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "README.md" "Link to method: [[demo.Greeter.greet]]")

      ;; Simulates generating API docs
      (fspit "api/demo/Greeter.html" "<body><section id=\"greet(java.lang.String)\">")

      (let [output-path (str dir "/docs")
            javadoc-path (str dir "/api")
            resolve-apilink #(javadoc/resolve javadoc-path %)
            _ (dinodoc/generate {:inputs [{:path dir
                                           :output-path "."}]
                                 :output-path output-path
                                 :resolve-apilink resolve-apilink})
            data (fsdata output-path)]
        (is (str/includes?
             (get-in data ["index.md"])
             "Link to method: [`demo.Greeter.greet`](pathname://./api/demo/Greeter.html#greet(java.lang.String))"))))))
