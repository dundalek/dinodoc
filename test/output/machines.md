
## machine

An example of a simple lights machine.  
Based on https://lucywang000.github.io/clj-statecharts/docs/actions/#a-full-example

```mermaid
stateDiagram-v2
state lights {
[*] --> red
green --> yellow : timer
yellow --> red : timer
red --> green : timer
}
```

[source](https://github.com/dundalek/dinodoc/blob/main/example/statecharts.clj#L8-L8)

## regions

Example of parallel regions.  
Based on https://statecharts.dev/what-is-a-statechart.html#a-state-can-have-many-regions

```mermaid
stateDiagram-v2
state A {
state bc {
[*] --> B
B --> C : flick
C --> B : after-1s
}
state de {
[*] --> D
D --> E : flick
E --> D : flick
}
}
```

[source](https://github.com/dundalek/dinodoc/blob/main/example/statecharts.clj#L41-L41)

## dog-walk

Demonstrating nested states.  
Based on https://stately.ai/docs/state-machines-and-statecharts#parent-states

```mermaid
stateDiagram-v2
state dog_walk {
[*] --> waiting
waiting --> on_a_walk : leave-home
on_a_walk --> walk_complete : arrive-home
state on_a_walk {
[*] --> walking
walking --> running : speed-up
running --> walking : slow-down
}
}
```
