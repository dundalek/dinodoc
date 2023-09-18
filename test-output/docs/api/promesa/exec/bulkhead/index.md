---
sidebar_label: bulkhead
title: promesa.exec.bulkhead
toc_min_heading_level: 2
toc_max_heading_level: 4
---

# <a name="promesa.exec.bulkhead">promesa.exec.bulkhead</a>


Bulkhead pattern: limiter of concurrent executions.




## <a name="promesa.exec.bulkhead/->ExecutorBulkheadTask">`->ExecutorBulkheadTask`</a><a name="promesa.exec.bulkhead/->ExecutorBulkheadTask"></a>
``` clojure

(->ExecutorBulkheadTask bulkhead f inst)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L54-L68">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/-get-stats">`-get-stats`</a><a name="promesa.exec.bulkhead/-get-stats"></a>
``` clojure

(-get-stats _)
```

Get internal statistics of the bulkhead instance
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L42-L42">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/-invoke!">`-invoke!`</a><a name="promesa.exec.bulkhead/-invoke!"></a>
``` clojure

(-invoke! _ f)
```

Call synchronously a function under bulkhead context
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L43-L43">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/-offer!">`-offer!`</a><a name="promesa.exec.bulkhead/-offer!"></a>
``` clojure

(-offer! _ _)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L38-L38">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/-poll!">`-poll!`</a><a name="promesa.exec.bulkhead/-poll!"></a>
``` clojure

(-poll! _)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L37-L37">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/ExecutorBulkheadTask">`ExecutorBulkheadTask`</a><a name="promesa.exec.bulkhead/ExecutorBulkheadTask"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L54-L68">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/IBulkhead">`IBulkhead`</a><a name="promesa.exec.bulkhead/IBulkhead"></a>




Bulkhead main API
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L40-L43">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/IQueue">`IQueue`</a><a name="promesa.exec.bulkhead/IQueue"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L36-L38">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/bulkhead?">`bulkhead?`</a><a name="promesa.exec.bulkhead/bulkhead?"></a>
``` clojure

(bulkhead? o)
```

Check if the provided object is instance of Bulkhead type.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L205-L208">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/create">`create`</a><a name="promesa.exec.bulkhead/create"></a>
``` clojure

(create & {:keys [type], :as params})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L190-L195">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/get-stats">`get-stats`</a><a name="promesa.exec.bulkhead/get-stats"></a>
``` clojure

(get-stats instance)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L197-L199">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/invoke!">`invoke!`</a><a name="promesa.exec.bulkhead/invoke!"></a>
``` clojure

(invoke! instance f)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L201-L203">Source</a></sub></p>

## <a name="promesa.exec.bulkhead/log!">`log!`</a><a name="promesa.exec.bulkhead/log!"></a>
``` clojure

(log! & params)
```
Function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/bulkhead.clj#L28-L32">Source</a></sub></p>
