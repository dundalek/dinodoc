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


### ExecutorBulkheadTask {#ExecutorBulkheadTask}


### IBulkhead {#IBulkhead}


Bulkhead main API

*protocol*


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

### create {#create}
``` clojure
(create & {:keys [type], :as params})
```


### get\-stats {#get-stats}
``` clojure
(get-stats instance)
```


### invoke\! {#invoke-BANG-}
``` clojure
(invoke! instance f)
```


### log\! {#log-BANG-}
``` clojure
(log! & params)
```


*macro*

