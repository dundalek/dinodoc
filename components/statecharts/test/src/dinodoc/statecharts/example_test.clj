(ns dinodoc.statecharts.example-test
  (:require
   [clojure.test :refer [deftest is]]
   [statecharts.core :as fsm]
   [example.statecharts :refer [machine]]))

;; Running the machine itself to make sure the source representation is valid.
(deftest example-machine
  (let  [service (fsm/service machine)]
    (is (= {:_state :red} (fsm/start service)))
    (is (= {:_state :green} (fsm/send service :timer)))
    (is (= {:_state :yellow} (fsm/send service :timer)))
    (is (= "Firing the traffic cameras!\n"
           (with-out-str
             (is (= {:_state :red} (fsm/send service :timer))))))))
