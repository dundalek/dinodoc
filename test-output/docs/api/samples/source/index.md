---
sidebar_label: source
title: samples.source
toc_min_heading_level: 2
toc_max_heading_level: 4
---

# <a name="samples.source">samples.source</a>


docstring on namespace

*deprecated*





### \*dynamic\-var\-example\* {#-STAR-dynamic-var-example-STAR-}


We need to escape var names otherwise earmuffs are not shown but interpreted as italic.

*dynamic*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L9-L11">Source</a></sub></p>

### &lt;&lt; {#-LT--LT-}
``` clojure
(<<)
```


Angle brackets in var name
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L17-L19">Source</a></sub></p>

### \_underscore\-surrounded\_ {#-underscore-surrounded-}
``` clojure
(_underscore-surrounded_)
```

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L13-L13">Source</a></sub></p>

### a\-macro {#a-macro}
``` clojure
(a-macro)
```


hello

*macro*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L44-L46">Source</a></sub></p>

### added\-and\-deprecated {#added-and-deprecated}
``` clojure
(added-and-deprecated)
```


*deprecated | added in 8.0*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L39-L42">Source</a></sub></p>

### added\-tag {#added-tag}
``` clojure
(added-tag)
```


*added*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L31-L33">Source</a></sub></p>

### added\-version {#added-version}
``` clojure
(added-version)
```


*added in 8.0*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L35-L37">Source</a></sub></p>

### deprecated\-tag {#deprecated-tag}
``` clojure
(deprecated-tag)
```


Doc string

*deprecated*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L21-L24">Source</a></sub></p>

### deprecated\-version {#deprecated-version}
``` clojure
(deprecated-version)
```


Doc string

*deprecated in 9.0*

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L26-L29">Source</a></sub></p>

### foo {#foo}
``` clojure
(foo)
```


Hello
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L5-L7">Source</a></sub></p>

### has\-&gt;arrow {#has--GT-arrow}
``` clojure
(has->arrow)
```

<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L15-L15">Source</a></sub></p>

### links\-backticks {#links-backticks}
``` clojure
(links-backticks)
```


Link to a var in the current namespace: [`foo`](#foo)

  Link to a qualified var: [`samples.crossplatform/some-clj-fn`](../../samples/crossplatform/#some-clj-fn)

  Link to a namespace: [`samples.protocols`](../../samples/protocols/)

  Link to a var containing a special character: [`has->arrow`](#has--GT-arrow)
  
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L48-L57">Source</a></sub></p>

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
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L59-L68">Source</a></sub></p>

### links\-backticks\-multiple\-occurences {#links-backticks-multiple-occurences}
``` clojure
(links-backticks-multiple-occurences)
```


Same link referenced multiple times will get messed up: [[`foo`](#foo)](#foo) and [[`foo`](#foo)](#foo)
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L70-L72">Source</a></sub></p>

### links\-wikilinks {#links-wikilinks}
``` clojure
(links-wikilinks)
```


Link to a var in the current namespace: [`foo`](#foo)

  Link to a qualified var: [`samples.crossplatform/some-clj-fn`](../../samples/crossplatform/#some-clj-fn)

  Link to a namespace: [`samples.protocols`](../../samples/protocols/)

  Link with a title supported by codox: [[samples.crossplatform/some-clj-fn|some-title]]
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L74-L82">Source</a></sub></p>
