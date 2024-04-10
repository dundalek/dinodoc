const Asciidoctor = require('asciidoctor')

const asciidoctor = Asciidoctor()

const registry = asciidoctor.Extensions.create()
require('./apilink-extension')(registry)

// var html = asciidoctor.convert('Hello, _Asciidoctor_')

var html = asciidoctor.convertFile('experiments/modules/ROOT/pages/custom.adoc', {
  to_file: false,
  standalone: false,
  extension_registry: registry,
})
console.log(html)

