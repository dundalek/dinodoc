with import <nixpkgs> { };
mkShell {
  buildInputs = [
    babashka
    bun
    clojure
    git

    # for antora example
    pandoc
    nodejs_20 # Antora broken with Node 22: https://antora.zulipchat.com/#narrow/stream/282403-announce-.F0.9F.93.A2/topic/Incompatibility.20with.20Node.2Ejs.2022

    # for dbschema example
    postgresql
    tbls

    # for contextmapper examples (dependency of plantuml)
    graphviz

    # for rust example
    cargo

    # for javadoc example
    jdk
  ];
}
