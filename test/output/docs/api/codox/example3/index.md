---
sidebar_label: example3
title: codox.example3
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

This is an example namespace for testing ClojureScript support.

  Some more detailed description down here.

  An inline link: http://example.com
  An inline HTTPS link: https://example.com




### \*conn\* {#-STAR-conn-STAR-}


A dynamic var.

*dynamic*


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

### froo {#froo}


A derefable thing created using reify.

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


### zoo? {#zoo-QMARK-}
``` clojure
(zoo? x)
```


An example predicate.
