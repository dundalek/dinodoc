with import <nixpkgs> { };
mkShell {
buildInputs = [
  cargo
  graphviz
];
}
