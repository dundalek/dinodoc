# Antora integration

Uses [Antora utilities](https://dinodoc.pages.dev/docs/antora/) to render markdown files and API docs using Antora.

See the [rendered example](https://dinodoc.pages.dev/examples/antora/example/api/example/main/). Generate example docs with:

```sh
bb generate
```

Sample link to [[example.main/greet]] function in API docs.

## How the example works

1) The Antora generator uses `antora-playbook.yml` where sources are defined.
2) That resolves to a component described in `antora.yml` which uses the [collector-extension](https://gitlab.com/antora/antora-collector-extension) that executes the API docs generator and converts markdown files to asciidoc using `pandoc`.

We do this two-step process to be able to leverage functionality of Antora to fetch and collect content from multiple repos, where each can optionaly specify how its API docs are generated.
