(ns dinodoc.statecharts.example-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [example.statecharts :as example :refer [machine]]
   [statecharts.core :as fsm]))

;; Running the machine itself to make sure the source representation is valid.
(deftest example-machine
  (let [service (fsm/service machine)]
    (is (= {:_state :red} (fsm/start service)))
    (is (= {:_state :green} (fsm/send service :timer)))
    (is (= {:_state :yellow} (fsm/send service :timer)))
    (is (= "Firing the traffic cameras!\n"
           (with-out-str
             (is (= {:_state :red} (fsm/send service :timer))))))))

(deftest regions-example
  (let [service (fsm/service example/regions)
        initial-state {:_state {:bc :B :de :D}}]
    (is (= initial-state (fsm/start service)))
    (testing "first flick event transitions both regions"
      (is (= {:_state {:bc :C :de :E}} (fsm/send service :flick))))
    (testing "second flick event only transitions :de region"
      (is (= {:_state {:bc :C :de :D}} (fsm/send service :flick))))
    (testing "final after-1s event transitions the :bc region back to initial state"
      (is (= initial-state (fsm/send service :after-1s))))))
