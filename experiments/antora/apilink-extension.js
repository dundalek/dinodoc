
// dummy example implementation
function lookup(target) {
  return {
    target: 'apimod:' + target.replace(/\//, '#').replace(/&gt;/, '-GT-'),
    label: target,
  }
}

module.exports = function(registry) {
  registry.inlineMacro('api', function() {
    this.process((parent, node) => {
      const { target, label } = lookup(node)
      var text = 'xref:' + target + '[' + label + ']'
      return this.createInline(parent, 'quoted', text)
    })
  })
}
