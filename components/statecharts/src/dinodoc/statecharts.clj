(ns dinodoc.statecharts
  (:require
   [dinodoc.impl.quickdoc.impl :as impl]
   [dinodoc.impl.git :as git]))

(defn with-code-block [f]
  (println "```mermaid")
  (f)
  (println "```"))

(defn render-state-name [state]
  ;; will need to add some string escaping
  (if (keyword? state)
    (name state)
    state))

(defn render-machine-diagram [machine]
  (println "stateDiagram-v2")
  (println "state" (render-state-name (:id machine)) "{")
  (when-some [initial (:initial machine)]
    (println "[*] -->" (render-state-name initial)))
  (doseq [[source-state {:keys [on]}] (:states machine)]
    (doseq [[event transitions] on]
      (doseq [{:keys [target actions]} transitions]
        (println (render-state-name source-state) "-->"
                 (render-state-name target) ":"
                 (render-state-name event)))))
  (println "}"))

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
