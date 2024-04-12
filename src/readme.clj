(ns readme
  (:require
   [dinodoc.api :as api]
   [dinodoc.impl.core :as impl]
   [dinodoc.impl.quickdoc.api :as qd]
   [dinodoc.impl.quickdoc.impl :as qd.impl]))

;; # Generating docs

;; Main entrypoint, consists of two main parts are generating articles and API docs
api/generate

;; ### Generating articles

  ;; Collects article content and resolves links as a data representation
- impl/process-doc-tree-pure

  ;; Writes the processed representation to disk
- impl/process-doc-tree!

;; ### Generating API docs

  ;; For each specified input runs clj-kondo analysis and renders a file for each namespace
- qd/quickdoc

    ;; For each namespace renders some metadata and iterates through var definitions
- - qd.impl/print-namespace

      ;; Bulk of logic to format and print each var
- - - qd.impl/print-var-impl
