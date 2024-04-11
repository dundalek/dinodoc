# OpenAPI HTTP APIs

HTTP API documentation specified by [OpenAPI](https://www.openapis.org/) can be rendered using the [docusaurus-openapi-docs](https://github.com/PaloAltoNetworks/docusaurus-openapi-docs) plugin.

Examples:

- [Petstore](https://dinodoc.pages.dev/examples/openapi/petstore) - classic OpenAPI example ([spec](https://github.com/swagger-api/swagger-petstore/blob/master/src/main/resources/openapi.yaml))
- [Museum](https://dinodoc.pages.dev/examples/openapi/museum) - newer OpenAPI example ([spec](https://github.com/Redocly/museum-openapi-example/blob/main/openapi.yaml))

### Plugin Configuration

Follow the plugin [installation and configuraiton](https://github.com/PaloAltoNetworks/docusaurus-openapi-docs?tab=readme-ov-file#installation-existing-docusaurus-site) instructions. As an example you can also refer to a [diff](https://github.com/dundalek/dinodoc/commit/e704c7decfbeda80145be4fd0007bec7436a3bb0) adding the plugin to an existing site.

### Reitit routes

[Reitit](https://github.com/metosin/reitit) router library has [OpenAPI support](https://github.com/metosin/reitit/blob/master/doc/ring/openapi.md) and is able to generate spec which can be rendered as documentation.

Example:

- [Reitit](https://dinodoc.pages.dev/examples/openapi/reitit) - based routes in [code](https://github.com/metosin/reitit/blob/7e00de835d33460c2e4b19c6aca4df452f869528/examples/openapi/src/example/server.clj#L31-L110) and extracted with a [script](https://github.com/dundalek/dinodoc/blob/main/examples/openapi/doc.clj).

**⚠️ Warning**: Using `requiring-resolve` to access route data will result in code being evaluated, therefore it only needs to be run on trusted sources!

## Plugin comparison

There are other plugins for Docusaurus that render OpenAPI spec. Here is a brief overview:

- [docusaurus-openapi-docs](https://github.com/PaloAltoNetworks/docusaurus-openapi-docs)
  - This is the one chosen in the examples above.
  - Good default look, shows HTTP method badges in the sidebar.
- [redocusaurus](https://github.com/rohit-gohri/redocusaurus)
  - Wraps [redoc](https://github.com/Redocly/redoc) to be embedded inside Docusarus.
  - It seems to work well and supports many features, but it has a slightly different look from Docusaurus, it looks a bit out of place.
- [docusaurus-openapi](https://github.com/cloud-annotations/docusaurus-openapi)
  - It looks a bit too plain compared to the other plugins
