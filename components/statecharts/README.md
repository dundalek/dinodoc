# Statechart diagrams

Render logic modeled as state machines or [statecharts](https://statecharts.dev/
) represented in code using [clj-statecharts](https://github.com/lucywang000/clj-statecharts) library.

Statecharts are currently specified manually and rendered using [[dinodoc.statecharts/render-machine-var]]. 

See the example [doc.clj](https://github.com/dundalek/dinodoc/blob/main/examples/statecharts/doc.clj) recipe and the rendered [result](https://dinodoc.pages.dev/examples/statecharts/).

In the future a better API should be designed, perhaps collecting statecharts from a codebase automatically.

**⚠️ Warning**: Using `requiring-resolve` to access statechart machine data will result in code being evaluated, therefore it only needs to be run on trusted sources!

