(ns dinodoc.structurizr
  (:require
   [dinodoc.generator :as generator]
   [dinodoc.structurizr.impl.core :as impl]))

(defn generate
  "Generate architecture documentation with diagrams from a Structurizr workspace.

Options:
* `:workspace-file` - Structurizr workspace file, accepts DSL or JSON format, for example `\"some/path/workspace.dsl\"`
* `:output-path` - Directory where to output the documentation
* `:base-path` - Pass a location in the depolyment if nested in a different directory than root `/`. (string, optional)
  * This option is a workaround. In the future we should implement relative paths to make it unnecesary."
  [{:keys [workspace-file output-path base-path]}]
  (let [workspace (impl/load-workspace workspace-file)]
    (impl/render-workspace {:workspace workspace
                            :output-path output-path
                            :base-path base-path})))

(deftype StructurizrGenerator [opts ^:volatile-mutable workspace ^:volatile-mutable index]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [workspace-file base-path]} opts]
      (set! workspace (impl/load-workspace workspace-file))
      (impl/set-element-urls {:workspace workspace
                              :workspace-edn (impl/workspace->data workspace)}
                             base-path)
      (set! index (impl/build-index workspace base-path))))
  (resolve-link [_ target]
    (get index target))
  (generate [_ {:keys [output-path]}]
    (generate (assoc opts
                     :output-path output-path))))

(defn make-generator [opts]
  (->StructurizrGenerator opts nil nil))
