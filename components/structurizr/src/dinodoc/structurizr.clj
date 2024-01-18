(ns dinodoc.structurizr
  (:require
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
