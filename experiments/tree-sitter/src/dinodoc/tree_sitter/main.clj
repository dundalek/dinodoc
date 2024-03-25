(ns dinodoc.tree-sitter.main
  (:require
   [clojure.java.io :as io])
  (:import
   (org.treesitter TSParser TreeSitterJson TreeSitterTypescript)))

(defn datafy-point [point]
  {:row (.getRow point)
   :column (.getColumn point)})

(defn datafy-node
  ([node] (datafy-node node nil))
  ([node source]
   (let [child-count (.getChildCount node)]
     (cond-> {:type (.getType node)
              :start-byte (.getStartByte node)
              :end-byte (.getEndByte node)
              :start-point (datafy-point (.getStartPoint node))
              :end-point (datafy-point (.getEndPoint node))}
       source (assoc :content (subs source (.getStartByte node) (.getEndByte node)))
       (pos? child-count) (assoc :children
                                 (->> (range 0 child-count)
                                      (map #(datafy-node (.getChild node %)
                                                         source))))))))
(comment
  (let [parser (TSParser.)
        json (TreeSitterJson.)
        _ (.setLanguage parser json)
        source "[1, null]"
        tree (.parseString parser nil source)
        rootNode (.getRootNode tree)
        arrayNode (.getNamedChild rootNode 0)
        numberNode (.getNamedChild arrayNode 0)]
    (def tree tree)
    (def root rootNode)
    {:tree tree
     :rootNode rootNode
     :arrayNode arrayNode
     :numberNode numberNode})

  (-> (bean root)
      (update-vals
       (fn [x]
         (if (re-find #"^class (java|clojure)\." (str (class x)))
           x
           (bean x)))))

  (tap> (bean (.getStartPoint root)))
  (tap> (bean (.getChild root 0)))

  (.printDotGraphs tree (io/file "tree.dot"))

  (datafy-node root)

  ;; getting node content from source with start/end offsets
  (let [source "[1, null]"]
    (datafy-node root source))

  (let [parser (TSParser.)
        language (TreeSitterTypescript.)
        _ (.setLanguage parser language)
        source (slurp "../../examples/ts/src/index.ts")
        tree (.parseString parser nil source)
        rootNode (.getRootNode tree)]
    (tap> (datafy-node rootNode source))))
