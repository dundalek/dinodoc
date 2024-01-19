# Structurizr Architecture docs

Render architecture documentation with diagrams based on the [C4 model](https://c4model.com/) expressed in [Structurizr DSL](https://structurizr.com/).

Examples:
- [Big Bank plc](https://dinodoc.pages.dev/examples/structurizr/Big%20Bank%20plc-0/)
- [Financial Risk System](https://dinodoc.pages.dev/examples/structurizr/Financial%20Risk%20System-0/)

Use [[dinodoc.structurizr/generate]] with a path to Structurizr workspace: 

```clojure
(require '[dinodoc.structurizr :as structurizr]))

(structurizr/generate
 {:workspace-file "some/path/workspace.dsl"
  :output-path "docs"})
```

The [mermaid plugin](https://docusaurus.io/docs/markdown-features/diagrams) needs to be added to render diagrams.

## Rendered structure

- At the top-level we render a workspace with a list of systems and a system landscape diagram if is defined.
- Elements are nested in a sidebar hierarchy: workspace -> system -> container -> component
- On element page we show diagrams containing given element and also any associated deployment nodes.
- Diagrams are rendered using Mermaid and are clickable to navigate to element pages.

### Considerations

- IDs  
  Structurizr DSL uses human-specified IDs to refer to elements, but these are not available programmatically in the model.
  Instead auto-generated integer IDs are available.  
  We want to have human-readable URL slugs therefore element names are used, but those are not guaranteed to be unique.
  To make sure there are no conflicts, IDs are appended to names like `Internet%20Banking%20System-7`.  
  The concern is that these auto-ids might not be stable, which would make the URLs potentially unstable.
  In the future we should probably add custom slugification with disambiguation to avoid conflicts.
- Currently [people](https://docs.structurizr.com/dsl/language#person) are only rendered in diagrams. Would it be useful to render a list of users? What additional info would be useful to render for each user?
- Deployment diagrams are displayed on system element pages. However, deployment nodes can be organized in `deploymentNodes` hierarchy. Would it be useful to render it somehow?
- Model includes [tags](https://docs.structurizr.com/dsl/language#tags).
  We might render them on element page.
  Also could perhaps render a page that lists elements grouped by tags.
  However, this sounds like something that would be better served using a dynamic UI instead of a static one.
- Are [groups](https://docs.structurizr.com/dsl/language#group) just for visual appearance in diagrams or would it make sense to take groups into account when rendering the structure?
