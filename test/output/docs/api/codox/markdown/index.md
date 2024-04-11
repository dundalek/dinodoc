---
sidebar_label: markdown
title: codox.markdown
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

This is an example of a namespace written in markdown, rather than the usual
  plaintext format.

  We're going to cover all the standard formatting, like *italics*, **bolds**,
  `:inline-code` ~~strikethrough~~ and "smart quotes" -- so we can check the
  styling in the docs.

  - Let's check
  - unordered
  - lists

  1. And let's also check
  2. ordered
  2. lists

  We should also check code block formatting:

      (defn foo [x]
        (+ x 1))

  Even within fenced code blocks:

  ```clojure
  (defn foo-1 [x]
    (+ x 1))
  ```

  ~~~clojure
  (defn foo-2 [x]
    (+ x 1))
  ~~~

  [Inline links](http://example.com) and [reference links][1].

  [1]: http://example.com

  Extensions like auto-linked URLs (http://example.com) and tabular data:

  foo | bar
  ----|----
   1  |  2
   3  |  4

  Definition lists:

  foo
  : a variable

  :bar
  : a keyword definition

  baz
  : a longer description that goes on for several lines, demonstrating that
    terms in a definition list can have definitions that span multiple lines.

  We can also use wikilinks to reference existing vars, like [[example/foo]] or
  the [[Foop]] protocol, or to reference namespaces, like [`codox.example`](../../codox/example/), and
  optionally customize the link text, like this [[codox.example|link]].

  Any abbreviations, like HTML or HTTP, that have corresponding abbreviations,
  will be marked with `<abbr>` tags.

  *[HTML]: Hyper Text Markup Language
  *[HTTP]: Hyper Text Transfer Protocol

*deprecated in 1.3*





### some\-function {#some-function}
``` clojure
(some-function x)
```


Some function defined in the namespace.

[source](/blob/master/test/projects/codox/example/src/clojure/codox/markdown.clj#L70-L73)

