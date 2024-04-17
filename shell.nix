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

    # for rust example
    cargo

    # for javadoc example
    jdk
  ];
}
