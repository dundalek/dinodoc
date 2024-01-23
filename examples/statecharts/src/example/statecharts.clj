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
