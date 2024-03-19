# Minimal Java example

Uses the [Dokka](https://dinodoc.pages.dev/docs/dokka/) engine.
See the rendered [result](https://dinodoc.pages.dev/examples/java/).
Note that `org.jetbrains.dokka/kotlin-as-java-plugin` needs to be added as a dependency to render signatures in the Java format.

Run the example code:
```
mkdir target
javac src/main/java/demo/*.java -d target && java -cp target demo.Main
```
