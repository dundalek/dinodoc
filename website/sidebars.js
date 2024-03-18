/**
 * Creating a sidebar enables you to:
 - create an ordered group of docs
 - render a sidebar for each doc of that group
 - provide next/previous navigation

 The sidebars can be generated from the filesystem, or explicitly defined here.

 Create as many sidebars as you want.
 */

// @ts-check

/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
  // By default, Docusaurus generates a sidebar from the docs folder structure
  docSidebar: [{ type: 'autogenerated', dirName: 'docs' }],
  examplesPolylith: [{ type: 'autogenerated', dirName: 'examples/polylith' }],
  examplesPromesa: [{ type: 'autogenerated', dirName: 'examples/promesa' }],
  examplesReitit: [{ type: 'autogenerated', dirName: 'examples/reitit' }],
  examplesRing: [{ type: 'autogenerated', dirName: 'examples/ring' }],
  examplesStatecharts: [{ type: 'autogenerated', dirName: 'examples/statecharts' }],
  examplesStructurizr: [{ type: 'autogenerated', dirName: 'examples/structurizr' }],
  examplesTs: [{ type: 'autogenerated', dirName: 'examples/ts' }],
  dbschema: [{ type: 'autogenerated', dirName: 'examples/dbschema' }],

  kotlin: [{ type: 'autogenerated', dirName: 'examples/kotlin' }],
  java: [{ type: 'autogenerated', dirName: 'examples/java' }],

  openapi: [{
    type: "category",
    label: "Petstore",
    link: {
      type: "generated-index",
      slug: "/examples/openapi/petstore",
    },
    items: require("./docs/examples/openapi/petstore/sidebar.js"),
  }, {
    type: "category",
    label: "Museum",
    link: {
      type: "generated-index",
      slug: "/examples/openapi/museum",
    },
    items: require("./docs/examples/openapi/museum/sidebar.js"),
  }, {
    type: "category",
    label: "Reitit",
    link: {
      type: "generated-index",
      slug: "/examples/openapi/reitit",
    },
    items: require("./docs/examples/openapi/reitit/sidebar.js"),
  }],

  // But you can create a sidebar manually
  /*
    torialSidebar: [
    'intro',
    'hello',

      type: 'category',
      label: 'Tutorial',
      items: ['tutorial-basics/create-a-document'],
    },
   ,
   */
};

module.exports = sidebars;
