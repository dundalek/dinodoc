(ns dinodoc.statecharts
  (:require
   [clojure.string :as str]
   [dinodoc.impl.git :as git]
   [dinodoc.impl.quickdoc.impl :as impl]))

(defn with-code-block [f]
  (println "```mermaid")
  (f)
  (println "```"))

(defn render-event-name [state]
  ;; will need to add some string escaping
  (if (keyword? state)
    (name state)
    state))

(defn render-state-name [state]
  ;; will need to add some string escaping
  (-> (if (keyword? state)
        (name state)
        state)
      (str/replace #"-" "_")))

(defn- render-states [name machine]
  (println "state" (render-state-name name) "{")
  (if (= (:type machine) :parallel)
    ;; Alternative to render parallel regions would be to separate transitions with `--`:
    ;; https://mermaid.js.org/syntax/stateDiagram.html#concurrency
    ;; But given clj-statecharts defines regions in a map, we will always have
    ;; a key for regions so they can rendered as nested states.
    (doseq [[name states] (:regions machine)]
      (render-states name states))
    (do
      (when-some [initial (:initial machine)]
        (println "[*] -->" (render-state-name initial)))
      (doseq [[source-state target-machine] (:states machine)]
        (doseq [[event transitions] (:on target-machine)]
          (doseq [{:keys [target actions]} transitions]
            (println (render-state-name source-state) "-->"
                     (render-state-name target) ":"
                     (render-event-name event))))
        (when (seq (:states target-machine))
          (render-states source-state target-machine)))))
  (println "}"))

(defn render-machine-diagram [machine]
  (println "stateDiagram-v2")
  (render-states (:id machine) machine))

(defn render-machine-block [machine]
  (with-code-block #(render-machine-diagram machine)))

(defn render-machine-var
  ([machine-var] (render-machine-var machine-var {}))
  ([machine-var {:keys [filename-add-prefix]}]
   (let [{:keys [name file line]} (meta machine-var)
         machine @machine-var
         ;; consider ability to allow passing repo info to avoid running detection for each diagram
         {:keys [url branch]} (git/detect-repo-info ".")
         source-url (impl/var-source
                     {:filename file :row line :end-row line}
                     {:github/repo url
                      :git/branch branch
                      :filename-add-prefix filename-add-prefix})]
     (println)
     (println "##" name)
     (println)
     (render-machine-block machine)
     (println)
     (println (str "[source](" source-url ")")))))
