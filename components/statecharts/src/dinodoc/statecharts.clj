(ns dinodoc.statecharts
  (:require
   [clojure.string :as str]
   [dinodoc.impl.git :as git]
   [dinodoc.impl.quickdoc.impl :as impl]))

(defn- with-code-block [f]
  (println "```mermaid")
  (f)
  (println "```"))

(defn- render-event-name [state]
  ;; will need to add some string escaping
  (if (keyword? state)
    (name state)
    state))

(defn- render-state-name [state]
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
          (assert (sequential? transitions)
                  "Expecting a sequence of transitions, did you perhaps forget to wrap the machine in (fsm/machine)?")
          (doseq [{:keys [target actions]} transitions]
            (println (render-state-name source-state) "-->"
                     (render-state-name target) ":"
                     (render-event-name event))))
        (when (seq (:states target-machine))
          (render-states source-state target-machine)))))
  (println "}"))

(defn render-machine-diagram
  "Render given machine as a mermaid state diagram to standard output."
  [machine]
  (println "stateDiagram-v2")
  (render-states (:id machine) machine))

(defn render-machine-block
  "Render given machine as a mermaid state diagram wrapped in a markdown fenced code block to standard output."
  [machine]
  (with-code-block #(render-machine-diagram machine)))

(defn render-machine-var
  "Render a var bound to a machine to standard output.
  Using a var has the benefit of being able to include link to the source file including the line location."
  ([machine-var] (render-machine-var machine-var {}))
  ([machine-var {:keys [filename-add-prefix]}]
   (let [{:keys [name file line doc]} (meta machine-var)
         machine @machine-var
         ;; consider ability to allow passing repo info to avoid running detection for each diagram
         repo-info (git/detect-repo-info ".")
         source-url (when-some [{:keys [url branch]} repo-info]
                      (impl/var-source
                       {:filename file :row line :end-row line}
                       {:github/repo url
                        :git/branch branch
                        :filename-add-prefix filename-add-prefix}))]
     (println)
     (println "##" name)
     (when doc
       (println)
       (println doc))
     (println)
     (render-machine-block machine)
     (when source-url
       (println)
       (println (str "[source](" source-url ")"))))))
