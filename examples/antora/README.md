# Antora integration

See the [rendered example](https://dinodoc.pages.dev/examples/antora/example/api/example/main/). Generate example docs with:

```sh
bb generate
```

Sample link to [[example.main/greet]] function in API docs.

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

## How the example works

1) The Antora generator uses `antora-playbook.yml` where sources are defined.
2) That resolves to a component described in `antora.yml` which uses the [collector-extension](https://gitlab.com/antora/antora-collector-extension) that executes the API docs generator and converts markdown files to asciidoc using `pandoc`.

We do this two-step process to be able to leverage functionality of Antora to fetch and collect content from multiple repos, where each can optionaly specify how its API docs are generated.

## TODOs

- [ ] Fix link to source in API docs.
- [ ] Fix headings ids starting with dash like `-main`.
- [ ] Generate `nav.adoc` file for sidebar tree navigation.
- [ ] Explore using [xrefs](https://docs.antora.org/antora/latest/navigation/xrefs-and-link-text/) for linking into API docs or across components.
- [ ] Try to render more examples using Antora, e.g. statecharts, db schemas, structurizr, openapi.
