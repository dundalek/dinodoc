# ContextMapper

Example of rendering documentation for Domain Driven Design (DDD) model using [ContextMapper](https://contextmapper.org)
defined [CML](https://contextmapper.org/docs/language-reference/) model.

See the rendered [example](https://dinodoc.pages.dev/examples/contextmapper/insurance-map/).
Example of linking to a specific domain [[InsuranceDomain]] or a bounded context [[RiskManagementContext]].

### Considerations

- Teams in CML are modeled as type of Bounded Contexts, but I find clearer to treat and render them separately from contexts.
  - Teams are rendered on a single page since there is not much details available.
    If there is more content in the future then they can be rendered as separate pages.
- Currently not rendering:
  - user requirements, use cases, user stories.
  - partnership details
  - tactical elements like aggregates, entities, etc. (just shown in the context diagrams for now)
