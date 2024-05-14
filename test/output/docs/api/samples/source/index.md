---
sidebar_label: source
title: samples.source
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

docstring on namespace

*deprecated*





### \*dynamic\-var\-example\* {#-STAR-dynamic-var-example-STAR-}


We need to escape var names otherwise earmuffs are not shown but interpreted as italic.

*dynamic*


### &lt;&lt; {#-LT--LT-}
``` clojure
(<<)
```


Angle brackets in var name

### Foo {#Foo}
``` clojure
(Foo)
```


Foo with different case, link does not clash with [`foo`](#foo).

### \_underscore\-surrounded\_ {#-underscore-surrounded-}
``` clojure
(_underscore-surrounded_)
```


### a\-macro {#a-macro}
``` clojure
(a-macro)
```


hello

*macro*


### added\-and\-deprecated {#added-and-deprecated}
``` clojure
(added-and-deprecated)
```


*deprecated | added in 8.0*


### added\-tag {#added-tag}
``` clojure
(added-tag)
```


*added*


### added\-version {#added-version}
``` clojure
(added-version)
```


*added in 8.0*


### deprecated\-tag {#deprecated-tag}
``` clojure
(deprecated-tag)
```


Doc string

*deprecated*


### deprecated\-version {#deprecated-version}
``` clojure
(deprecated-version)
```


Doc string

*deprecated in 9.0*


### foo {#foo}
``` clojure
(foo)
```


Hello, there is also [`Foo`](#Foo) that differs in casing.

### has\-&gt;arrow {#has--GT-arrow}
``` clojure
(has->arrow)
```


### links\-backticks {#links-backticks}
``` clojure
(links-backticks)
```


Link to a var in the current namespace: [`foo`](#foo)

  Link to a qualified var: [`samples.crossplatform/some-clj-fn`](../../samples/crossplatform/#some-clj-fn)

  Link to a namespace: [`samples.protocols`](../../samples/protocols/)

  Link to a var containing a special character: [`has->arrow`](#has--GT-arrow)
  

### links\-backticks\-in\-codeblock {#links-backticks-in-codeblock}
``` clojure
(links-backticks-in-codeblock)
```


 In a code block should ideally not be replaced.
  ```clojure
  ;; Link to a var in the current namespace: [`foo`](#foo)

  ;; Link to a qualified var: [`samples.crossplatform/some-clj-fn`](../../samples/crossplatform/#some-clj-fn)

  ;; Link to a namespace: [`samples.protocols`](../../samples/protocols/)
  ```

### links\-backticks\-multiple\-occurences {#links-backticks-multiple-occurences}
``` clojure
(links-backticks-multiple-occurences)
```


Same link referenced multiple times will not get messed up: [`foo`](#foo) and [`foo`](#foo)

### links\-wikilinks {#links-wikilinks}
``` clojure
(links-wikilinks)
```


Link to a var in the current namespace: [`foo`](#foo)

  Link to a qualified var: [`samples.crossplatform/some-clj-fn`](../../samples/crossplatform/#some-clj-fn)

  Link to a namespace: [`samples.protocols`](../../samples/protocols/)

  Link with a title supported by codox: [[samples.crossplatform/some-clj-fn|some-title]]
