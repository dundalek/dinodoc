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


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L60-L62)


### Foop {#Foop}


An example protocol.

*protocol*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L43-L46)


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

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L15-L17)


### foo {#foo}
``` clojure
(foo x)
(foo x y & z)
```


This is an example function.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L10-L13)


### foobar {#foobar}
``` clojure
(foobar x)
```


An obsolete function.

*deprecated*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L28-L31)


### foobaz {#foobaz}
``` clojure
(foobaz x)
```


An obsolete function with a specific version.

*deprecated in 1.1*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L33-L36)


### foom {#foom}
``` clojure
(foom x)
```


An example multimethod.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L48-L51)


### froo {#froo}


A derefable thing created using reify.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L64-L67)


### markfoo {#markfoo}
``` clojure
(markfoo x)
```


A docstring that selectively uses **markdown**.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L55-L58)


### quz {#quz}
``` clojure
(quz x)
```


Another example function.

*added in 1.1*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L23-L26)


### quzbar {#quzbar}
``` clojure
(quzbar x)
```


A function with a lifespan.

*deprecated in 1.1 | added in 1.0*


[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L38-L41)


### zoo? {#zoo-QMARK-}
``` clojure
(zoo? x)
```


An example predicate.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/example3.cljs#L19-L21)

