(ns dinodoc.impl.javadoc-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [dinodoc.api :as dinodoc]
   [dinodoc.fs-helpers :refer [fsdata with-temp-dir]]
   [dinodoc.impl.javadoc :as javadoc]))

(deftest resolve-test
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "demo/Greeter.html" "<body><section id=\"greet(java.lang.String)\"><section id=\"GREETING\">")

      (testing "resolves classes"
        (is (= "demo/Greeter.html" (javadoc/resolve-link dir "demo.Greeter"))))

      (testing "resolves methods"
        (is (= "demo/Greeter.html#greet(java.lang.String)" (javadoc/resolve-link dir "demo.Greeter.greet"))))
          ;; what about multiple arities?

          ;; allow alternative notation with `#` separator?
          ; (is (= "demo/Greeter.html#greet(java.lang.String)" (javadoc/resolve-link javadoc-path "demo.Greeter#greet")))

      (testing "fields"
        (is (= "demo/Greeter.html#GREETING" (javadoc/resolve-link dir "demo.Greeter.GREETING"))))

      (testing "no link for missing definitions"
        (is (= nil (javadoc/resolve-link dir "demo.NonExistingClass")))
        (is (= nil (javadoc/resolve-link dir "demo.Greeter.nonExistingMethod")))))

    #_(testing "non-qualified"
        (is (= "demo/Greeter.html" (javadoc/resolve-link javadoc-path "Greeter"))))))

    ;; warn when multiple candidates?

(deftest generate-with-resolution
  (with-temp-dir
    (fn [{:keys [dir fspit]}]
      (fspit "README.md" "Link to method: [[demo.Greeter.greet]]")

      ;; Simulates generating API docs
      (fspit "api/demo/Greeter.html" "<body><section id=\"greet(java.lang.String)\">")

      (let [output-path (str dir "/docs")
            javadoc-path (str dir "/api")
            resolve-apilink #(javadoc/resolve-link javadoc-path %)
            _ (dinodoc/generate {:inputs [{:path dir
                                           :output-path "."}]
                                 :output-path output-path
                                 :resolve-apilink #(some->> (resolve-apilink %) (str "api/"))})
            data (fsdata output-path)]
        (is (str/includes?
             (get-in data ["index.md"])
             "Link to method: [`demo.Greeter.greet`](pathname://./api/demo/Greeter.html#greet(java.lang.String))"))))))
