# Changelog

## [main](https://github.com/dundalek/dinodoc/compare/v0.3.0...main) (unreleased)

## [v0.3.0](https://github.com/dundalek/dinodoc/compare/v0.2.0...v0.3.0) (2024-05-16)

New features:
- DB Schema and Structurizr generators now support wiki links to elements
- Integration with language-specific API docs generators (rendering and linking):
  - Dokka for Kotlin and Java (markdown-based)
  - Javadoc for Java (HTML-based)
  - Rustdoc for Rust (HTML-based)
- Rendering and linking to documentation based on non-code models
  - Generate documentation for ContextMapper model (Domain Driven Design)
  - Render and link to glossary of terms from Contextive definitions
- Experimental helpers to render documentation with Antora 
  - Transform Markdown to AsciiDoc
  - Auto-generate navigation sidebar

Fixes:
- Fixed rendering without a git repo

## [0.2.0](https://github.com/dundalek/dinodoc/compare/v0.1.0...v0.2.0) (2024-03-05)

New features and examples:
- Structurizr Architecture docs rendering
- HTTP API documentation using OpenAPI
- DB schema documentation
- Statecharts diagram rendering

Fixes:
- Fixed links to API docs in global hierarchy mode

## 0.1.0 (2024-01-16)

Initial release
