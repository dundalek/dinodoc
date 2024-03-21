use serde_json;
use stack_graphs::storage::SQLiteReader;
use std::fs;

fn main() -> Result<(), std::io::Error> {
    let file_to_dump = "../../../examples/ts/src/index.ts";

    // Tried to use `tree_sitter_stack_graphs::cli::database::default_user_database_path_for_crate("tree-sitter-stack-graphs-typescript")`
    // but it seems it is a private module.
    let db_path = concat!(
        env!("HOME"),
        "/.local/share/tree-sitter-stack-graphs-typescript.sqlite"
    );
    let file_path = std::env::current_dir()?.join(file_to_dump).canonicalize()?;

    let mut db = SQLiteReader::open(&db_path).expect("DB opened");
    db.load_graph_for_file(&file_path.to_string_lossy())
        .expect("Graph loaded");
    let (graph, _, _) = db.get();

    let json = serde_json::to_string_pretty(&graph.to_serializable())?;
    fs::create_dir_all("../target")?;
    fs::write("../target/graph.json", json)?;

    Ok(())
}
