
## machine

```mermaid
stateDiagram-v2
state lights {
[*] --> red
green --> yellow : timer
yellow --> red : timer
red --> green : timer
}
```

[source](https://github.com/dundalek/dinodoc/blob/main/example/statecharts.clj#L9-L9)

## regions

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

[source](https://github.com/dundalek/dinodoc/blob/main/example/statecharts.clj#L25-L25)

## dog-walk

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

[source](https://github.com/dundalek/dinodoc/blob/main/example/statecharts.clj#L38-L38)
