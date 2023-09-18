
-----
# <a name="promesa.protocols">promesa.protocols</a>


A generic promise abstraction and related protocols.




## <a name="promesa.protocols/-acquire!">`-acquire!`</a><a name="promesa.protocols/-acquire!"></a>
``` clojure

(-acquire! it)
(-acquire! it n)
```

Acquire 1 or N permits
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L64-L64">Source</a></sub></p>

## <a name="promesa.protocols/-active?">`-active?`</a><a name="promesa.protocols/-active?"></a>
``` clojure

(-active? it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L96-L96">Source</a></sub></p>

## <a name="promesa.protocols/-await!">`-await!`</a><a name="promesa.protocols/-await!"></a>
``` clojure

(-await! it)
(-await! it duration)
```

block current thread await termination
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L102-L102">Source</a></sub></p>

## <a name="promesa.protocols/-blockable?">`-blockable?`</a><a name="promesa.protocols/-blockable?"></a>
``` clojure

(-blockable? it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L98-L98">Source</a></sub></p>

## <a name="promesa.protocols/-cancel!">`-cancel!`</a><a name="promesa.protocols/-cancel!"></a>
``` clojure

(-cancel! it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L45-L45">Source</a></sub></p>

## <a name="promesa.protocols/-cancelled?">`-cancelled?`</a><a name="promesa.protocols/-cancelled?"></a>
``` clojure

(-cancelled? it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L46-L46">Source</a></sub></p>

## <a name="promesa.protocols/-close!">`-close!`</a><a name="promesa.protocols/-close!"></a>
``` clojure

(-close! it)
(-close! it reason)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L87-L87">Source</a></sub></p>

## <a name="promesa.protocols/-closed?">`-closed?`</a><a name="promesa.protocols/-closed?"></a>
``` clojure

(-closed? it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L86-L86">Source</a></sub></p>

## <a name="promesa.protocols/-commit!">`-commit!`</a><a name="promesa.protocols/-commit!"></a>
``` clojure

(-commit! it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L97-L97">Source</a></sub></p>

## <a name="promesa.protocols/-exec!">`-exec!`</a><a name="promesa.protocols/-exec!"></a>
``` clojure

(-exec! it task)
```

Submit a task and return nil
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L53-L53">Source</a></sub></p>

## <a name="promesa.protocols/-extract">`-extract`</a><a name="promesa.protocols/-extract"></a>
``` clojure

(-extract it)
(-extract it default)
```

Extract the current value.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L34-L34">Source</a></sub></p>

## <a name="promesa.protocols/-fmap">`-fmap`</a><a name="promesa.protocols/-fmap"></a>
``` clojure

(-fmap it f)
(-fmap it f executor)
```

Apply function to a computation
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L11-L12">Source</a></sub></p>

## <a name="promesa.protocols/-fnly">`-fnly`</a><a name="promesa.protocols/-fnly"></a>
``` clojure

(-fnly it f)
(-fnly it f executor)
```

Apply function to a computation independently if is failed or
    successful; the return value is ignored.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L24-L26">Source</a></sub></p>

## <a name="promesa.protocols/-full?">`-full?`</a><a name="promesa.protocols/-full?"></a>
``` clojure

(-full? it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L90-L90">Source</a></sub></p>

## <a name="promesa.protocols/-hmap">`-hmap`</a><a name="promesa.protocols/-hmap"></a>
``` clojure

(-hmap it f)
(-hmap it f executor)
```

Apply function to a computation independently if is failed or
    successful.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L20-L22">Source</a></sub></p>

## <a name="promesa.protocols/-lock!">`-lock!`</a><a name="promesa.protocols/-lock!"></a>
``` clojure

(-lock! it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L69-L69">Source</a></sub></p>

## <a name="promesa.protocols/-mcat">`-mcat`</a><a name="promesa.protocols/-mcat"></a>
``` clojure

(-mcat it f)
(-mcat it f executor)
```

Apply function to a computation and flatten 1 level
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L17-L18">Source</a></sub></p>

## <a name="promesa.protocols/-merr">`-merr`</a><a name="promesa.protocols/-merr"></a>
``` clojure

(-merr it f)
(-merr it f executor)
```

Apply function to a failed computation and flatten 1 level
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L14-L15">Source</a></sub></p>

## <a name="promesa.protocols/-offer!">`-offer!`</a><a name="promesa.protocols/-offer!"></a>
``` clojure

(-offer! it val)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L92-L92">Source</a></sub></p>

## <a name="promesa.protocols/-pending?">`-pending?`</a><a name="promesa.protocols/-pending?"></a>
``` clojure

(-pending? it)
```

Retutns true if a promise is pending.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L37-L37">Source</a></sub></p>

## <a name="promesa.protocols/-poll!">`-poll!`</a><a name="promesa.protocols/-poll!"></a>
``` clojure

(-poll! it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L91-L91">Source</a></sub></p>

## <a name="promesa.protocols/-promise">`-promise`</a><a name="promesa.protocols/-promise"></a>
``` clojure

(-promise it)
```

Create a promise instance from other types
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L41-L41">Source</a></sub></p>

## <a name="promesa.protocols/-put!">`-put!`</a><a name="promesa.protocols/-put!"></a>
``` clojure

(-put! it val handler)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L76-L76">Source</a></sub></p>

## <a name="promesa.protocols/-reject!">`-reject!`</a><a name="promesa.protocols/-reject!"></a>
``` clojure

(-reject! it e)
```

Deliver an error to empty promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L50-L50">Source</a></sub></p>

## <a name="promesa.protocols/-rejected?">`-rejected?`</a><a name="promesa.protocols/-rejected?"></a>
``` clojure

(-rejected? it)
```

Returns true if a promise is rejected.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L36-L36">Source</a></sub></p>

## <a name="promesa.protocols/-release!">`-release!`</a><a name="promesa.protocols/-release!"></a>
``` clojure

(-release! it)
(-release! it n)
```

Release 1 or N permits
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L65-L65">Source</a></sub></p>

## <a name="promesa.protocols/-resolve!">`-resolve!`</a><a name="promesa.protocols/-resolve!"></a>
``` clojure

(-resolve! it v)
```

Deliver a value to empty promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L49-L49">Source</a></sub></p>

## <a name="promesa.protocols/-resolved?">`-resolved?`</a><a name="promesa.protocols/-resolved?"></a>
``` clojure

(-resolved? it)
```

Returns true if a promise is resolved.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L35-L35">Source</a></sub></p>

## <a name="promesa.protocols/-run!">`-run!`</a><a name="promesa.protocols/-run!"></a>
``` clojure

(-run! it task)
```

Submit a task and return a promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L54-L54">Source</a></sub></p>

## <a name="promesa.protocols/-schedule!">`-schedule!`</a><a name="promesa.protocols/-schedule!"></a>
``` clojure

(-schedule! it ms func)
```

Schedule a function to be executed in future.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L59-L59">Source</a></sub></p>

## <a name="promesa.protocols/-size">`-size`</a><a name="promesa.protocols/-size"></a>
``` clojure

(-size it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L93-L93">Source</a></sub></p>

## <a name="promesa.protocols/-submit!">`-submit!`</a><a name="promesa.protocols/-submit!"></a>
``` clojure

(-submit! it task)
```

Submit a task and return a promise.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L55-L55">Source</a></sub></p>

## <a name="promesa.protocols/-take!">`-take!`</a><a name="promesa.protocols/-take!"></a>
``` clojure

(-take! it handler)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L73-L73">Source</a></sub></p>

## <a name="promesa.protocols/-then">`-then`</a><a name="promesa.protocols/-then"></a>
``` clojure

(-then it f)
(-then it f executor)
```

Apply function to a computation and flatten multiple levels
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L28-L29">Source</a></sub></p>

## <a name="promesa.protocols/-try-acquire!">`-try-acquire!`</a><a name="promesa.protocols/-try-acquire!"></a>
``` clojure

(-try-acquire! it)
(-try-acquire! it n)
(-try-acquire! it n t)
```

Try acquire n or n permits, non-blocking or optional timeout
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L63-L63">Source</a></sub></p>

## <a name="promesa.protocols/-unlock!">`-unlock!`</a><a name="promesa.protocols/-unlock!"></a>
``` clojure

(-unlock! it)
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L70-L70">Source</a></sub></p>

## <a name="promesa.protocols/IAwaitable">`IAwaitable`</a><a name="promesa.protocols/IAwaitable"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L101-L102">Source</a></sub></p>

## <a name="promesa.protocols/IBuffer">`IBuffer`</a><a name="promesa.protocols/IBuffer"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L89-L93">Source</a></sub></p>

## <a name="promesa.protocols/ICancellable">`ICancellable`</a><a name="promesa.protocols/ICancellable"></a>




A cancellation abstraction.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L43-L46">Source</a></sub></p>

## <a name="promesa.protocols/IChannelInternal">`IChannelInternal`</a><a name="promesa.protocols/IChannelInternal"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L78-L79">Source</a></sub></p>

## <a name="promesa.protocols/IChannelMultiplexer">`IChannelMultiplexer`</a><a name="promesa.protocols/IChannelMultiplexer"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L81-L83">Source</a></sub></p>

## <a name="promesa.protocols/ICloseable">`ICloseable`</a><a name="promesa.protocols/ICloseable"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L85-L87">Source</a></sub></p>

## <a name="promesa.protocols/ICompletable">`ICompletable`</a><a name="promesa.protocols/ICompletable"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L48-L50">Source</a></sub></p>

## <a name="promesa.protocols/IExecutor">`IExecutor`</a><a name="promesa.protocols/IExecutor"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L52-L55">Source</a></sub></p>

## <a name="promesa.protocols/IHandler">`IHandler`</a><a name="promesa.protocols/IHandler"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L95-L98">Source</a></sub></p>

## <a name="promesa.protocols/ILock">`ILock`</a><a name="promesa.protocols/ILock"></a>




An experimental lock protocol, used internally; no public api
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L67-L70">Source</a></sub></p>

## <a name="promesa.protocols/IPromise">`IPromise`</a><a name="promesa.protocols/IPromise"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L10-L30">Source</a></sub></p>

## <a name="promesa.protocols/IPromiseFactory">`IPromiseFactory`</a><a name="promesa.protocols/IPromiseFactory"></a>




A promise constructor abstraction.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L39-L41">Source</a></sub></p>

## <a name="promesa.protocols/IReadChannel">`IReadChannel`</a><a name="promesa.protocols/IReadChannel"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L72-L73">Source</a></sub></p>

## <a name="promesa.protocols/IScheduler">`IScheduler`</a><a name="promesa.protocols/IScheduler"></a>




A generic abstraction for scheduler facilities.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L57-L59">Source</a></sub></p>

## <a name="promesa.protocols/ISemaphore">`ISemaphore`</a><a name="promesa.protocols/ISemaphore"></a>




An experimental semaphore protocol, used internally; no public api
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L61-L65">Source</a></sub></p>

## <a name="promesa.protocols/IState">`IState`</a><a name="promesa.protocols/IState"></a>




Additional state/introspection abstraction.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L32-L37">Source</a></sub></p>

## <a name="promesa.protocols/IWriteChannel">`IWriteChannel`</a><a name="promesa.protocols/IWriteChannel"></a>



<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L75-L76">Source</a></sub></p>
