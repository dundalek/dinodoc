(ns ^{:doc "This a docstring on a namespace."}
 example.main)

(defn greet
  "Returns a greeting message for a given name."
  [name]
  (str "Hello, " name "!"))

(defn -main []
  (println (greet "World")))
