# Dinodoc

Dinodoc is a documentation tool for Clojure that generates API documentation from source code with linked markdown articles.
It aims to enable creating a unified documentation hub within an organization.

Dinodoc is designed to generate documentation from multiple sources.
It supports documenting multiple projects in a monorepo or across repos,
and projects written in other languages besides Clojure.

## Features

It works on top of [Docusaurus](https://docusaurus.io/) which provides:

- Modern look with Light/Dark theme
- Include API documentation for non-Clojure languages like [TypeScript](https://github.com/tgreyuk/typedoc-plugin-markdown/tree/master/packages/docusaurus-plugin-typedoc)
- Document HTTP APIs of services with [OpenAPI](https://github.com/rohit-gohri/redocusaurus)
- Additional features like Search, Diagrams, Analytics, Blog
- Large ecosystem of [community plugins](https://docusaurus.io/community/resources)
- Complementary tools like [CMS intergration](https://github.com/tinacms/tinasaurus) to make writing docs easier for non-developers

## Examples

- [Promesa](https://dinodoc.pages.dev/examples/promesa/) ([source](https://github.com/dundalek/dinodoc/blob/main/examples/promesa/doc.clj))
  - single project with curated doc pages and API docs
  - compare with [codox](https://funcool.github.io/promesa/latest/) and [cljdoc](https://cljdoc.org/d/funcool/promesa/11.0.678/)
- [Reitit](https://dinodoc.pages.dev/examples/reitit/) ([source](https://github.com/dundalek/dinodoc/blob/main/examples/reitit/doc.clj))
  - monorepo with multiple modules
  - API docs combined into a single namespace hierarchy
- [Polylith](https://dinodoc.pages.dev/examples/polylith/) ([source](https://github.com/dundalek/dinodoc/blob/main/examples/polylith/doc.clj))
   - monorepo with components
   - API docs rendered separately for each component
- [Ring](https://dinodoc.pages.dev/examples/ring/) ([source](https://github.com/dundalek/dinodoc/blob/main/examples/ring/doc.clj))
  - mix of monorepo and multiple repos
  - modules in a main monorepo and additional modules in separate repositories
  - API docs rendered separately under each module
- [TypeScript](https://dinodoc.pages.dev/examples/ts/) ([source](https://github.com/dundalek/dinodoc/tree/main/examples/ts))
  - including documentation for projects written in other languages within a single site
  - for example TypeScript using [docusaurus-plugin-typedoc](https://github.com/tgreyuk/typedoc-plugin-markdown/tree/master/packages/docusaurus-plugin-typedoc)

## Usage

1. Create a new [Docusaurus](https://docusaurus.io/docs) site:  
   ```sh
   npx create-docusaurus@latest my-website classic
   ```
2. Add alias with dependencies to `deps.edn`:
   ```clojure
   {:aliases
    {:doc {:extra-deps {io.github.dundalek/dinodoc {:git/tag "v0.1.0" :git/sha "abb8482"}
           :main-opts ["doc.clj"]}}}
   ```
3. Create the script to generate docs and save as `doc.clj`:  
   Defaults are to use source files in `src/` to generate API docs and `doc/` for markdown pages.
   ```clojure
   (ns doc
     (:require [dinodoc.api :as dinodoc]))

   (dinodoc/generate
    {:inputs ["."]
     :output-path "docs"})
   ```
4. Generate markdown API docs:  
   `clojure -M:doc`
5. Render the site:  
   `npm start` to start dev-server in watch mode  
   `npm run build` to build the static site for deployment

## Documentation

- [Guide](https://dinodoc.pages.dev/docs/guide) describes options and configuration in more detail.
- [API Reference](https://dinodoc.pages.dev/docs/api/dinodoc/api/)

## Status

Alpha, there are issues to iron out and improvements to be made.
The main points at this stage are:

- Figure out the useful feature set.
- Possibly try out some experimental features with good leverage based on Living Documentation principles.
- Settle on good defaults that cover most common uses and figure out minimal required configuration options needed for customization.

## Credits

- Dinodoc started as a fork of [quickdoc](https://github.com/borkdude/quickdoc) and leverages [clj-kondo](https://github.com/clj-kondo/clj-kondo) for code analysis.
- Inspired by prior work done by [codox](https://github.com/weavejester/codox) and [cljdoc](https://github.com/cljdoc/cljdoc).
