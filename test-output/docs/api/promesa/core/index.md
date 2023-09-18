
-----
# <a name="promesa.core">promesa.core</a>






## <a name="promesa.core/->">`->`</a><a name="promesa.core/->"></a>
``` clojure

(-> x & forms)
```
Function.

Like the clojure.core/->, but it will handle promises in values
  and make sure the next form gets the value realized instead of
  the promise.

  Example fetching data in the browser with CLJS:

  (p/-> (js/fetch #js {...}) ; returns a promise
        .-body)

  The result of a thread is a promise that will resolve to the
  end of the thread chain.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L686-L704">Source</a></sub></p>

## <a name="promesa.core/->>">`->>`</a><a name="promesa.core/->>"></a>
``` clojure

(->> x & forms)
```
Function.

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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L706-L726">Source</a></sub></p>

## <a name="promesa.core/all">`all`</a><a name="promesa.core/all"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L358-L376">Source</a></sub></p>

## <a name="promesa.core/any">`any`</a><a name="promesa.core/any"></a>
``` clojure

(any promises)
(any promises default)
```

Given an array of promises, return a promise that is fulfilled when
  first one item in the array is fulfilled.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L382-L421">Source</a></sub></p>

## <a name="promesa.core/as->">`as->`</a><a name="promesa.core/as->"></a>
``` clojure

(as-> expr name & forms)
```
Function.

Like clojure.core/as->, but it will handle promises in values
   and make sure the next form gets the value realized instead of
   the promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L728-L737">Source</a></sub></p>

## <a name="promesa.core/await">`await`</a><a name="promesa.core/await"></a>
``` clojure

(await resource)
(await resource duration)
```

A exception safer variant of [`await!`](#promesa.core/await!). Returns `nil` on timeout
  exception, forwards interrupted exception and all other exceptions
  are returned as value, so user is responsible for checking if the returned
  value is exception or not.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L797-L825">Source</a></sub></p>

## <a name="promesa.core/await!">`await!`</a><a name="promesa.core/await!"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L770-L794">Source</a></sub></p>

## <a name="promesa.core/bind">`bind`</a><a name="promesa.core/bind"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L162-L173">Source</a></sub></p>

## <a name="promesa.core/cancel!">`cancel!`</a><a name="promesa.core/cancel!"></a>
``` clojure

(cancel! p)
```

Cancel the promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L476-L480">Source</a></sub></p>

## <a name="promesa.core/cancelled?">`cancelled?`</a><a name="promesa.core/cancelled?"></a>
``` clojure

(cancelled? v)
```

Return true if `v` is a cancelled promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L482-L485">Source</a></sub></p>

## <a name="promesa.core/catch">`catch`</a><a name="promesa.core/catch"></a>
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
  [`merr`](#promesa.core/merr) if you want the ability to schedule the computation to other
  thread.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L314-L335">Source</a></sub></p>

## <a name="promesa.core/chain">`chain`</a><a name="promesa.core/chain"></a>
``` clojure

(chain p f)
(chain p f & fs)
```

Chain variable number of functions to be executed serially using
  [`then`](#promesa.core/then).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L218-L222">Source</a></sub></p>

## <a name="promesa.core/chain'">`chain'`</a><a name="promesa.core/chain'"></a>
``` clojure

(chain' p f)
(chain' p f & fs)
```

Chain variable number of functions to be executed serially using
  [`map`](#promesa.core/map).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L224-L228">Source</a></sub></p>

## <a name="promesa.core/create">`create`</a><a name="promesa.core/create"></a>
``` clojure

(create f)
(create f executor)
```

Create a promise instance from a factory function. If an executor is
  provided, the factory will be executed in the provided executor.

  A factory function looks like `(fn [resolve reject] (resolve 1))`.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L64-L85">Source</a></sub></p>

## <a name="promesa.core/deferred">`deferred`</a><a name="promesa.core/deferred"></a>
``` clojure

(deferred)
```

Creates an empty promise instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L42-L45">Source</a></sub></p>

## <a name="promesa.core/deferred?">`deferred?`</a><a name="promesa.core/deferred?"></a>
``` clojure

(deferred? v)
```

Return true if `v` is a deferred instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L92-L95">Source</a></sub></p>

## <a name="promesa.core/delay">`delay`</a><a name="promesa.core/delay"></a>
``` clojure

(delay t)
(delay t v)
(delay t v scheduler)
```

Given a timeout in miliseconds and optional value, returns a promise
  that will be fulfilled with provided value (or nil) after the time is
  reached.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L538-L547">Source</a></sub></p>

## <a name="promesa.core/do">`do`</a><a name="promesa.core/do"></a>
``` clojure

(do & exprs)
```
Function.

Execute potentially side effectful code and return a promise resolved
  to the last expression after awaiting the result of each
  expression.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L561-L569">Source</a></sub></p>

## <a name="promesa.core/do!">`do!`</a><a name="promesa.core/do!"></a>
``` clojure

(do! & exprs)
```
Function.

A convenience alias for [`do`](#promesa.core/do) macro.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L571-L574">Source</a></sub></p>

## <a name="promesa.core/do*">`do*`</a><a name="promesa.core/do*"></a>
``` clojure

(do* & exprs)
```
Function.

An exception unsafe do-like macro. Supposes that we are already
  wrapped in promise context so avoids defensive wrapping.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L549-L559">Source</a></sub></p>

## <a name="promesa.core/done?">`done?`</a><a name="promesa.core/done?"></a>
``` clojure

(done? p)
```

Returns true if promise `p` is already done.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L127-L130">Source</a></sub></p>

## <a name="promesa.core/doseq">`doseq`</a><a name="promesa.core/doseq"></a>
``` clojure

(doseq [binding xs] & body)
```
Function.

Simplified version of [`doseq`](#promesa.core/doseq) which takes one binding and a seq, and
  runs over it using [`promesa.core/run!`](#promesa.core/run!)
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L761-L767">Source</a></sub></p>

## <a name="promesa.core/error">`error`</a><a name="promesa.core/error"></a>
``` clojure

(error f p)
(error f type p)
```

Same as [`catch`](#promesa.core/catch) but with parameters inverted.

  DEPRECATED
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L350-L356">Source</a></sub></p>

## <a name="promesa.core/extract">`extract`</a><a name="promesa.core/extract"></a>
``` clojure

(extract p)
(extract p default)
```

Returns the current promise value.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L120-L125">Source</a></sub></p>

## <a name="promesa.core/finally">`finally`</a><a name="promesa.core/finally"></a>
``` clojure

(finally p f)
(finally p f executor)
```

Like [`handle`](#promesa.core/handle) but ignores the return value. Returns a promise that
  will mirror the original one.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L256-L264">Source</a></sub></p>

## <a name="promesa.core/fmap">`fmap`</a><a name="promesa.core/fmap"></a>
``` clojure

(fmap f p)
(fmap executor f p)
```

A convenience alias for [`map`](#promesa.core/map).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L189-L194">Source</a></sub></p>

## <a name="promesa.core/fnly">`fnly`</a><a name="promesa.core/fnly"></a>
``` clojure

(fnly f p)
(fnly executor f p)
```

Inverted arguments version of [`finally`](#promesa.core/finally); intended to be used with
  [`->>`](#promesa.core/->>).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L306-L312">Source</a></sub></p>

## <a name="promesa.core/future">`future`</a><a name="promesa.core/future"></a>
``` clojure

(future & body)
```
Function.

Analogous macro to `clojure.core/future` that returns promise
  instance instead of the `Future`. Exposed just for convenience and
  works as an alias to [`thread`](#promesa.core/thread).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L634-L639">Source</a></sub></p>

## <a name="promesa.core/handle">`handle`</a><a name="promesa.core/handle"></a>
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

  For performance sensitive code, look at [`hmap`](#promesa.core/hmap) and [`hcat`](#promesa.core/hcat).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L230-L254">Source</a></sub></p>

## <a name="promesa.core/hcat">`hcat`</a><a name="promesa.core/hcat"></a>
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

  Intended to be used with [`->>`](#promesa.core/->>).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L281-L304">Source</a></sub></p>

## <a name="promesa.core/hmap">`hmap`</a><a name="promesa.core/hmap"></a>
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

  Intended to be used with [`->>`](#promesa.core/->>).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L266-L279">Source</a></sub></p>

## <a name="promesa.core/let">`let`</a><a name="promesa.core/let"></a>
``` clojure

(let bindings & body)
```
Function.

A [`let`](#promesa.core/let) alternative that always returns promise and waits for all the
  promises on the bindings.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L586-L594">Source</a></sub></p>

## <a name="promesa.core/let*">`let*`</a><a name="promesa.core/let*"></a>
``` clojure

(let* bindings & body)
```
Function.

An exception unsafe let-like macro. Supposes that we are already
  wrapped in promise context so avoids defensive wrapping.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L576-L584">Source</a></sub></p>

## <a name="promesa.core/loop">`loop`</a><a name="promesa.core/loop"></a>
``` clojure

(loop bindings & body)
```
Function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L646-L680">Source</a></sub></p>

## <a name="promesa.core/map">`map`</a><a name="promesa.core/map"></a>
``` clojure

(map f p)
(map executor f p)
```

Chains a function `f` to be executed when the promise `p` is
  successfully resolved. Returns a promise that will be resolved with
  the return value of calling `f` with value as single argument.

  The computation will be executed in the completion thread by
  default; you also can provide a custom executor.

  This function is intended to be used with [`->>`](#promesa.core/->>).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L175-L187">Source</a></sub></p>

## <a name="promesa.core/mapcat">`mapcat`</a><a name="promesa.core/mapcat"></a>
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

  This funciton is intended to be used with [`->>`](#promesa.core/->>).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L196-L209">Source</a></sub></p>

## <a name="promesa.core/mcat">`mcat`</a><a name="promesa.core/mcat"></a>
``` clojure

(mcat f p)
(mcat executor f p)
```

A convenience alias for [`mapcat`](#promesa.core/mapcat).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L211-L216">Source</a></sub></p>

## <a name="promesa.core/merr">`merr`</a><a name="promesa.core/merr"></a>
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

  This is intended to be used with [`->>`](#promesa.core/->>).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L337-L348">Source</a></sub></p>

## <a name="promesa.core/pending?">`pending?`</a><a name="promesa.core/pending?"></a>
``` clojure

(pending? p)
```

Returns true if promise `p` is stil pending.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L115-L118">Source</a></sub></p>

## <a name="promesa.core/plet">`plet`</a><a name="promesa.core/plet"></a>
``` clojure

(plet bindings & body)
```
Function.

A parallel let; executes all the bindings in parallel and when all
  bindings are resolved, executes the body.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L596-L607">Source</a></sub></p>

## <a name="promesa.core/promise">`promise`</a><a name="promesa.core/promise"></a>
``` clojure

(promise v)
(promise v executor)
```

The coerce based promise constructor. Creates an appropriate promise
  instance depending on the provided value.

  If an executor is provided, it will be used to resolve this
  promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L47-L56">Source</a></sub></p>

## <a name="promesa.core/promise?">`promise?`</a><a name="promesa.core/promise?"></a>
``` clojure

(promise? v)
```

Return true if `v` is a promise instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L87-L90">Source</a></sub></p>

## <a name="promesa.core/promisify">`promisify`</a><a name="promesa.core/promisify"></a>
``` clojure

(promisify callable)
```

Given a function that accepts a callback as the last argument, return a
  function that returns a promise. Callback is expected to take one
  parameter (result of a computation).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L501-L512">Source</a></sub></p>

## <a name="promesa.core/race">`race`</a><a name="promesa.core/race"></a>
``` clojure

(race promises)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L378-L380">Source</a></sub></p>

## <a name="promesa.core/recur">`recur`</a><a name="promesa.core/recur"></a>
``` clojure

(recur & args)
```
Function.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L682-L684">Source</a></sub></p>

## <a name="promesa.core/recur?">`recur?`</a><a name="promesa.core/recur?"></a>
``` clojure

(recur? o)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L642-L644">Source</a></sub></p>

## <a name="promesa.core/reject!">`reject!`</a><a name="promesa.core/reject!"></a>
``` clojure

(reject! p e)
```

Reject a completable promise with an error.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L494-L497">Source</a></sub></p>

## <a name="promesa.core/rejected">`rejected`</a><a name="promesa.core/rejected"></a>
``` clojure

(rejected v)
```

Return a rejected promise with provided reason.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L37-L40">Source</a></sub></p>

## <a name="promesa.core/rejected?">`rejected?`</a><a name="promesa.core/rejected?"></a>
``` clojure

(rejected? p)
```

Returns true if promise `p` is already rejected.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L110-L113">Source</a></sub></p>

## <a name="promesa.core/resolve!">`resolve!`</a><a name="promesa.core/resolve!"></a>
``` clojure

(resolve! o)
(resolve! o v)
```

Resolve a completable promise with a value.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L489-L492">Source</a></sub></p>

## <a name="promesa.core/resolved">`resolved`</a><a name="promesa.core/resolved"></a>
``` clojure

(resolved v)
```

Return a resolved promise with provided value.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L32-L35">Source</a></sub></p>

## <a name="promesa.core/resolved?">`resolved?`</a><a name="promesa.core/resolved?"></a>
``` clojure

(resolved? p)
```

Returns true if promise `p` is already fulfilled.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L105-L108">Source</a></sub></p>

## <a name="promesa.core/run!">`run!`</a><a name="promesa.core/run!"></a>
``` clojure

(run! f coll)
(run! f coll executor)
```

A promise aware run! function. Executed in terms of [`then`](#promesa.core/then) rules.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L465-L472">Source</a></sub></p>

## <a name="promesa.core/then">`then`</a><a name="promesa.core/then"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L134-L146">Source</a></sub></p>

## <a name="promesa.core/then'">`then'`</a><a name="promesa.core/then'"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L148-L160">Source</a></sub></p>

## <a name="promesa.core/thread">`thread`</a><a name="promesa.core/thread"></a>
``` clojure

(thread & body)
```
Function.

Analogous to `clojure.core.async/thread` that returns a promise instance
  instead of the `Future`.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L621-L625">Source</a></sub></p>

## <a name="promesa.core/thread-call">`thread-call`</a><a name="promesa.core/thread-call"></a>
``` clojure

(thread-call f)
(thread-call executor f)
```

Analogous to `clojure.core.async/thread` that returns a promise
  instance instead of the `Future`. Useful for executing synchronous
  code in a separate thread (also works in cljs).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L609-L614">Source</a></sub></p>

## <a name="promesa.core/timeout">`timeout`</a><a name="promesa.core/timeout"></a>
``` clojure

(timeout p t)
(timeout p t v)
(timeout p t v scheduler)
```

Returns a cancellable promise that will be fulfilled with this
  promise's fulfillment value or rejection reason.  However, if this
  promise is not fulfilled or rejected within `ms` milliseconds, the
  returned promise is cancelled with a TimeoutError.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L524-L536">Source</a></sub></p>

## <a name="promesa.core/vthread">`vthread`</a><a name="promesa.core/vthread"></a>
``` clojure

(vthread & body)
```
Function.

Analogous to `clojure.core.async/thread` that returns a promise instance
  instead of the `Future`. Useful for executing synchronous code in a
  separate thread (also works in cljs).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L627-L632">Source</a></sub></p>

## <a name="promesa.core/vthread-call">`vthread-call`</a><a name="promesa.core/vthread-call"></a>
``` clojure

(vthread-call f)
```

A shortcut for `(p/thread-call :vthread f)`.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L616-L619">Source</a></sub></p>

## <a name="promesa.core/wait-all">`wait-all`</a><a name="promesa.core/wait-all"></a>
``` clojure

(wait-all & promises)
```

Given a variable number of promises, returns a promise which resolves
  to `nil` when all provided promises complete (rejected or resolved).

  **EXPERIMENTAL**
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L451-L457">Source</a></sub></p>

## <a name="promesa.core/wait-all!">`wait-all!`</a><a name="promesa.core/wait-all!"></a>
``` clojure

(wait-all! promises)
```

A blocking version of [`wait-all`](#promesa.core/wait-all).
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L460-L463">Source</a></sub></p>

## <a name="promesa.core/wait-all*">`wait-all*`</a><a name="promesa.core/wait-all*"></a>
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
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L423-L449">Source</a></sub></p>

## <a name="promesa.core/with-redefs">`with-redefs`</a><a name="promesa.core/with-redefs"></a>
``` clojure

(with-redefs bindings & body)
```
Function.

Like clojure.core/with-redefs, but it will handle promises in
   body and wait until they resolve or reject before restoring the
   bindings. Useful for mocking async APIs.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L739-L759">Source</a></sub></p>

## <a name="promesa.core/wrap">`wrap`</a><a name="promesa.core/wrap"></a>
``` clojure

(wrap v)
```

A convenience alias for [`promise`](#promesa.core/promise) coercion function that only accepts
  a single argument.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/core.cljc#L58-L62">Source</a></sub></p>
