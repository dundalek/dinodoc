(ns example.statecharts
  (:require
   [statecharts.core :as fsm]))

(defn fire-cameras [& _]
  (println "Firing the traffic cameras!"))

;; https://lucywang000.github.io/clj-statecharts/docs/actions/#a-full-example
(def machine
  (fsm/machine
   {:id :lights
    :initial :red
    :states
    {:green {:on
             {:timer :yellow}}
     :yellow {:on
              {:timer {:target :red
                       :actions fire-cameras}}}
     :red {:on
           {:timer :green}}}

    :on {:power-outage :red}}))

;; https://statecharts.dev/what-is-a-statechart.html#a-state-can-have-many-regions
(def regions
  (fsm/machine
   {:id :A
    :type :parallel
    :regions
    {:bc {:initial :B
          :states {:B {:on {:flick :C}}
                   :C {:on {:after-1s :B}}}}
     :de {:initial :D
          :states {:D {:on {:flick :E}}
                   :E {:on {:flick :D}}}}}}))
