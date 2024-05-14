---
sidebar_label: protocols
title: promesa.protocols
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

A generic promise abstraction and related protocols.




### IAwaitable {#IAwaitable}


*protocol*


#### \-await\! {#-await-BANG-}
``` clojure
(-await! it)
(-await! it duration)
```


block current thread await termination

*protocol*


### IBuffer {#IBuffer}


*protocol*


#### \-full? {#-full-QMARK-}
``` clojure
(-full? it)
```


*protocol*


#### \-offer\! {#-offer-BANG-}
``` clojure
(-offer! it val)
```


*protocol*


#### \-poll\! {#-poll-BANG-}
``` clojure
(-poll! it)
```


*protocol*


#### \-size {#-size}
``` clojure
(-size it)
```


*protocol*


### ICancellable {#ICancellable}


A cancellation abstraction.

*protocol*


#### \-cancel\! {#-cancel-BANG-}
``` clojure
(-cancel! it)
```


*protocol*


#### \-cancelled? {#-cancelled-QMARK-}
``` clojure
(-cancelled? it)
```


*protocol*


### IChannelInternal {#IChannelInternal}


*protocol*


### IChannelMultiplexer {#IChannelMultiplexer}


*protocol*


### ICloseable {#ICloseable}


*protocol*


#### \-close\! {#-close-BANG-}
``` clojure
(-close! it)
(-close! it reason)
```


*protocol*


#### \-closed? {#-closed-QMARK-}
``` clojure
(-closed? it)
```


*protocol*


### ICompletable {#ICompletable}


*protocol*


#### \-reject\! {#-reject-BANG-}
``` clojure
(-reject! it e)
```


Deliver an error to empty promise.

*protocol*


#### \-resolve\! {#-resolve-BANG-}
``` clojure
(-resolve! it v)
```


Deliver a value to empty promise.

*protocol*


### IExecutor {#IExecutor}


*protocol*


#### \-exec\! {#-exec-BANG-}
``` clojure
(-exec! it task)
```


Submit a task and return nil

*protocol*


#### \-run\! {#-run-BANG-}
``` clojure
(-run! it task)
```


Submit a task and return a promise.

*protocol*


#### \-submit\! {#-submit-BANG-}
``` clojure
(-submit! it task)
```


Submit a task and return a promise.

*protocol*


### IHandler {#IHandler}


*protocol*


#### \-active? {#-active-QMARK-}
``` clojure
(-active? it)
```


*protocol*


#### \-blockable? {#-blockable-QMARK-}
``` clojure
(-blockable? it)
```


*protocol*


#### \-commit\! {#-commit-BANG-}
``` clojure
(-commit! it)
```


*protocol*


### ILock {#ILock}


An experimental lock protocol, used internally; no public api

*protocol*


#### \-lock\! {#-lock-BANG-}
``` clojure
(-lock! it)
```


*protocol*


#### \-unlock\! {#-unlock-BANG-}
``` clojure
(-unlock! it)
```


*protocol*


### IPromise {#IPromise}


*protocol*


#### \-fmap {#-fmap}
``` clojure
(-fmap it f)
(-fmap it f executor)
```


Apply function to a computation

*protocol*


#### \-fnly {#-fnly}
``` clojure
(-fnly it f)
(-fnly it f executor)
```


Apply function to a computation independently if is failed or
    successful; the return value is ignored.

*protocol*


#### \-hmap {#-hmap}
``` clojure
(-hmap it f)
(-hmap it f executor)
```


Apply function to a computation independently if is failed or
    successful.

*protocol*


#### \-mcat {#-mcat}
``` clojure
(-mcat it f)
(-mcat it f executor)
```


Apply function to a computation and flatten 1 level

*protocol*


#### \-merr {#-merr}
``` clojure
(-merr it f)
(-merr it f executor)
```


Apply function to a failed computation and flatten 1 level

*protocol*


#### \-then {#-then}
``` clojure
(-then it f)
(-then it f executor)
```


Apply function to a computation and flatten multiple levels

*protocol*


### IPromiseFactory {#IPromiseFactory}


A promise constructor abstraction.

*protocol*


#### \-promise {#-promise}
``` clojure
(-promise it)
```


Create a promise instance from other types

*protocol*


### IReadChannel {#IReadChannel}


*protocol*


#### \-take\! {#-take-BANG-}
``` clojure
(-take! it handler)
```


*protocol*


### IScheduler {#IScheduler}


A generic abstraction for scheduler facilities.

*protocol*


#### \-schedule\! {#-schedule-BANG-}
``` clojure
(-schedule! it ms func)
```


Schedule a function to be executed in future.

*protocol*


### ISemaphore {#ISemaphore}


An experimental semaphore protocol, used internally; no public api

*protocol*


#### \-acquire\! {#-acquire-BANG-}
``` clojure
(-acquire! it)
(-acquire! it n)
```


Acquire 1 or N permits

*protocol*


#### \-release\! {#-release-BANG-}
``` clojure
(-release! it)
(-release! it n)
```


Release 1 or N permits

*protocol*


#### \-try\-acquire\! {#-try-acquire-BANG-}
``` clojure
(-try-acquire! it)
(-try-acquire! it n)
(-try-acquire! it n t)
```


Try acquire n or n permits, non-blocking or optional timeout

*protocol*


### IState {#IState}


Additional state/introspection abstraction.

*protocol*


#### \-extract {#-extract}
``` clojure
(-extract it)
(-extract it default)
```


Extract the current value.

*protocol*


#### \-pending? {#-pending-QMARK-}
``` clojure
(-pending? it)
```


Retutns true if a promise is pending.

*protocol*


#### \-rejected? {#-rejected-QMARK-}
``` clojure
(-rejected? it)
```


Returns true if a promise is rejected.

*protocol*


#### \-resolved? {#-resolved-QMARK-}
``` clojure
(-resolved? it)
```


Returns true if a promise is resolved.

*protocol*


### IWriteChannel {#IWriteChannel}


*protocol*


#### \-put\! {#-put-BANG-}
``` clojure
(-put! it val handler)
```


*protocol*

