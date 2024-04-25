(ns dinodoc.structurizr
  (:require
   [dinodoc.generator :as generator]
   [dinodoc.structurizr.impl.core :as impl]))

(deftype StructurizrGenerator [opts ^:volatile-mutable workspace ^:volatile-mutable index]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [workspace-file]} opts]
      (set! workspace (impl/load-workspace workspace-file))
      (impl/set-element-urls workspace)
      (set! index (impl/build-index workspace))))
  (resolve-link [_ target]
    (get index target))
  (generate [_ {:keys [output-path]}]
    (impl/render-workspace {:workspace workspace
                            :output-path output-path})))

(defn make-generator
  "Generate architecture documentation with diagrams from a Structurizr workspace.

Options:
* `:workspace-file` - Structurizr workspace file, accepts DSL or JSON format, for example `\"some/path/workspace.dsl\"`"
  [opts]
  (->StructurizrGenerator opts nil nil))
