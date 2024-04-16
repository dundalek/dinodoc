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

    # for javadoc example
    jdk
  ];
}
