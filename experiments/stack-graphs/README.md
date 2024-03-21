This is experiment of using [Stack Graphs](https://github.com/github/stack-graphs/) to index source code in language-agnostic way (based on tree-sitter) in order to get definitions.

Index example TypeScript source (installing all features to explore, but `--features cli` might be enough):
```
cargo install --all-features tree-sitter-stack-graphs-typescript
```

```
tree-sitter-stack-graphs-typescript index ../../examples/ts/src
```

Dump to JSON for further exploration, will crate `target/graph.json`.

```
cd dumper
cargo run
```

Explore in REPL using [main.clj](https://github.com/dundalek/dinodoc/blob/main/experiments/stack-graphs/src/dinodoc/stack_graphs/main.clj).

## Other utilities

Show the parse tree:
```
tree-sitter-stack-graphs-typescript parse ../../examples/ts/src/index.ts
```

Query for symbol definitions:
```
tree-sitter-stack-graphs-typescript query definition ../../examples/ts/src/index.ts:6:13
```

Clear the databse:
```
tree-sitter-stack-graphs-typescript clean --delete
```

## Structure of the database

- Index stored in sqlite database, on Linux in  `~/.local/share/tree-sitter-stack-graphs-typescript.sqlite` (see `default_user_database_path_for_crate`).
- Table `graphs` contains the per-file graphs which are stored as blobs encoded using Rust-specific `bincode` serialization.
