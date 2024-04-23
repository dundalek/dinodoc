(ns dinodoc.generator)

(defprotocol Generator
  (prepare-index [this])
  (resolve-link [this target])
  (generate [this opts]))
