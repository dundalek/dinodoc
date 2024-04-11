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


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L73-L75)


### \-&gt;CljsRecord {#--GT-CljsRecord}
``` clojure
(->CljsRecord aah choo)
```


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.cljs#L13-L13)


### CljsRecord {#CljsRecord}


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.cljs#L13-L13)


### Foop {#Foop}


An example protocol.

*protocol*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L46-L49)


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


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L14-L16)


### baz {#baz}


This is an example var.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L18-L20)


### foo {#foo}
``` clojure
(foo x)
(foo x y & z)
```


This is an example function.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L9-L12)


### foobar {#foobar}
``` clojure
(foobar x)
```


An obsolete function.

*deprecated*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L31-L34)


### foobaz {#foobaz}
``` clojure
(foobaz x)
```


An obsolete function with a specific version.

*deprecated in 1.1*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L36-L39)


### foom {#foom}
``` clojure
(foom x)
```


An example multimethod.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L51-L54)


### map\-&gt;CljsRecord {#map--GT-CljsRecord}
``` clojure
(map->CljsRecord m)
```


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.cljs#L13-L13)


### markbar {#markbar}
``` clojure
(markbar x)
```


See [`foo`](#foo), and also [[example2/bar]].

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L68-L71)


### markfoo {#markfoo}
``` clojure
(markfoo x)
```


A docstring that selectively uses **markdown**.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L63-L66)


### quz {#quz}
``` clojure
(quz x)
```


Another example function.

*added in 1.1*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L26-L29)


### quzbar {#quzbar}
``` clojure
(quzbar x)
```


A function with a lifespan.

*deprecated in 1.1 | added in 1.0*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L41-L44)


### quzquz {#quzquz}
``` clojure
(quzquz x)
```


This is a ClojureScript-only function.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.cljs#L9-L11)


### zoo? {#zoo-QMARK-}
``` clojure
(zoo? x)
```


An example predicate.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example.clj#L22-L24)

