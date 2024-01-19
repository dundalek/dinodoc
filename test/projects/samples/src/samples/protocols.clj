(ns samples.protocols)

(defprotocol IQueue
  (-poll! [_])
  (-offer! [_ _]))

(defprotocol IBulkhead
  "Bulkhead main API"
  (-get-stats [_] "Get internal statistics of the bulkhead instance")
  (-invoke! [_ f] "Call synchronously a function under bulkhead context"))
