(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.javadoc :as javadoc]
   [dinodoc.rustdoc :as rustdoc]))

(dinodoc/generate
 {:inputs [{:path "."}
           {:output-path "javadoc"
            :generator (javadoc/make-generator
                        {:sourcepath "../java/src/main/java"
                         :subpackages "demo"})}
           {:output-path "rustdoc"
            :generator (rustdoc/make-generator
                        {:manifest-path "../rust/Cargo.toml"})}]
  :output-path "docs"})

