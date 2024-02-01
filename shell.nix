with import <nixpkgs> { };
mkShell {
  buildInputs = [
    babashka
    bun
    clojure
    git

    # for dbschema example
    postgresql
    tbls
  ];
}
