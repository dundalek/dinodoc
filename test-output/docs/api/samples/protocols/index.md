---
sidebar_label: protocols
title: samples.protocols
toc_min_heading_level: 2
toc_max_heading_level: 4
---

# <a name="samples.protocols">samples.protocols</a>






## <a name="samples.protocols/IBulkhead">`IBulkhead`</a><a name="samples.protocols/IBulkhead"></a>


Bulkhead main API

*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L7-L10">Source</a></sub></p>

### <a name="samples.protocols/-get-stats">`-get-stats`</a><a name="samples.protocols/-get-stats"></a>
``` clojure

(-get-stats _)
```


Get internal statistics of the bulkhead instance

*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L9-L9">Source</a></sub></p>

### <a name="samples.protocols/-invoke!">`-invoke!`</a><a name="samples.protocols/-invoke!"></a>
``` clojure

(-invoke! _ f)
```


Call synchronously a function under bulkhead context

*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L10-L10">Source</a></sub></p>

## <a name="samples.protocols/IQueue">`IQueue`</a><a name="samples.protocols/IQueue"></a>


*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L3-L5">Source</a></sub></p>

### <a name="samples.protocols/-offer!">`-offer!`</a><a name="samples.protocols/-offer!"></a>
``` clojure

(-offer! _ _)
```


*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L5-L5">Source</a></sub></p>

### <a name="samples.protocols/-poll!">`-poll!`</a><a name="samples.protocols/-poll!"></a>
``` clojure

(-poll! _)
```


*protocol*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/protocols.clj#L4-L4">Source</a></sub></p>
