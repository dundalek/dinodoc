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

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L54-L68">Source</a></sub></p>

### ExecutorBulkheadTask {#ExecutorBulkheadTask}

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L54-L68">Source</a></sub></p>

### IBulkhead {#IBulkhead}


Bulkhead main API

*protocol*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L40-L43">Source</a></sub></p>

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

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L36-L38">Source</a></sub></p>

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
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L205-L208">Source</a></sub></p>

### create {#create}
``` clojure
(create & {:keys [type], :as params})
```

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L190-L195">Source</a></sub></p>

### get\-stats {#get-stats}
``` clojure
(get-stats instance)
```

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L197-L199">Source</a></sub></p>

### invoke\! {#invoke-BANG-}
``` clojure
(invoke! instance f)
```

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L201-L203">Source</a></sub></p>

### log\! {#log-BANG-}
``` clojure
(log! & params)
```


*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/exec/bulkhead.clj#L28-L32">Source</a></sub></p>
