(ns stack-graphs.main
  (:require
   [cheshire.core :as json]
   [clojure.java.shell :as sh]
   [loom.graph :as g]
   [loom.io :as lio]))

(def graph (json/parse-string (slurp "target/graph.json") true))

(def nodes
  (->> graph
       :nodes
       (map (fn [node]
              [(-> node :id :local_id) node]))
       (into {})))

(def g
  (apply g/digraph
         (->> graph
              :edges
              (map (fn [{:keys [source sink]}]
                     [(:local_id source) (:local_id sink)])))))

;; Visualize using graphviz
(do
  (spit "target/graph.dot"
        (lio/dot-str g
                     {:node-label (fn [id]
                                    (let [node (get nodes id)
                                          {:keys [syntax_type]} (:source_info node)]
                                      (cond-> (format "%s %d %s" (:type node) id (:symbol node))
                                        syntax_type (str " (" syntax_type ")"))))}))
  (sh/sh "dot" "-Tpng" "target/graph.dot" "-o" "target/graph.png"))

;; Nodes

(->> graph
     :nodes
     (map :type)
     (frequencies))

{"root" 1,
 "jump_to_scope" 1,
 "scope" 510,
 "push_symbol" 85,
 "pop_symbol" 58,
 "push_scoped_symbol" 7}

(->> graph
     :nodes
     (count))

(->> graph
     :nodes
     (filter (comp #{"root"} :type)))

(g/out-edges g 1)
(g/in-edges g 1)

(->> graph
     :nodes
     (mapcat keys)
     (frequencies))

{:is_exported 510,
 :symbol 150,
 :type 662,
 :scope 7,
 :debug_info 662,
 :is_definition 58,
 :id 662,
 :is_reference 92,
 :source_info 644}

(->> graph
     :nodes
     (filter :is_exported))

(->> graph
     :nodes
     (filter :is_definition)
     (map #(-> %
               (dissoc :debug_info)
               (update :source_info dissoc :span)
               (update :id dissoc :file))))

(->> graph
     :nodes
     (filter #(-> % :source_info :syntax_type)))

;; Edges

(->> graph
     :edges
     (mapcat keys)
     (frequencies))

{:source 450, :sink 450, :precedence 450, :debug_info 450}

(->> graph
     :edges
     (first))
