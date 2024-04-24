(ns dinodoc.generator)

(defprotocol Generator
  (prepare-index [_])
  (resolve-link [_ target])
  (generate [_ opts]))
