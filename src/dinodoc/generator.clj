(ns dinodoc.generator)

(defprotocol Generator
  "The `Generator` protocol defines an abstraction for a source that generates documentation and resolves links to entities.

Lifecycle:
1. `prepare-index` is called once at the beginning.

   The generator can parse or analyze the source at this stage to be able to serve subsequent `resolve-link` calls.
2. `resolve-link` can be called zero or many times.
3. `generate` is called once at the end.

   The generator writes its output to a given location. The generator can rely on the index to generate the output."

  (prepare-index
    [_] "Prepare the index for subsequent `resolve-link` calls.")
  (resolve-link
    [_ target] "Return a relative link given a logical target or `nil` if there is no match.")
  (generate
    [_ opts] "Passed options:

- `:output-path` - location where output should be written"))
