
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
