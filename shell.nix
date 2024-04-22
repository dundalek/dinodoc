with import <nixpkgs> { };
mkShell {
  buildInputs = [
    babashka
    bun
    clojure
    git

    # for antora example
    pandoc

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
