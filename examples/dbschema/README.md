# Database Schemas

This example demonstrates using [tbls](https://github.com/k1LoW/tbls) utility to generate documentation for relational database schema.

Example:
- [Chinook](https://dinodoc.pages.dev/examples/dbschema/) - documentation for the [Chinook](https://github.com/lerocha/chinook-database) sample database using [doc.clj](https://github.com/dundalek/dinodoc/blob/main/examples/dbschema/doc.clj) script


### Diagram format considerations

`tbls` supports rendering diagrams as SVG (default, uses graphviz) and Mermaid.
- I find that Mermaid diagrams look a bit better, but a disadvantage is that for larger schemas the diagrams are scaled down and the text can end up illegible.
- Therefore I slightly prefer the SVG format because as a workaround one can open it in another browser tab to pan and zoom to see all details.
