---
sidebar_label: protocols
title: promesa.protocols
toc_min_heading_level: 2
toc_max_heading_level: 4
---

# <a name="promesa.protocols">promesa.protocols</a>


A generic promise abstraction and related protocols.




### IAwaitable {#IAwaitable}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L101-L102">Source</a></sub></p>

#### \-await\! {#-await-BANG-}
``` clojure

(-await! it)
(-await! it duration)
```


block current thread await termination

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L102-L102">Source</a></sub></p>

### IBuffer {#IBuffer}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L89-L93">Source</a></sub></p>

#### \-full? {#-full-QMARK-}
``` clojure

(-full? it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L90-L90">Source</a></sub></p>

#### \-offer\! {#-offer-BANG-}
``` clojure

(-offer! it val)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L92-L92">Source</a></sub></p>

#### \-poll\! {#-poll-BANG-}
``` clojure

(-poll! it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L91-L91">Source</a></sub></p>

#### \-size {#-size}
``` clojure

(-size it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L93-L93">Source</a></sub></p>

### ICancellable {#ICancellable}


A cancellation abstraction.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L43-L46">Source</a></sub></p>

#### \-cancel\! {#-cancel-BANG-}
``` clojure

(-cancel! it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L45-L45">Source</a></sub></p>

#### \-cancelled? {#-cancelled-QMARK-}
``` clojure

(-cancelled? it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L46-L46">Source</a></sub></p>

### IChannelInternal {#IChannelInternal}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L78-L79">Source</a></sub></p>

### IChannelMultiplexer {#IChannelMultiplexer}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L81-L83">Source</a></sub></p>

### ICloseable {#ICloseable}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L85-L87">Source</a></sub></p>

#### \-close\! {#-close-BANG-}
``` clojure

(-close! it)
(-close! it reason)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L87-L87">Source</a></sub></p>

#### \-closed? {#-closed-QMARK-}
``` clojure

(-closed? it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L86-L86">Source</a></sub></p>

### ICompletable {#ICompletable}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L48-L50">Source</a></sub></p>

#### \-reject\! {#-reject-BANG-}
``` clojure

(-reject! it e)
```


Deliver an error to empty promise.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L50-L50">Source</a></sub></p>

#### \-resolve\! {#-resolve-BANG-}
``` clojure

(-resolve! it v)
```


Deliver a value to empty promise.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L49-L49">Source</a></sub></p>

### IExecutor {#IExecutor}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L52-L55">Source</a></sub></p>

#### \-exec\! {#-exec-BANG-}
``` clojure

(-exec! it task)
```


Submit a task and return nil

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L53-L53">Source</a></sub></p>

#### \-run\! {#-run-BANG-}
``` clojure

(-run! it task)
```


Submit a task and return a promise.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L54-L54">Source</a></sub></p>

#### \-submit\! {#-submit-BANG-}
``` clojure

(-submit! it task)
```


Submit a task and return a promise.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L55-L55">Source</a></sub></p>

### IHandler {#IHandler}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L95-L98">Source</a></sub></p>

#### \-active? {#-active-QMARK-}
``` clojure

(-active? it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L96-L96">Source</a></sub></p>

#### \-blockable? {#-blockable-QMARK-}
``` clojure

(-blockable? it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L98-L98">Source</a></sub></p>

#### \-commit\! {#-commit-BANG-}
``` clojure

(-commit! it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L97-L97">Source</a></sub></p>

### ILock {#ILock}


An experimental lock protocol, used internally; no public api

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L67-L70">Source</a></sub></p>

#### \-lock\! {#-lock-BANG-}
``` clojure

(-lock! it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L69-L69">Source</a></sub></p>

#### \-unlock\! {#-unlock-BANG-}
``` clojure

(-unlock! it)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L70-L70">Source</a></sub></p>

### IPromise {#IPromise}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L10-L30">Source</a></sub></p>

#### \-fmap {#-fmap}
``` clojure

(-fmap it f)
(-fmap it f executor)
```


Apply function to a computation

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L11-L12">Source</a></sub></p>

#### \-fnly {#-fnly}
``` clojure

(-fnly it f)
(-fnly it f executor)
```


Apply function to a computation independently if is failed or
    successful; the return value is ignored.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L24-L26">Source</a></sub></p>

#### \-hmap {#-hmap}
``` clojure

(-hmap it f)
(-hmap it f executor)
```


Apply function to a computation independently if is failed or
    successful.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L20-L22">Source</a></sub></p>

#### \-mcat {#-mcat}
``` clojure

(-mcat it f)
(-mcat it f executor)
```


Apply function to a computation and flatten 1 level

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L17-L18">Source</a></sub></p>

#### \-merr {#-merr}
``` clojure

(-merr it f)
(-merr it f executor)
```


Apply function to a failed computation and flatten 1 level

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L14-L15">Source</a></sub></p>

#### \-then {#-then}
``` clojure

(-then it f)
(-then it f executor)
```


Apply function to a computation and flatten multiple levels

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L28-L29">Source</a></sub></p>

### IPromiseFactory {#IPromiseFactory}


A promise constructor abstraction.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L39-L41">Source</a></sub></p>

#### \-promise {#-promise}
``` clojure

(-promise it)
```


Create a promise instance from other types

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L41-L41">Source</a></sub></p>

### IReadChannel {#IReadChannel}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L72-L73">Source</a></sub></p>

#### \-take\! {#-take-BANG-}
``` clojure

(-take! it handler)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L73-L73">Source</a></sub></p>

### IScheduler {#IScheduler}


A generic abstraction for scheduler facilities.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L57-L59">Source</a></sub></p>

#### \-schedule\! {#-schedule-BANG-}
``` clojure

(-schedule! it ms func)
```


Schedule a function to be executed in future.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L59-L59">Source</a></sub></p>

### ISemaphore {#ISemaphore}


An experimental semaphore protocol, used internally; no public api

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L61-L65">Source</a></sub></p>

#### \-acquire\! {#-acquire-BANG-}
``` clojure

(-acquire! it)
(-acquire! it n)
```


Acquire 1 or N permits

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L64-L64">Source</a></sub></p>

#### \-release\! {#-release-BANG-}
``` clojure

(-release! it)
(-release! it n)
```


Release 1 or N permits

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L65-L65">Source</a></sub></p>

#### \-try\-acquire\! {#-try-acquire-BANG-}
``` clojure

(-try-acquire! it)
(-try-acquire! it n)
(-try-acquire! it n t)
```


Try acquire n or n permits, non-blocking or optional timeout

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L63-L63">Source</a></sub></p>

### IState {#IState}


Additional state/introspection abstraction.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L32-L37">Source</a></sub></p>

#### \-extract {#-extract}
``` clojure

(-extract it)
(-extract it default)
```


Extract the current value.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L34-L34">Source</a></sub></p>

#### \-pending? {#-pending-QMARK-}
``` clojure

(-pending? it)
```


Retutns true if a promise is pending.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L37-L37">Source</a></sub></p>

#### \-rejected? {#-rejected-QMARK-}
``` clojure

(-rejected? it)
```


Returns true if a promise is rejected.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L36-L36">Source</a></sub></p>

#### \-resolved? {#-resolved-QMARK-}
``` clojure

(-resolved? it)
```


Returns true if a promise is resolved.

*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L35-L35">Source</a></sub></p>

### IWriteChannel {#IWriteChannel}


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L75-L76">Source</a></sub></p>

#### \-put\! {#-put-BANG-}
``` clojure

(-put! it val handler)
```


*protocol*

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/protocols.cljc#L76-L76">Source</a></sub></p>
