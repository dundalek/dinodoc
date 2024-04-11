---
sidebar_label: exec
title: promesa.exec
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

Executors & Schedulers facilities.




### \*default\-executor\* {#-STAR-default-executor-STAR-}


*dynamic*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L59-L59)


### \*default\-scheduler\* {#-STAR-default-scheduler-STAR-}


*dynamic*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L58-L58)


### \-&gt;ScheduledTask {#--GT-ScheduledTask}


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L53-L53)


### cached\-executor {#cached-executor}
``` clojure
(cached-executor & {:keys [max-size factory keepalive], :or {keepalive 60000, max-size Integer/MAX_VALUE}})
```


A cached thread executor pool constructor.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L417-L428)


### compile\-if\-virtual {#compile-if-virtual}
``` clojure
(compile-if-virtual then else)
```


*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L74-L76)


### compile\-when\-virtual {#compile-when-virtual}
``` clojure
(compile-when-virtual body)
```


*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L77-L78)


### configure\-default\-executor\! {#configure-default-executor-BANG-}
``` clojure
(configure-default-executor! & params)
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L573-L581)


### current\-thread {#current-thread}
``` clojure
(current-thread)
```


Return the current thread.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L756-L759)


### current\-thread\-executor {#current-thread-executor}
``` clojure
(current-thread-executor)
```


Creates an executor instance that run tasks in the same thread.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L446-L474)


### default\-current\-thread\-executor {#default-current-thread-executor}


Default Executor instance that runs the task in the same thread.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L108-L110)


### default\-executor {#default-executor}


Default executor instance, ForkJoinPool/commonPool in JVM, MicrotaskExecutor on JS.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L100-L105)


### default\-scheduler {#default-scheduler}


Default scheduled executor instance.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L93-L98)


### exec\! {#exec-BANG-}
``` clojure
(exec! f)
(exec! executor f)
```


Run the task in the provided executor, returns `nil`. Analogous to
  the `(.execute executor f)`. Fire and forget.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L242-L248)


### executor? {#executor-QMARK-}
``` clojure
(executor? o)
```


Returns true if `o` is an instane of Executor or satisfies IExecutor protocol.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L138-L143)


### fixed\-executor {#fixed-executor}
``` clojure
(fixed-executor & {:keys [parallelism factory]})
```


A fixed thread executor pool constructor.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L431-L436)


### fn\-&gt;thread {#fn--GT-thread}
``` clojure
(fn->thread f & {:keys [daemon start priority name], :or {daemon true, start true, priority Thread/NORM_PRIORITY}})
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L714-L723)


### forkjoin\-executor {#forkjoin-executor}
``` clojure
(forkjoin-executor
 &
 {:keys [factory async parallelism keepalive core-size max-size], :or {max-size 32767, async true, keepalive 60000}})
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L546-L564)


### forkjoin\-thread\-factory {#forkjoin-thread-factory}
``` clojure
(forkjoin-thread-factory & {:keys [name daemon], :or {name "promesa/forkjoin/%s", daemon true}})
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L323-L333)


### get\-available\-processors {#get-available-processors}
``` clojure
(get-available-processors)
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L89-L91)


### get\-name {#get-name}
``` clojure
(get-name)
(get-name thread)
```


Retrieve thread name

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L768-L772)


### get\-thread\-id {#get-thread-id}
``` clojure
(get-thread-id)
(get-thread-id thread)
```


Retrieves the thread ID.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L794-L799)


### interrupt\! {#interrupt-BANG-}
``` clojure
(interrupt!)
(interrupt! thread)
```


Interrupt a thread.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L811-L816)


### interrupted? {#interrupted-QMARK-}
``` clojure
(interrupted?)
(interrupted? thread)
```


Check if the thread has the interrupted flag set.

  There are two special cases:

  Using the `:current` keyword as argument will check the interrupted
  flag on the current thread.

  Using the arity 0 (passing no arguments), then the current thread
  will be checked and **WARNING** the interrupted flag reset to
  `false`.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L775-L791)


### pmap {#pmap}
``` clojure
(pmap f coll)
(pmap f coll & colls)
```


Analogous to the `clojure.core/pmap` with the excetion that it allows
  use a custom executor (binded to *default-executor* var) The default
  clojure chunk size (32) is used for evaluation and the real
  parallelism is determined by the provided executor.


  **EXPERIMENTAL API:** This function should be considered
  EXPERIMENTAL and may be changed or removed in future versions until
  this notification is removed.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L667-L694)


### run\! {#run-BANG-}
``` clojure
(run! f)
(run! executor f)
```


Run the task in the provided executor.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L250-L255)


### schedule\! {#schedule-BANG-}
``` clojure
(schedule! ms f)
(schedule! scheduler ms f)
```


Schedule a callable to be executed after the `ms` delay
  is reached.

  In JVM it uses a scheduled executor service and in JS
  it uses the `setTimeout` function.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L268-L277)


### scheduled\-executor {#scheduled-executor}
``` clojure
(scheduled-executor & {:keys [parallelism factory], :or {parallelism 1}})
```


A scheduled thread pool constructor. A ScheduledExecutor (IScheduler
  in CLJS) instance allows execute asynchronous tasks some time later.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L515-L529)


### set\-name\! {#set-name-BANG-}
``` clojure
(set-name! name)
(set-name! thread name)
```


Rename thread.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L762-L765)


### shutdown\! {#shutdown-BANG-}
``` clojure
(shutdown! executor)
```


Shutdowns the executor service.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L146-L149)


### shutdown\-now\! {#shutdown-now-BANG-}
``` clojure
(shutdown-now! executor)
```


Shutdowns and interrupts the executor service.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L152-L155)


### shutdown? {#shutdown-QMARK-}
``` clojure
(shutdown? executor)
```


Check if execitor is in shutdown state.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L158-L161)


### single\-executor {#single-executor}
``` clojure
(single-executor & {:keys [factory]})
```


A single thread executor pool constructor.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L439-L444)


### sleep {#sleep}
``` clojure
(sleep ms)
```


Turn the current thread to sleep accept a number of milliseconds or
  Duration instance.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L825-L831)


### submit\! {#submit-BANG-}
``` clojure
(submit! f)
(submit! executor f)
```


Submit a task to be executed in a provided executor
  and return a promise that will be completed with
  the return value of a task.

  A task is a plain clojure function.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L257-L266)


### thread {#thread}
``` clojure
(thread opts & body)
```


A low-level, not-pooled thread constructor, it accepts an optional
  map as first argument and the body. The options map is interepreted
  as options if a literal map is provided. The available options are:
  `:name`, `:priority`, `:daemon` and `:virtual`. The `:virtual`
  option is ignored if you are using a JVM that has no support for
  Virtual Threads.

*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L726-L739)


### thread\-call {#thread-call}
``` clojure
(thread-call f & {:as opts})
```


Advanced version of `p/thread-call` that creates and starts a thread
  configured with `opts`. No executor service is used, this will start
  a plain unpooled thread.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L742-L753)


### thread\-factory {#thread-factory}
``` clojure
(thread-factory
 &
 {:keys [name daemon priority], :or {daemon true, priority Thread/NORM_PRIORITY, name "promesa/thread/%s"}})
```


Returns an instance of promesa default thread factory.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L308-L320)


### thread\-factory? {#thread-factory-QMARK-}
``` clojure
(thread-factory? o)
```


Checks if `o` is an instance of ThreadFactory

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L282-L285)


### thread\-id {#thread-id}
``` clojure
(thread-id)
(thread-id thread)
```


Retrieves the thread ID.

*deprecated in 11.0*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L802-L808)


### thread\-per\-task\-executor {#thread-per-task-executor}
``` clojure
(thread-per-task-executor & {:keys [factory]})
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L533-L537)


### thread? {#thread-QMARK-}
``` clojure
(thread? t)
```


Check if provided object is a thread instance.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L819-L822)


### throw\-uncaught\! {#throw-uncaught-BANG-}
``` clojure
(throw-uncaught! cause)
```


Throw an exception to the current uncaught exception handler.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L834-L841)


### virtual\-threads\-available? {#virtual-threads-available-QMARK-}


Var that indicates the availability of virtual threads.

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L61-L70)


### vthread\-per\-task\-executor {#vthread-per-task-executor}
``` clojure
(vthread-per-task-executor)
```


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L541-L543)


### vthread\-supported? {#vthread-supported-QMARK-}


backward compatibility alias for [`virtual-threads-available?`](#virtual-threads-available-QMARK-)

*deprecated*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L81-L84)


### with\-dispatch {#with-dispatch}
``` clojure
(with-dispatch executor & body)
```


Helper macro for dispatch execution of the body to an executor
  service. The returned promise is not cancellable (the body will be
  executed independently of the cancellation).

*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L619-L625)


### with\-dispatch\! {#with-dispatch-BANG-}
``` clojure
(with-dispatch! executor & body)
```


Blocking version of [`with-dispatch`](#with-dispatch). Useful when you want to
  dispatch a blocking operation to a separated thread and join current
  thread waiting for result; effective when current thread is virtual
  thread.

*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L627-L642)


### with\-executor {#with-executor}
``` clojure
(with-executor executor & body)
```


Binds the *default-executor* var with the provided executor,
  executes the macro body. It also can optionally shutdown or shutdown
  and interrupt on termination if you provide `^:shutdown` and
  `^:interrupt` metadata.

  **EXPERIMENTAL API:** This function should be considered
  EXPERIMENTAL and may be changed or removed in future versions until
  this notification is removed.

*macro*


[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L644-L664)


### work\-stealing\-executor {#work-stealing-executor}
``` clojure
(work-stealing-executor & params)
```


An alias for the [`forkjoin-executor`](#forkjoin-executor).

[source](/blob/master/test/projects/promesa/src/promesa/exec.cljc#L567-L570)

