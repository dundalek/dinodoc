# Guide

### Input options

[[dinodoc.api/generate]] can generate a single documentation site from multiple inputs passed as a collection of `:inputs`.

An input represents a component, a module or a project which is a subdirectory that can include source code in `src/` and markdown articles in `doc/`.


Input can be specified as a *path* (any coercible value such as string, Path or File instances) or a *map* with `:path` and other options.

For example `{:inputs ["foo"]}` is equivalent to `{:inputs [{:path "foo"}]}`.

### Global options

There are *common options* that can be specified globally, but can be overridden for each input for flexibility.
Following two examples are the same, the second is shorter by using common `:git/branch` option globally.

Specifying options for each input individually:

```clj
(dinodoc/generate
  {:inputs [{:path "foo"
             :git/branch "main"
             :github/repo "https://github.com/org/foo"}
            {:path "bar"
             :git/branch "main"
             :github/repo "https://github.com/org/bar"}]})
```

Specifying common options globally:

```clj
(dinodoc/generate
  {:inputs [{:path "foo"
             :github/repo "https://github.com/org/foo"}
            {:path "bar"
             :github/repo "https://github.com/org/bar"}]
   :git/branch "main"})
```

### Monorepo with multiple modules 

The [reitit]() example demonstrates documentation for a single monorepo with multiple modules.

1) The root path includes `doc/` directory with markdown articles.
2) Inputs for modules are specified by concating the result of `fs/list-dir`. Since these use default convention they will be coerced to inputs.
3) `:api-docs` is set to `:global` which will collect namespaces from all inputs into a single API hierarchy.

```clj
(require '[babashka.fs :as fs])

(dinodoc/generate
 {:paths (concat
          [{:path "." ;; (1)
            :include-readme? false}]
          (fs/list-dir "modules")) ;; (2)
  :outdir "docs"
  :api-docs :global ;; (3)
  :git/branch "master"
  :github/repo "https://github.com/metosin/reitit"})
```

The [Polylith]() example shows a monorepo where a separate API hierarchy is rendered for each component.

1) Polylith repo contains `doc/` directory with markdown files, but it does not contain `cljdoc.edn` for curation so `doc-tree` is defined manually.
2) Passing curated `doc-tree` to the input.
3) Rendering all components in top-level would look crowded, so inputs passed as maps with customized `:outdir` option to add nesting under `Components` directory.

In the future it might be useful to have an integration with Polylith that would automatically handle projects, bases and components.

```clj
(def doc-tree [["Polylith" {:file "doc/readme.md"}]]) ;; (1)

(dinodoc/generate
 {:paths (concat
          [{:path "."
            :doc-tree doc-tree}] ;; (2)
          (->> (fs/list-dir "components")
               (map (fn [path]
                      {:path path
                       :outdir (str "Components/" (fs/file-name path))})))) ;; (3)
  :outdir "docs"
  :git/branch "master"
  :github/repo "https://github.com/polyfy/polylith"})
```

### Image assets

Currently Dinodoc copies over only markdown files, so any other assets like images need to be copied manually, for example using [babashka.fs](https://github.com/babashka/fs).

In the future it would probably be useful if Dinodoc detected referenced image assets and copied them over automatically.

```clj
(require '[babashka.fs :as fs])

(dinodoc/generate ...)

(fs/copy-tree "polylith/images" "docs/images")
```

### Curating documentation articles

TODO

To curate articles support for `:cljdoc.doc/tree` `doc/cljdoc.edn`
`doc-tree` to input

[cljdoc](https://github.com/cljdoc/cljdoc/blob/master/doc/userguide/for-library-authors.adoc#configuring-articles)

On a team it is easy 
adding remaining files
  easy to forget to add entry to the doc-tree
  collect remaining ones to render them
  can be noticed and reordered

## Docusaurus Configuration Tips

Here are some tips of useful configuration and plugins to add an extra touch to the default Docusaurus template.

#### Clojure syntax highlighting  

By default Clojure is not included in the [supported languages](https://docusaurus.io/docs/markdown-features/code-blocks#supported-languages) to highlight code blocks.
To add Clojure support add following to `docusaurus.config.js`:  
```js
themeConfig: { prism: { additionalLanguages: ['clojure'] }},
```

#### Mermaid diagrams

Add [@docusaurus/theme-mermaid](https://docusaurus.io/docs/markdown-features/diagrams) to render [Mermaid](https://mermaid.js.org/) diagrams.

#### Theme colors

To modify the default green color to match your brand you can [generate a palette](https://docusaurus.io/docs/styling-layout#styling-your-site-with-infima) and replace variables in `custom.css`.

#### Docs-only mode

In case you don't need blog and other content pages you can configure [Docs-only mode](https://docusaurus.io/docs/docs-introduction#docs-only-mode) by adding `routeBasePath: '/'` to the `docs` preset so that docs become the default landing page.

#### Search

Docusaurus offers various [search options](https://docusaurus.io/docs/search), there are two main categories:
- Local Search - Easy to start with as it does not require any external services.
  It works by downloading a statically generated index and performing the search client-side.  
  The [docusaurus-search-local](https://github.com/easyops-cn/docusaurus-search-local) plugin seems to works well.
  However, for a larger documentation sites using a search provider might be a better option.
- Search Providers - For the best experience one can use dedicated search providers like Algolia or Typesense.

#### Hideable sidebar

Configure the ability to [hide the sidebar](https://docusaurus.io/docs/sidebar#hideable-sidebar) by enabling the `themeConfig.docs.sidebar.hideable` option.

#### Starting Docusaurus without opening browser

Starting the process with `npm start` also opens the site in a browser.
This can be annoying since it opens another browser tab on each restart.
To disable it add the `--no-open` option in `package.json`:

```js
"scripts": {
  "start": "docusaurus start --no-open",
},
```
