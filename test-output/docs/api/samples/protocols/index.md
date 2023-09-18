---
sidebar_label: protocols
title: samples.protocols
toc_min_heading_level: 2
toc_max_heading_level: 4
---

# <a name="samples.protocols">samples.protocols</a>






### IBulkhead {#IBulkhead}


Bulkhead main API

*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L7-L10">Source</a></sub></p>

#### \-get\-stats {#-get-stats}
``` clojure
(-get-stats _)
```


Get internal statistics of the bulkhead instance

*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L9-L9">Source</a></sub></p>

#### \-invoke\! {#-invoke-BANG-}
``` clojure
(-invoke! _ f)
```


Call synchronously a function under bulkhead context

*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L10-L10">Source</a></sub></p>

### IQueue {#IQueue}


*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L3-L5">Source</a></sub></p>

#### \-offer\! {#-offer-BANG-}
``` clojure
(-offer! _ _)
```


*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L5-L5">Source</a></sub></p>

#### \-poll\! {#-poll-BANG-}
``` clojure
(-poll! _)
```


*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L4-L4">Source</a></sub></p>
