---
sidebar_label: bulkhead
title: promesa.exec.bulkhead
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

Bulkhead pattern: limiter of concurrent executions.




### \-&gt;ExecutorBulkheadTask {#--GT-ExecutorBulkheadTask}
``` clojure
(->ExecutorBulkheadTask bulkhead f inst)
```


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L54-L68)


### ExecutorBulkheadTask {#ExecutorBulkheadTask}


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L54-L68)


### IBulkhead {#IBulkhead}


Bulkhead main API

*protocol*


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L40-L43)


#### \-get\-stats {#-get-stats}
``` clojure
(-get-stats _)
```


Get internal statistics of the bulkhead instance

*protocol*


#### \-invoke\! {#-invoke-BANG-}
``` clojure
(-invoke! _ f)
```


Call synchronously a function under bulkhead context

*protocol*


### IQueue {#IQueue}


*protocol*


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L36-L38)


#### \-offer\! {#-offer-BANG-}
``` clojure
(-offer! _ _)
```


*protocol*


#### \-poll\! {#-poll-BANG-}
``` clojure
(-poll! _)
```


*protocol*


### bulkhead? {#bulkhead-QMARK-}
``` clojure
(bulkhead? o)
```


Check if the provided object is instance of Bulkhead type.

[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L205-L208)


### create {#create}
``` clojure
(create & {:keys [type], :as params})
```


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L190-L195)


### get\-stats {#get-stats}
``` clojure
(get-stats instance)
```


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L197-L199)


### invoke\! {#invoke-BANG-}
``` clojure
(invoke! instance f)
```


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L201-L203)


### log\! {#log-BANG-}
``` clojure
(log! & params)
```


*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L28-L32)

