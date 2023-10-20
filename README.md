# Dinodoc

Dinodoc is a documentation tool for Clojure that generates API documentation from source code with linked markdown articles.
It is designed to generate documentation from multiple sources.

It supports documenting multiple projects in a monorepo or across repos,
and projects written in other languages besides Clojure.
This enables to create a unified documentation hub within an organization.

## Features

It works on top of [Docusaurus](https://docusaurus.io/) which provides:

- Modern look with Light/Dark theme
- Include API documentation for non-Clojure languages like [TypeScript](https://github.com/tgreyuk/typedoc-plugin-markdown/tree/master/packages/docusaurus-plugin-typedoc)
- Document HTTP APIs of services with [OpenAPI](https://github.com/rohit-gohri/redocusaurus)
- Additional features like Search, Diagrams, Analytics, Blogging
- Large ecosystem of [community plugins](https://docusaurus.io/community/resources)


## Examples

- [Promesa](/doc.clj) ([view]())
  - single project with curated doc pages and API docs
  - compare with [codox](https://funcool.github.io/promesa/latest/) and [cljdoc](https://cljdoc.org/d/funcool/promesa/11.0.678/)
- [Reitit]() ([view]())
  - monorepo with multiple modules
  - API docs combined into a single namespace hierarchy
- [Polylith]() ([view]())
   - monorepo with components
   - API docs rendered separately for each component
- [Ring]() ([view]())
  - mix of monorepo and multiple repos
  - modules in a main monorepo and additional modules in separate repositories
  - API docs rendered separately under each module

## Status

Alpha, there are issues to iron out and improvements to be made.
figure out good defaults to work minimal configuration and figure out what configuration is needed to cover custom

## Usage

1) Create a new [Docusaurus](https://docusaurus.io/docs) site:  
   ```sh
   npx create-docusaurus@latest my-website classic
   ```
2) Add alias with dependencies to `deps.edn`:
   ```clj
   {:aliases
    {:doc {:extra-deps {dinodoc/dinodoc {:local/root "../../quickdoc"}
           :main-opts ["doc.clj"]}}}
   ```
3) Create the script to generate docs and save as `doc.clj`:  
   Defaults are to use source files in `src/` to generate API docs and `doc/` for markdown pages.
   ```clj
   (ns doc
     (:require [dinodoc.core :as dinodoc]))

   (dinodoc/generate
    {:paths ["."]
     :outdir "docs"
     :git/branch "main"
     :github/repo "https://github.com/your-org/your-repo"})
   ```
4) Generate markdown API docs:  
   `clojure -M:doc`
5) Render the site:  
   `npm start` to start dev-server in watch mode  
   `npm run build` to build the static site for deployment

## Documentation

- [Guide](doc/guide.md) describes options and configuration in more detail.
- [Design](doc/design.md) discusses design principles and considerations.
- [Reference]()

## Credits

- Dinodoc started as a fork of [quickdoc](https://github.com/borkdude/quickdoc) and leverages [clj-kondo](https://github.com/clj-kondo/clj-kondo) for code analysis.
- Inspired by prior work done by [codox](https://github.com/weavejester/codox) and [cljdoc](https://github.com/cljdoc/cljdoc).
