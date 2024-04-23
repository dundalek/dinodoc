(ns dinodoc.impl.linking-test
  (:require
   [clojure.test :refer [deftest is testing]]))

(defn suffixes [segments]
  (loop [segments (seq segments)
         coll []]
    (if segments
      (recur
       (next segments)
       (conj coll segments))
      coll)))

(defn make []
  {:prefix {}
   :suffix {}})

(defn add [index segments item]
  (let [suffix (reduce (fn [m s]
                         (update-in m s update ::items conj item))
                       (:suffix index)
                       (suffixes segments))]
    (-> index
        (update :prefix update-in segments update ::items conj item)
        (assoc :suffix suffix))))

(defn lookup [index segments]
  (or (-> index :prefix (get-in segments) ::items first)
      (some (fn [x]
              (-> index :suffix (get-in x) ::items first))
            (suffixes segments))))

(deftest lookup-test
  (is (= [["a" "b" "c"]
          ["b" "c"]
          ["c"]]
         (suffixes ["a" "b" "c"])))

  (let [index (-> (make)
                  (add ["a" "b" "c"] :a.b.c)
                  (add ["a"] :a))]
    (testing "basic prefix"
      (is (= :a (lookup index ["a"])))
      (is (= :a.b.c (lookup index ["a" "b" "c"]))))

    (testing "suffix"
      (is (= :a.b.c (lookup index ["c"])))
      (is (= :a.b.c (lookup index ["b" "c"])))))

  (let [index (-> (make)
                  (add ["b" "c"] :b.c)
                  (add ["a" "b" "c"] :a.b.c))]
    (testing "prefix has precedence over suffix"
      (is (= :b.c (lookup index ["b" "c"]))))))
