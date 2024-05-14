---
sidebar_label: csp
title: promesa.exec.csp
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

A core.async analogous implementation of channels that uses promises
  instead of callbacks for all operations and are intended to be used
  as-is (using blocking operations) in go-blocks backed by virtual
  threads.

  There are no macro transformations, go blocks are just alias for the
  [`promesa.core/vthread`](../../../promesa/core/#vthread) macro that launches an virtual thread.

  This code is based on the same ideas as core.async but the
  implementation is written from scratch, for make it more
  simplier (and smaller, because it does not intend to solve all the
  corner cases that core.async does right now).

  This code is implemented in CLJS for make available the channel
  abstraction to the CLJS, but the main use case for this ns is
  targeted to the JVM, where you will be able to take advantage of
  virtual threads and seamless blocking operations on channels.

  **EXPERIMENTAL API**




### \*executor\* {#-STAR-executor-STAR-}


A defalt executor for channel callback dispatching

*dynamic*


### &lt;\! {#-LT--BANG-}
``` clojure
(<! port)
(<! port timeout-duration)
(<! port timeout-duration timeout-value)
```


A convenience alias for [`take!`](#take-BANG-).

### &gt;\! {#-GT--BANG-}
``` clojure
(>! port val)
(>! port val timeout-duration)
(>! port val timeout-duration timeout-value)
```


A convenience alias for [`put!`](#put-BANG-).

### alts {#alts}
``` clojure
(alts ports & {:as opts})
```


Completes at most one of several operations on channel. Receives a
  vector of operations and optional keyword options.

  A channel operation is defined as a vector of 2 elements for take,
  and 3 elements for put. Unless the :priority option is true and if
  more than one channel operation is ready, a non-deterministic choice
  will be made.

  Returns a promise instance that will be resolved when a single
  operation is ready to a vector [val channel] where val is return
  value of the operation and channel identifies the channel where the
  the operation is succeeded.

### alts\! {#alts-BANG-}
``` clojure
(alts! ports & {:as opts})
```


A blocking variant of [`alts`](#alts).

### chan {#chan}
``` clojure
(chan & {:keys [buf xf exh exc], :or {exh channel/close-with-exception}})
```


Creates a new channel instance, it optionally accepts buffer,
  transducer and error handler. If buffer is an integer, it will be
  used as initial capacity for the expanding buffer.

### chan? {#chan-QMARK-}
``` clojure
(chan? o)
```


Returns true if `o` is instance of Channel or satisfies IChannel protocol.

### close\! {#close-BANG-}
``` clojure
(close! port)
(close! port cause)
```


Close the channel.

### close\-with\-exception {#close-with-exception}
``` clojure
(close-with-exception ch cause)
```


A channel exception handler that closes the channel with the cause
  if an exception is raised in the transducer.

### closed? {#closed-QMARK-}
``` clojure
(closed? port)
```


Returns true if channel is closed.

### dropping\-buffer {#dropping-buffer}
``` clojure
(dropping-buffer n)
```


Create a dropping buffer instance.

### expanding\-buffer {#expanding-buffer}
``` clojure
(expanding-buffer n)
```


Create a fixed size (but expanding) buffer instance.

  This buffer is used by default if you pass an integer as buffer on
  channel constructor.

### fixed\-buffer {#fixed-buffer}
``` clojure
(fixed-buffer n)
```


Create a fixed size buffer instance.

### go {#go}
``` clojure
(go & body)
```


Schedules the body to be executed asychronously, potentially using
  virtual thread if available (a normal thread will be used in other
  case, determined by *executor* dynamic var). Returns a promise
  instance that resolves with the return value when the asynchronous
  block finishes.

  Forwards dynamic bindings.

*macro*


### go\-chan {#go-chan}
``` clojure
(go-chan & body)
```


A convencience go macro version that returns a channel instead of a
  promise instance, has the same semantics as [`go`](#go) macro.

*macro*


### go\-loop {#go-loop}
``` clojure
(go-loop bindings & body)
```


A convencience helper macro that combines go + loop.

*macro*


### mult {#mult}
``` clojure
(mult & {:as opts})
```


Creates an instance of multiplexer.

  A multiplexer instance acts like a write-only channel what enables a
  broadcast-like (instead of a queue-like) behavior. Channels
  containing copies of this multiplexer can be attached using [`tap!`](#tap-BANG-)
  and dettached with [`untap!`](#untap-BANG-).

  Each item is forwarded to all attached channels in parallel and
  synchronously; use buffers to prevent slow taps from holding up the
  multiplexer.

  If there are no taps, all received items will be dropped. Closed
  channels will be automatically removed from multiplexer.

  Do not creates vthreads (or threads).

### mult\* {#mult-STAR-}
``` clojure
(mult* ch)
(mult* ch close?)
```


Create a multiplexer with an externally provided channel. From now,
  you can use the external channel or the multiplexer instace to put
  values in because multiplexer implements the IWriteChannel protocol.

  Optionally accepts `close?` argument, that determines if the channel will
  be closed when [`close!`](#close-BANG-) is called on multiplexer o not.

### offer\! {#offer-BANG-}
``` clojure
(offer! port val)
```


Puts a val into channel if it's possible to do so immediately.
  Returns a resolved promise with `true` if the operation
  succeeded. Never blocks.

### once\-buffer {#once-buffer}
``` clojure
(once-buffer)
```


Create a once buffer instance.

### onto\-chan\! {#onto-chan-BANG-}
``` clojure
(onto-chan! ch coll)
(onto-chan! ch coll close?)
```


Puts the contents of coll into the supplied channel.

  By default the channel will be closed after the items are copied,
  but can be determined by the close? parameter. Returns a promise
  that will be resolved with `nil` once the items are copied.

### pipe {#pipe}
``` clojure
(pipe from to)
(pipe from to close?)
```


Takes elements from the from channel and supplies them to the to
  channel. By default, the to channel will be closed when the from
  channel closes, but can be determined by the close?  parameter. Will
  stop consuming the from channel if the to channel closes.

### pipeline {#pipeline}
``` clojure
(pipeline & {:keys [typ in out f close? n exh], :or {typ :thread, close? true}})
```


Create a processing pipeline with the ability to specify the process
  function `proc-fn`, the type of concurrency primitive to
  use (`:thread` or `:vthread`) and the parallelism.

  The `proc-fn` should be a function that takes the value and the
  result channel; the result channel should be closed once the
  processing unit is finished.

  By default the output channel is closed when pipeline is terminated,
  but it can be specified by user using the `:close?` parameter.

  Returns a promise which will be resolved once the pipeline is
  terminated.

  Example:

    (def inp (sp/chan))
    (def out (sp/chan (map inc)))

    (sp/pipeline :typ :vthread
                 :close? true
                 :n 10
                 :in inp
                 :out out
                 :f proc-fn)

    (sp/go
      (loop []
        (when-let [val (sp/<! out)]
          (prn "RES:" val)
          (recur)))
      (prn "RES: END"))

    (p/await! (sp/onto-chan! inp ["1" "2" "3" "4"] true))

  Internally, uses 2 vthreads for pipeline internals processing.

  EXPERIMENTAL: API subject to be changed or removed in future
  releases.

### poll\! {#poll-BANG-}
``` clojure
(poll! port)
```


Takes a val from port if it's possible to do so
  immediatelly. Returns a resolved promise with the value if
  succeeded,  `nil` otherwise.

### put {#put}
``` clojure
(put port val)
(put port val timeout-duration)
(put port val timeout-duration timeout-value)
```


Schedules a put operation on the channel. Returns a promise
  instance that will resolve to: false if channel is closed, true if
  put is succeed. If channel has buffer, it will return immediatelly
  with resolved promise.

  Optionally accepts a timeout-duration and timeout-value. The
  `timeout-duration` can be a long or Duration instance measured in
  milliseconds.

### put\! {#put-BANG-}
``` clojure
(put! port val)
(put! port val timeout-duration)
(put! port val timeout-duration timeout-value)
```


A blocking version of [`put`](#put).

### sliding\-buffer {#sliding-buffer}
``` clojure
(sliding-buffer n)
```


Create a sliding buffer instance.

### take {#take}
``` clojure
(take port)
(take port timeout-duration)
(take port timeout-duration timeout-value)
```


Schedules a take operation on the channel. Returns a promise instance
  that will resolve to: nil if channel is closed, obj if value is
  found. If channel has non-empty buffer the take operation will
  succeed immediatelly with resolved promise.

  Optionally accepts a timeout-duration and timeout-value. The
  `timeout-duration` can be a long or Duration instance measured in
  milliseconds.

### take\! {#take-BANG-}
``` clojure
(take! port)
(take! port timeout-duration)
(take! port timeout-duration timeout-value)
```


Blocking version of [`take`](#take).

### tap\! {#tap-BANG-}
``` clojure
(tap! mult ch)
(tap! mult ch close?)
```


Copies the multiplexer source onto the provided channel.

### thread\-chan {#thread-chan}
``` clojure
(thread-chan & body)
```


A convencience thread macro version that returns a channel instead of
  a promise instance.

*macro*


### throw\-uncaught {#throw-uncaught}
``` clojure
(throw-uncaught ch cause)
```


A channel exception handler that throws the exception to the default
  uncaught exception handler.

### timeout {#timeout}
``` clojure
(timeout ms)
```


Returns a promise that will be resolved in the specified timeout. The
  default scheduler will be used.

### timeout\-chan {#timeout-chan}
``` clojure
(timeout-chan ms)
(timeout-chan scheduler ms)
```


Returns a channel that will be closed in the specified timeout. The
  default scheduler will be used. You can provide your own as optional
  first argument.

### untap\! {#untap-BANG-}
``` clojure
(untap! mult ch)
```


Disconnects a channel from the multiplexer.
