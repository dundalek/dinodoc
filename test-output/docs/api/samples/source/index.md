
-----
# <a name="samples.source">samples.source</a>


docstring on namespace




## <a name="samples.source/*dynamic-var-example*">`*dynamic-var-example*`</a><a name="samples.source/*dynamic-var-example*"></a>




We need to escape var names otherwise earmuffs are not shown but interpreted as italic.
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L9-L11">Source</a></sub></p>

## <a name="samples.source/<<">`<<`</a><a name="samples.source/<<"></a>
``` clojure

(<<)
```

Angle brackets in var name
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L17-L19">Source</a></sub></p>

## <a name="samples.source/_underscore-surrounded_">`_underscore-surrounded_`</a><a name="samples.source/_underscore-surrounded_"></a>
``` clojure

(_underscore-surrounded_)
```
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L13-L13">Source</a></sub></p>

## <a name="samples.source/a-macro">`a-macro`</a><a name="samples.source/a-macro"></a>
``` clojure

(a-macro)
```
Function.

hello
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L44-L46">Source</a></sub></p>

## <a name="samples.source/added-and-deprecated">`added-and-deprecated`</a><a name="samples.source/added-and-deprecated"></a>
``` clojure

(added-and-deprecated)
```
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L39-L42">Source</a></sub></p>

## <a name="samples.source/added-tag">`added-tag`</a><a name="samples.source/added-tag"></a>
``` clojure

(added-tag)
```
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L31-L33">Source</a></sub></p>

## <a name="samples.source/added-version">`added-version`</a><a name="samples.source/added-version"></a>
``` clojure

(added-version)
```
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L35-L37">Source</a></sub></p>

## <a name="samples.source/deprecated-tag">`deprecated-tag`</a><a name="samples.source/deprecated-tag"></a>
``` clojure

(deprecated-tag)
```

Doc string
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L21-L24">Source</a></sub></p>

## <a name="samples.source/deprecated-version">`deprecated-version`</a><a name="samples.source/deprecated-version"></a>
``` clojure

(deprecated-version)
```

Doc string
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L26-L29">Source</a></sub></p>

## <a name="samples.source/foo">`foo`</a><a name="samples.source/foo"></a>
``` clojure

(foo)
```

Hello
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L5-L7">Source</a></sub></p>

## <a name="samples.source/has->arrow">`has->arrow`</a><a name="samples.source/has->arrow"></a>
``` clojure

(has->arrow)
```
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L15-L15">Source</a></sub></p>

## <a name="samples.source/links-backticks">`links-backticks`</a><a name="samples.source/links-backticks"></a>
``` clojure

(links-backticks)
```

Link to a var in the current namespace: [`foo`](#samples.source/foo)

  Link to a qualified var: [`samples.crossplatform/some-clj-fn`](#samples.crossplatform/some-clj-fn)

  Link to a namespace: [`samples.protocols`](#samples.protocols)

  Link to a var containing a special character: [`has->arrow`](#samples.source/has->arrow)
  
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L48-L57">Source</a></sub></p>

## <a name="samples.source/links-backticks-in-codeblock">`links-backticks-in-codeblock`</a><a name="samples.source/links-backticks-in-codeblock"></a>
``` clojure

(links-backticks-in-codeblock)
```

 In a code block should ideally not be replaced.
  ```clojure
  ;; Link to a var in the current namespace: [`foo`](#samples.source/foo)

  ;; Link to a qualified var: [`samples.crossplatform/some-clj-fn`](#samples.crossplatform/some-clj-fn)

  ;; Link to a namespace: [`samples.protocols`](#samples.protocols)
  ```
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L59-L68">Source</a></sub></p>

## <a name="samples.source/links-backticks-multiple-occurences">`links-backticks-multiple-occurences`</a><a name="samples.source/links-backticks-multiple-occurences"></a>
``` clojure

(links-backticks-multiple-occurences)
```

Same link referenced multiple times will get messed up: [[`foo`](#samples.source/foo)](#samples.source/foo) and [[`foo`](#samples.source/foo)](#samples.source/foo)
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L70-L72">Source</a></sub></p>

## <a name="samples.source/links-wikilinks">`links-wikilinks`</a><a name="samples.source/links-wikilinks"></a>
``` clojure

(links-wikilinks)
```

Link to a var in the current namespace: [[foo]]

  Link to a qualified var: [`samples.crossplatform/some-clj-fn`](#samples.crossplatform/some-clj-fn)

  Link to a namespace: [`samples.protocols`](#samples.protocols)

  Link with a title supported by codox: [[samples.crossplatform/some-clj-fn|some-title]]
<p><sub><a href="https://github.com/borkdude/quickdoc/blob/master//src/samples/source.clj#L74-L82">Source</a></sub></p>
