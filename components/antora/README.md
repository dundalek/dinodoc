# Antora Integration

## Motivation

Why explore [Antora](https://antora.org) when already using Docusaurus?

#### Pro

The main advantage of Antora over Docusaurus is that it is designed to fetch and collect content from [multiple sources](https://opendevise.com/blog/content-is-sovereign/) which is a great fit for internal documentation where the system consists of multiple services and components.

For the Docusaurus-based examples I am currently collecting the content manually (using git submodules for now).
This is a temporary approach.
A more robust solution would need to be eventually implemented.
By leveraging this feature of Antora it could possible reduce the scope of the project and save some effort.

#### Cons

- No markdown
  - Antora is based on [AsciiDoc](https://asciidoc.org). It has more features allowing more flexibility when building advanced sites. But it is also more complicated. When a goal is to encourage developers writing documentation, I feel it adds additional friction, so markdown is preferrable.
  - Since Markdown is a simpler format, it should be possible to convert from it using [pandoc](https://pandoc.org) or [kramdown-asciidoc](https://github.com/asciidoctor/kramdown-asciidoc).
- Worse UI - it feels less polished, but it is still tolerable.
  - No dark mode for default theme.
  - Sidebar flickers when navigating to another page.
  - Slower navigation because it does not preload content on hover.

## Utilities

Depends on [pandoc](https://pandoc.org/), v3+ needs to be installed.

Use [[dinodoc.antora/transform-directory]]
to convert a directory tree of markdown files to asciidoc format that can be rendered with Antora.

Use [[dinodoc.antora/generate-navigation]]
to auto-generate a sidebar tree navigation.
This is useful because otherwise navigation needs to specified manually in Antora
making it less feasible for auto-generated API docs.

See an [example](https://github.com/dundalek/dinodoc/tree/main/examples/antora) that uses these utilities.
