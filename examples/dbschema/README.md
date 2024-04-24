# Database Schemas

This example demonstrates using [tbls](https://github.com/k1LoW/tbls) utility to generate documentation for relational database schema.

Examples (defined in [doc.clj](https://github.com/dundalek/dinodoc/blob/main/examples/dbschema/doc.clj) script):

- [Chinook](https://dinodoc.pages.dev/examples/dbschema/chinook/) - documentation for the [Chinook](https://github.com/lerocha/chinook-database) sample database using SQLite.
- [Sakila](https://dinodoc.pages.dev/examples/dbschema/sakila/) - uses the [Pagila](https://github.com/devrimgunduz/pagila) sample database and PostgreSQL.  
  It may not be desirable to connect to real database just to extract schema.
  This example shows how to generate docs from a static DDL SQL script by loading it into a temporary Postgres instance.

Examples of wikilinks:
- [[Album]] or [[chinook:Album]]
- [[actor]] or [[sakila:public.actor]] or [[sakila:actor]] (without the `public.` schema)

### Diagram format considerations

`tbls` supports rendering diagrams as SVG (default, uses graphviz) and Mermaid.

- I find that Mermaid diagrams look a bit better, but a disadvantage is that for larger schemas the diagrams are scaled down and the text can end up illegible.
- Therefore I slightly prefer the SVG format because as a workaround one can open it in another browser tab to pan and zoom to see all details.
