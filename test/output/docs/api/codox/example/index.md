---
sidebar_label: example
title: codox.example
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

ClojureScript version.

*deprecated in 2.0 | added in 1.1*





### \*conn\* {#-STAR-conn-STAR-}


A dynamic var.

*dynamic*


### \-&gt;CljsRecord {#--GT-CljsRecord}
``` clojure
(->CljsRecord aah choo)
```


### CljsRecord {#CljsRecord}


### Foop {#Foop}


An example protocol.

*protocol*


#### barp {#barp}
``` clojure
(barp x y)
```


Another protocol function.

*protocol | deprecated*


#### foop {#foop}
``` clojure
(foop x)
```


A protocol function belonging to the protocol Foom.

*protocol*


### bar {#bar}
``` clojure
(bar x & body)
```


This is an example macro.

*macro*


### baz {#baz}


This is an example var.

### foo {#foo}
``` clojure
(foo x)
(foo x y & z)
```


This is an example function.

### foobar {#foobar}
``` clojure
(foobar x)
```


An obsolete function.

*deprecated*


### foobaz {#foobaz}
``` clojure
(foobaz x)
```


An obsolete function with a specific version.

*deprecated in 1.1*


### foom {#foom}
``` clojure
(foom x)
```


An example multimethod.

### map\-&gt;CljsRecord {#map--GT-CljsRecord}
``` clojure
(map->CljsRecord m)
```


### markbar {#markbar}
``` clojure
(markbar x)
```


See [`foo`](#foo), and also [[example2/bar]].

### markfoo {#markfoo}
``` clojure
(markfoo x)
```


A docstring that selectively uses **markdown**.

### quz {#quz}
``` clojure
(quz x)
```


Another example function.

*added in 1.1*


### quzbar {#quzbar}
``` clojure
(quzbar x)
```


A function with a lifespan.

*deprecated in 1.1 | added in 1.0*


### quzquz {#quzquz}
``` clojure
(quzquz x)
```


This is a ClojureScript-only function.

### zoo? {#zoo-QMARK-}
``` clojure
(zoo? x)
```


An example predicate.
