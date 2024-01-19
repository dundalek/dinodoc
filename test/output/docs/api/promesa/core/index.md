---
sidebar_label: core
title: promesa.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### \-&gt; {#--GT-}
``` clojure
(-> x & forms)
```


Like the clojure.core/->, but it will handle promises in values
  and make sure the next form gets the value realized instead of
  the promise.

  Example fetching data in the browser with CLJS:

  (p/-> (js/fetch #js {...}) ; returns a promise
        .-body)

  The result of a thread is a promise that will resolve to the
  end of the thread chain.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L686-L704">Source</a></sub></p>

### \-&gt;&gt; {#--GT--GT-}
``` clojure
(->> x & forms)
```


Like the clojure.core/->>, but it will handle promises in values
  and make sure the next form gets the value realized instead of
  the promise.

  Example fetching data in the browser with CLJS:

  (p/->> (js/fetch #js {...}) ; returns a promise
         .-body
         read-string
         (mapv inc)

  The result of a thread is a promise that will resolve to the
  end of the thread chain.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L706-L726">Source</a></sub></p>

### all {#all}
``` clojure
(all promises)
```


Given an array of promises, return a promise that is fulfilled when
  all the items in the array are fulfilled.

  Example:

  ```
  (-> (p/all [(promise :first-promise)
              (promise :second-promise)])
      (then (fn [[first-result second-result]])
              (println (str first-result ", " second-result))))
  ```

  Will print to out `:first-promise, :second-promise`.

  If at least one of the promises is rejected, the resulting promise
  will be rejected.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L358-L376">Source</a></sub></p>

### any {#any}
``` clojure
(any promises)
(any promises default)
```


Given an array of promises, return a promise that is fulfilled when
  first one item in the array is fulfilled.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L382-L421">Source</a></sub></p>

### as\-&gt; {#as--GT-}
``` clojure
(as-> expr name & forms)
```


Like clojure.core/as->, but it will handle promises in values
   and make sure the next form gets the value realized instead of
   the promise.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L728-L737">Source</a></sub></p>

### await {#await}
``` clojure
(await resource)
(await resource duration)
```


A exception safer variant of [`await!`](#await-BANG-). Returns `nil` on timeout
  exception, forwards interrupted exception and all other exceptions
  are returned as value, so user is responsible for checking if the returned
  value is exception or not.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L797-L825">Source</a></sub></p>

### await\! {#await-BANG-}
``` clojure
(await! resource)
(await! resource duration)
```


Generic await operation. Block current thread until some operation
  terminates. Returns `nil` on timeout; does not catch any other
  exception.

  Default implementation for Thread, CompletableFuture and
  CountDownLatch.

  The return value is implementation specific.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L770-L794">Source</a></sub></p>

### bind {#bind}
``` clojure
(bind p f)
(bind p f executor)
```


Chains a function `f` to be executed with when the promise `p` is
  successfully resolved. Returns a promise that will mirror the
  promise instance returned by calling `f` with the value as single
  argument; `f` **must** return a promise instance.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L162-L173">Source</a></sub></p>

### cancel\! {#cancel-BANG-}
``` clojure
(cancel! p)
```


Cancel the promise.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L476-L480">Source</a></sub></p>

### cancelled? {#cancelled-QMARK-}
``` clojure
(cancelled? v)
```


Return true if `v` is a cancelled promise.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L482-L485">Source</a></sub></p>

### catch {#catch}
``` clojure
(catch p f)
(catch p pred-or-type f)
```


Chains a function `f` to be executed when the promise `p` is
  rejected. Returns a promise that will be resolved with the return
  value of calling `f` with exception as single argument; `f` can
  return a plain value or promise instance, an automatic unwrapping
  will be performed.

  The computation will be executed in the completion thread, look at
  [`merr`](#merr) if you want the ability to schedule the computation to other
  thread.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L314-L335">Source</a></sub></p>

### chain {#chain}
``` clojure
(chain p f)
(chain p f & fs)
```


Chain variable number of functions to be executed serially using
  [`then`](#then).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L218-L222">Source</a></sub></p>

### chain' {#chain-SINGLEQUOTE-}
``` clojure
(chain' p f)
(chain' p f & fs)
```


Chain variable number of functions to be executed serially using
  [`map`](#map).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L224-L228">Source</a></sub></p>

### create {#create}
``` clojure
(create f)
(create f executor)
```


Create a promise instance from a factory function. If an executor is
  provided, the factory will be executed in the provided executor.

  A factory function looks like `(fn [resolve reject] (resolve 1))`.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L64-L85">Source</a></sub></p>

### deferred {#deferred}
``` clojure
(deferred)
```


Creates an empty promise instance.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L42-L45">Source</a></sub></p>

### deferred? {#deferred-QMARK-}
``` clojure
(deferred? v)
```


Return true if `v` is a deferred instance.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L92-L95">Source</a></sub></p>

### delay {#delay}
``` clojure
(delay t)
(delay t v)
(delay t v scheduler)
```


Given a timeout in miliseconds and optional value, returns a promise
  that will be fulfilled with provided value (or nil) after the time is
  reached.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L538-L547">Source</a></sub></p>

### do {#do}
``` clojure
(do & exprs)
```


Execute potentially side effectful code and return a promise resolved
  to the last expression after awaiting the result of each
  expression.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L561-L569">Source</a></sub></p>

### do\! {#do-BANG-}
``` clojure
(do! & exprs)
```


A convenience alias for [`do`](#do) macro.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L571-L574">Source</a></sub></p>

### do\* {#do-STAR-}
``` clojure
(do* & exprs)
```


An exception unsafe do-like macro. Supposes that we are already
  wrapped in promise context so avoids defensive wrapping.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L549-L559">Source</a></sub></p>

### done? {#done-QMARK-}
``` clojure
(done? p)
```


Returns true if promise `p` is already done.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L127-L130">Source</a></sub></p>

### doseq {#doseq}
``` clojure
(doseq [binding xs] & body)
```


Simplified version of [`doseq`](#doseq) which takes one binding and a seq, and
  runs over it using [`promesa.core/run!`](../../promesa/core/#run-BANG-)

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L761-L767">Source</a></sub></p>

### error {#error}
``` clojure
(error f p)
(error f type p)
```


Same as [`catch`](#catch) but with parameters inverted.

  DEPRECATED
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L350-L356">Source</a></sub></p>

### extract {#extract}
``` clojure
(extract p)
(extract p default)
```


Returns the current promise value.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L120-L125">Source</a></sub></p>

### finally {#finally}
``` clojure
(finally p f)
(finally p f executor)
```


Like [`handle`](#handle) but ignores the return value. Returns a promise that
  will mirror the original one.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L256-L264">Source</a></sub></p>

### fmap {#fmap}
``` clojure
(fmap f p)
(fmap executor f p)
```


A convenience alias for [`map`](#map).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L189-L194">Source</a></sub></p>

### fnly {#fnly}
``` clojure
(fnly f p)
(fnly executor f p)
```


Inverted arguments version of [`finally`](#finally); intended to be used with
  [`->>`](#--GT--GT-).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L306-L312">Source</a></sub></p>

### future {#future}
``` clojure
(future & body)
```


Analogous macro to `clojure.core/future` that returns promise
  instance instead of the `Future`. Exposed just for convenience and
  works as an alias to [`thread`](#thread).

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L634-L639">Source</a></sub></p>

### handle {#handle}
``` clojure
(handle p f)
(handle p f executor)
```


Chains a function `f` to be executed when the promise `p` is completed
  (resolved or rejected) and returns a promise completed (resolving or
  rejecting) with the return value of calling `f` with both: value and
  the exception; `f` can return a new plain value or promise instance,
  and automatic unwrapping will be performed.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  For performance sensitive code, look at [`hmap`](#hmap) and [`hcat`](#hcat).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L230-L254">Source</a></sub></p>

### hcat {#hcat}
``` clojure
(hcat f p)
(hcat executor f p)
```


Chains a function `f` to be executed when the promise `p` is completed
  (resolved or rejected) and returns a promise that will mirror the
  promise instance returned by calling `f` with both: value and the
  exception. The `f` function must return a promise instance.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  Intended to be used with [`->>`](#--GT--GT-).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L281-L304">Source</a></sub></p>

### hmap {#hmap}
``` clojure
(hmap f p)
(hmap executor f p)
```


Chains a function `f` to be executed when the promise `p` is completed
  (resolved or rejected) and returns a promise completed (resolving or
  rejecting) with the return value of calling `f` with both: value and
  the exception.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  Intended to be used with [`->>`](#--GT--GT-).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L266-L279">Source</a></sub></p>

### let {#let}
``` clojure
(let bindings & body)
```


A [`let`](#let) alternative that always returns promise and waits for all the
  promises on the bindings.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L586-L594">Source</a></sub></p>

### let\* {#let-STAR-}
``` clojure
(let* bindings & body)
```


An exception unsafe let-like macro. Supposes that we are already
  wrapped in promise context so avoids defensive wrapping.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L576-L584">Source</a></sub></p>

### loop {#loop}
``` clojure
(loop bindings & body)
```


*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L646-L680">Source</a></sub></p>

### map {#map}
``` clojure
(map f p)
(map executor f p)
```


Chains a function `f` to be executed when the promise `p` is
  successfully resolved. Returns a promise that will be resolved with
  the return value of calling `f` with value as single argument.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  This function is intended to be used with [`->>`](#--GT--GT-).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L175-L187">Source</a></sub></p>

### mapcat {#mapcat}
``` clojure
(mapcat f p)
(mapcat executor f p)
```


Chains a function `f` to be executed when the promise `p` is
  successfully resolved. Returns a promise that will mirror the
  promise instance returned by calling `f` with the value as single
  argument; `f` **must** return a promise instance.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  This funciton is intended to be used with [`->>`](#--GT--GT-).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L196-L209">Source</a></sub></p>

### mcat {#mcat}
``` clojure
(mcat f p)
(mcat executor f p)
```


A convenience alias for [`mapcat`](#mapcat).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L211-L216">Source</a></sub></p>

### merr {#merr}
``` clojure
(merr f p)
(merr executor f p)
```


Chains a function `f` to be executed when the promise `p` is
  rejected. Returns a promise that will mirror the promise returned by
  calling `f` with exception as single argument; `f` **must** return a
  promise instance or throw an exception.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  This is intended to be used with [`->>`](#--GT--GT-).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L337-L348">Source</a></sub></p>

### pending? {#pending-QMARK-}
``` clojure
(pending? p)
```


Returns true if promise `p` is stil pending.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L115-L118">Source</a></sub></p>

### plet {#plet}
``` clojure
(plet bindings & body)
```


A parallel let; executes all the bindings in parallel and when all
  bindings are resolved, executes the body.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L596-L607">Source</a></sub></p>

### promise {#promise}
``` clojure
(promise v)
(promise v executor)
```


The coerce based promise constructor. Creates an appropriate promise
  instance depending on the provided value.

  If an executor is provided, it will be used to resolve this
  promise.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L47-L56">Source</a></sub></p>

### promise? {#promise-QMARK-}
``` clojure
(promise? v)
```


Return true if `v` is a promise instance.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L87-L90">Source</a></sub></p>

### promisify {#promisify}
``` clojure
(promisify callable)
```


Given a function that accepts a callback as the last argument, return a
  function that returns a promise. Callback is expected to take one
  parameter (result of a computation).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L501-L512">Source</a></sub></p>

### race {#race}
``` clojure
(race promises)
```

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L378-L380">Source</a></sub></p>

### recur {#recur}
``` clojure
(recur & args)
```


*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L682-L684">Source</a></sub></p>

### recur? {#recur-QMARK-}
``` clojure
(recur? o)
```

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L642-L644">Source</a></sub></p>

### reject\! {#reject-BANG-}
``` clojure
(reject! p e)
```


Reject a completable promise with an error.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L494-L497">Source</a></sub></p>

### rejected {#rejected}
``` clojure
(rejected v)
```


Return a rejected promise with provided reason.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L37-L40">Source</a></sub></p>

### rejected? {#rejected-QMARK-}
``` clojure
(rejected? p)
```


Returns true if promise `p` is already rejected.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L110-L113">Source</a></sub></p>

### resolve\! {#resolve-BANG-}
``` clojure
(resolve! o)
(resolve! o v)
```


Resolve a completable promise with a value.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L489-L492">Source</a></sub></p>

### resolved {#resolved}
``` clojure
(resolved v)
```


Return a resolved promise with provided value.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L32-L35">Source</a></sub></p>

### resolved? {#resolved-QMARK-}
``` clojure
(resolved? p)
```


Returns true if promise `p` is already fulfilled.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L105-L108">Source</a></sub></p>

### run\! {#run-BANG-}
``` clojure
(run! f coll)
(run! f coll executor)
```


A promise aware run! function. Executed in terms of [`then`](#then) rules.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L465-L472">Source</a></sub></p>

### then {#then}
``` clojure
(then p f)
(then p f executor)
```


Chains a function `f` to be executed when the promise `p` is
  successfully resolved. Returns a promise that will be resolved with
  the return value of calling `f` with value as single argument; `f`
  can return a plain value or promise instance, an automatic
  unwrapping will be performed.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L134-L146">Source</a></sub></p>

### then' {#then-SINGLEQUOTE-}
``` clojure
(then' p f)
(then' p f executor)
```


Chains a function `f` to be executed when the promise `p` is
  successfully resolved. Returns a promise that will be resolved with
  the return value of calling `f` with value as single argument; `f`
  should return a plain value, no automatic unwrapping will be
  performed.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L148-L160">Source</a></sub></p>

### thread {#thread}
``` clojure
(thread & body)
```


Analogous to `clojure.core.async/thread` that returns a promise instance
  instead of the `Future`.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L621-L625">Source</a></sub></p>

### thread\-call {#thread-call}
``` clojure
(thread-call f)
(thread-call executor f)
```


Analogous to `clojure.core.async/thread` that returns a promise
  instance instead of the `Future`. Useful for executing synchronous
  code in a separate thread (also works in cljs).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L609-L614">Source</a></sub></p>

### timeout {#timeout}
``` clojure
(timeout p t)
(timeout p t v)
(timeout p t v scheduler)
```


Returns a cancellable promise that will be fulfilled with this
  promise's fulfillment value or rejection reason.  However, if this
  promise is not fulfilled or rejected within `ms` milliseconds, the
  returned promise is cancelled with a TimeoutError.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L524-L536">Source</a></sub></p>

### vthread {#vthread}
``` clojure
(vthread & body)
```


Analogous to `clojure.core.async/thread` that returns a promise instance
  instead of the `Future`. Useful for executing synchronous code in a
  separate thread (also works in cljs).

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L627-L632">Source</a></sub></p>

### vthread\-call {#vthread-call}
``` clojure
(vthread-call f)
```


A shortcut for `(p/thread-call :vthread f)`.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L616-L619">Source</a></sub></p>

### wait\-all {#wait-all}
``` clojure
(wait-all & promises)
```


Given a variable number of promises, returns a promise which resolves
  to `nil` when all provided promises complete (rejected or resolved).

  **EXPERIMENTAL**
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L451-L457">Source</a></sub></p>

### wait\-all\! {#wait-all-BANG-}
``` clojure
(wait-all! promises)
```


A blocking version of [`wait-all`](#wait-all).
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L460-L463">Source</a></sub></p>

### wait\-all\* {#wait-all-STAR-}
``` clojure
(wait-all* promises)
```


Given an array of promises, return a promise that is fulfilled when
  all the items in the array are resolved (independently if
  successfully or exceptionally).

  Example:

  ```
  (->> (p/wait-all* [(promise :first-promise)
                     (promise :second-promise)])
       (p/fmap (fn [_]
                 (println "done"))))
  ```

  Rejected promises also counts as resolved.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L423-L449">Source</a></sub></p>

### with\-redefs {#with-redefs}
``` clojure
(with-redefs bindings & body)
```


Like clojure.core/with-redefs, but it will handle promises in
   body and wait until they resolve or reject before restoring the
   bindings. Useful for mocking async APIs.

*macro*

<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L739-L759">Source</a></sub></p>

### wrap {#wrap}
``` clojure
(wrap v)
```


A convenience alias for [`promise`](#promise) coercion function that only accepts
  a single argument.
<p><sub><a href="/blob/master/test/projects/promesa/src/promesa/core.cljc#L58-L62">Source</a></sub></p>
