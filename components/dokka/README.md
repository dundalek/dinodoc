# Java, Kotlin (Dokka)

[Dokka](https://github.com/Kotlin/dokka) engine is used to generate documentation for [Java](https://github.com/dundalek/dinodoc/tree/main/examples/java) and [Kotlin](https://github.com/dundalek/dinodoc/tree/main/examples/kotlin) using the `gfm-plugin`.

Use [[dinodoc.dokka/generate]] function.

## Experimental status

This is an experimental approach that currently has known issues and limitations:
- Class members are rendered on separate pages
  - Default Dokka markdown renderer (GFM) renders each class member as a separate page which is not very usable.
  - A modification is needed to render all members on a single like the HTML renderer.
- Sidebar items and headings do not have proper titles
  - We should render title in frontmatter metadata. The `dokkaJekyll` renderer (that builds on top of `dokkaGfm`) outputs frontmatter and can serve as an inspiration.
  - We can't use the `jekyll` renderer instead of `gfm` because it transforms link targets from `.md` to `.html` which breaks them in Docusaurus.
- Bad look of rendered signatures
  - They are rendered as regular text and contain extra `[jvm]` plaftorm tag (issues [#2073](https://github.com/Kotlin/dokka/issues/2073), [#2782](https://github.com/Kotlin/dokka/issues/2782)).
  - As a workaround we could hide the tag for non-multiplatform projects and wrap the signature in `<pre><code></code></pre>` either via regex replacement (easier, but brittle) or plugin transformation (more effort, but robust).
