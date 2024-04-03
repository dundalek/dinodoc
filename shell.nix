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
  ];
}
