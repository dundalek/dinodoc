
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
