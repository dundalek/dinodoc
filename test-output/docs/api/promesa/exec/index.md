---
sidebar_label: exec
title: promesa.exec
toc_min_heading_level: 2
toc_max_heading_level: 4
---

# <a name="promesa.exec">promesa.exec</a>


Executors & Schedulers facilities.




## <a name="promesa.exec/*default-executor*">`*default-executor*`</a><a name="promesa.exec/*default-executor*"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L59-L59">Source</a></sub></p>

## <a name="promesa.exec/*default-scheduler*">`*default-scheduler*`</a><a name="promesa.exec/*default-scheduler*"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L58-L58">Source</a></sub></p>

## <a name="promesa.exec/->ScheduledTask">`->ScheduledTask`</a><a name="promesa.exec/->ScheduledTask"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L53-L53">Source</a></sub></p>

## <a name="promesa.exec/cached-executor">`cached-executor`</a><a name="promesa.exec/cached-executor"></a>
``` clojure

(cached-executor & {:keys [max-size factory keepalive], :or {keepalive 60000, max-size Integer/MAX_VALUE}})
```

A cached thread executor pool constructor.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L417-L428">Source</a></sub></p>

## <a name="promesa.exec/compile-if-virtual">`compile-if-virtual`</a><a name="promesa.exec/compile-if-virtual"></a>
``` clojure

(compile-if-virtual then else)
```
Function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L74-L76">Source</a></sub></p>

## <a name="promesa.exec/compile-when-virtual">`compile-when-virtual`</a><a name="promesa.exec/compile-when-virtual"></a>
``` clojure

(compile-when-virtual body)
```
Function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L77-L78">Source</a></sub></p>

## <a name="promesa.exec/configure-default-executor!">`configure-default-executor!`</a><a name="promesa.exec/configure-default-executor!"></a>
``` clojure

(configure-default-executor! & params)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L573-L581">Source</a></sub></p>

## <a name="promesa.exec/current-thread">`current-thread`</a><a name="promesa.exec/current-thread"></a>
``` clojure

(current-thread)
```

Return the current thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L756-L759">Source</a></sub></p>

## <a name="promesa.exec/current-thread-executor">`current-thread-executor`</a><a name="promesa.exec/current-thread-executor"></a>
``` clojure

(current-thread-executor)
```

Creates an executor instance that run tasks in the same thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L446-L474">Source</a></sub></p>

## <a name="promesa.exec/default-current-thread-executor">`default-current-thread-executor`</a><a name="promesa.exec/default-current-thread-executor"></a>




Default Executor instance that runs the task in the same thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L108-L110">Source</a></sub></p>

## <a name="promesa.exec/default-executor">`default-executor`</a><a name="promesa.exec/default-executor"></a>




Default executor instance, ForkJoinPool/commonPool in JVM, MicrotaskExecutor on JS.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L100-L105">Source</a></sub></p>

## <a name="promesa.exec/default-scheduler">`default-scheduler`</a><a name="promesa.exec/default-scheduler"></a>




Default scheduled executor instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L93-L98">Source</a></sub></p>

## <a name="promesa.exec/exec!">`exec!`</a><a name="promesa.exec/exec!"></a>
``` clojure

(exec! f)
(exec! executor f)
```

Run the task in the provided executor, returns `nil`. Analogous to
  the `(.execute executor f)`. Fire and forget.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L242-L248">Source</a></sub></p>

## <a name="promesa.exec/executor?">`executor?`</a><a name="promesa.exec/executor?"></a>
``` clojure

(executor? o)
```

Returns true if `o` is an instane of Executor or satisfies IExecutor protocol.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L138-L143">Source</a></sub></p>

## <a name="promesa.exec/fixed-executor">`fixed-executor`</a><a name="promesa.exec/fixed-executor"></a>
``` clojure

(fixed-executor & {:keys [parallelism factory]})
```

A fixed thread executor pool constructor.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L431-L436">Source</a></sub></p>

## <a name="promesa.exec/fn->thread">`fn->thread`</a><a name="promesa.exec/fn->thread"></a>
``` clojure

(fn->thread f & {:keys [daemon start priority name], :or {daemon true, start true, priority Thread/NORM_PRIORITY}})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L714-L723">Source</a></sub></p>

## <a name="promesa.exec/forkjoin-executor">`forkjoin-executor`</a><a name="promesa.exec/forkjoin-executor"></a>
``` clojure

(forkjoin-executor
 &
 {:keys [factory async parallelism keepalive core-size max-size], :or {max-size 32767, async true, keepalive 60000}})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L546-L564">Source</a></sub></p>

## <a name="promesa.exec/forkjoin-thread-factory">`forkjoin-thread-factory`</a><a name="promesa.exec/forkjoin-thread-factory"></a>
``` clojure

(forkjoin-thread-factory & {:keys [name daemon], :or {name "promesa/forkjoin/%s", daemon true}})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L323-L333">Source</a></sub></p>

## <a name="promesa.exec/get-available-processors">`get-available-processors`</a><a name="promesa.exec/get-available-processors"></a>
``` clojure

(get-available-processors)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L89-L91">Source</a></sub></p>

## <a name="promesa.exec/get-name">`get-name`</a><a name="promesa.exec/get-name"></a>
``` clojure

(get-name)
(get-name thread)
```

Retrieve thread name
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L768-L772">Source</a></sub></p>

## <a name="promesa.exec/get-thread-id">`get-thread-id`</a><a name="promesa.exec/get-thread-id"></a>
``` clojure

(get-thread-id)
(get-thread-id thread)
```

Retrieves the thread ID.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L794-L799">Source</a></sub></p>

## <a name="promesa.exec/interrupt!">`interrupt!`</a><a name="promesa.exec/interrupt!"></a>
``` clojure

(interrupt!)
(interrupt! thread)
```

Interrupt a thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L811-L816">Source</a></sub></p>

## <a name="promesa.exec/interrupted?">`interrupted?`</a><a name="promesa.exec/interrupted?"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L775-L791">Source</a></sub></p>

## <a name="promesa.exec/pmap">`pmap`</a><a name="promesa.exec/pmap"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L667-L694">Source</a></sub></p>

## <a name="promesa.exec/run!">`run!`</a><a name="promesa.exec/run!"></a>
``` clojure

(run! f)
(run! executor f)
```

Run the task in the provided executor.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L250-L255">Source</a></sub></p>

## <a name="promesa.exec/schedule!">`schedule!`</a><a name="promesa.exec/schedule!"></a>
``` clojure

(schedule! ms f)
(schedule! scheduler ms f)
```

Schedule a callable to be executed after the `ms` delay
  is reached.

  In JVM it uses a scheduled executor service and in JS
  it uses the `setTimeout` function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L268-L277">Source</a></sub></p>

## <a name="promesa.exec/scheduled-executor">`scheduled-executor`</a><a name="promesa.exec/scheduled-executor"></a>
``` clojure

(scheduled-executor & {:keys [parallelism factory], :or {parallelism 1}})
```

A scheduled thread pool constructor. A ScheduledExecutor (IScheduler
  in CLJS) instance allows execute asynchronous tasks some time later.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L515-L529">Source</a></sub></p>

## <a name="promesa.exec/set-name!">`set-name!`</a><a name="promesa.exec/set-name!"></a>
``` clojure

(set-name! name)
(set-name! thread name)
```

Rename thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L762-L765">Source</a></sub></p>

## <a name="promesa.exec/shutdown!">`shutdown!`</a><a name="promesa.exec/shutdown!"></a>
``` clojure

(shutdown! executor)
```

Shutdowns the executor service.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L146-L149">Source</a></sub></p>

## <a name="promesa.exec/shutdown-now!">`shutdown-now!`</a><a name="promesa.exec/shutdown-now!"></a>
``` clojure

(shutdown-now! executor)
```

Shutdowns and interrupts the executor service.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L152-L155">Source</a></sub></p>

## <a name="promesa.exec/shutdown?">`shutdown?`</a><a name="promesa.exec/shutdown?"></a>
``` clojure

(shutdown? executor)
```

Check if execitor is in shutdown state.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L158-L161">Source</a></sub></p>

## <a name="promesa.exec/single-executor">`single-executor`</a><a name="promesa.exec/single-executor"></a>
``` clojure

(single-executor & {:keys [factory]})
```

A single thread executor pool constructor.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L439-L444">Source</a></sub></p>

## <a name="promesa.exec/sleep">`sleep`</a><a name="promesa.exec/sleep"></a>
``` clojure

(sleep ms)
```

Turn the current thread to sleep accept a number of milliseconds or
  Duration instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L825-L831">Source</a></sub></p>

## <a name="promesa.exec/submit!">`submit!`</a><a name="promesa.exec/submit!"></a>
``` clojure

(submit! f)
(submit! executor f)
```

Submit a task to be executed in a provided executor
  and return a promise that will be completed with
  the return value of a task.

  A task is a plain clojure function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L257-L266">Source</a></sub></p>

## <a name="promesa.exec/thread">`thread`</a><a name="promesa.exec/thread"></a>
``` clojure

(thread opts & body)
```
Function.

A low-level, not-pooled thread constructor, it accepts an optional
  map as first argument and the body. The options map is interepreted
  as options if a literal map is provided. The available options are:
  `:name`, `:priority`, `:daemon` and `:virtual`. The `:virtual`
  option is ignored if you are using a JVM that has no support for
  Virtual Threads.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L726-L739">Source</a></sub></p>

## <a name="promesa.exec/thread-call">`thread-call`</a><a name="promesa.exec/thread-call"></a>
``` clojure

(thread-call f & {:as opts})
```

Advanced version of `p/thread-call` that creates and starts a thread
  configured with `opts`. No executor service is used, this will start
  a plain unpooled thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L742-L753">Source</a></sub></p>

## <a name="promesa.exec/thread-factory">`thread-factory`</a><a name="promesa.exec/thread-factory"></a>
``` clojure

(thread-factory
 &
 {:keys [name daemon priority], :or {daemon true, priority Thread/NORM_PRIORITY, name "promesa/thread/%s"}})
```

Returns an instance of promesa default thread factory.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L308-L320">Source</a></sub></p>

## <a name="promesa.exec/thread-factory?">`thread-factory?`</a><a name="promesa.exec/thread-factory?"></a>
``` clojure

(thread-factory? o)
```

Checks if `o` is an instance of ThreadFactory
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L282-L285">Source</a></sub></p>

## <a name="promesa.exec/thread-id">`thread-id`</a><a name="promesa.exec/thread-id"></a>
``` clojure

(thread-id)
(thread-id thread)
```

Retrieves the thread ID.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L802-L808">Source</a></sub></p>

## <a name="promesa.exec/thread-per-task-executor">`thread-per-task-executor`</a><a name="promesa.exec/thread-per-task-executor"></a>
``` clojure

(thread-per-task-executor & {:keys [factory]})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L533-L537">Source</a></sub></p>

## <a name="promesa.exec/thread?">`thread?`</a><a name="promesa.exec/thread?"></a>
``` clojure

(thread? t)
```

Check if provided object is a thread instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L819-L822">Source</a></sub></p>

## <a name="promesa.exec/throw-uncaught!">`throw-uncaught!`</a><a name="promesa.exec/throw-uncaught!"></a>
``` clojure

(throw-uncaught! cause)
```

Throw an exception to the current uncaught exception handler.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L834-L841">Source</a></sub></p>

## <a name="promesa.exec/virtual-threads-available?">`virtual-threads-available?`</a><a name="promesa.exec/virtual-threads-available?"></a>




Var that indicates the availability of virtual threads.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L61-L70">Source</a></sub></p>

## <a name="promesa.exec/vthread-per-task-executor">`vthread-per-task-executor`</a><a name="promesa.exec/vthread-per-task-executor"></a>
``` clojure

(vthread-per-task-executor)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L541-L543">Source</a></sub></p>

## <a name="promesa.exec/vthread-supported?">`vthread-supported?`</a><a name="promesa.exec/vthread-supported?"></a>




backward compatibility alias for [`virtual-threads-available?`](#promesa.exec/virtual-threads-available?)
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L81-L84">Source</a></sub></p>

## <a name="promesa.exec/with-dispatch">`with-dispatch`</a><a name="promesa.exec/with-dispatch"></a>
``` clojure

(with-dispatch executor & body)
```
Function.

Helper macro for dispatch execution of the body to an executor
  service. The returned promise is not cancellable (the body will be
  executed independently of the cancellation).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L619-L625">Source</a></sub></p>

## <a name="promesa.exec/with-dispatch!">`with-dispatch!`</a><a name="promesa.exec/with-dispatch!"></a>
``` clojure

(with-dispatch! executor & body)
```
Function.

Blocking version of [`with-dispatch`](#promesa.exec/with-dispatch). Useful when you want to
  dispatch a blocking operation to a separated thread and join current
  thread waiting for result; effective when current thread is virtual
  thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L627-L642">Source</a></sub></p>

## <a name="promesa.exec/with-executor">`with-executor`</a><a name="promesa.exec/with-executor"></a>
``` clojure

(with-executor executor & body)
```
Function.

Binds the *default-executor* var with the provided executor,
  executes the macro body. It also can optionally shutdown or shutdown
  and interrupt on termination if you provide `^:shutdown` and
  `^:interrupt` metadata.

  **EXPERIMENTAL API:** This function should be considered
  EXPERIMENTAL and may be changed or removed in future versions until
  this notification is removed.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L644-L664">Source</a></sub></p>

## <a name="promesa.exec/work-stealing-executor">`work-stealing-executor`</a><a name="promesa.exec/work-stealing-executor"></a>
``` clojure

(work-stealing-executor & params)
```

An alias for the [`forkjoin-executor`](#promesa.exec/forkjoin-executor).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec.cljc#L567-L570">Source</a></sub></p>
