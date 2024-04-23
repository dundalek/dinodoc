(ns doc
  (:require
   [dinodoc.api :as dinodoc]
   [dinodoc.generator :as g]
   [dinodoc.impl.javadoc :as javadoc]
   [dinodoc.impl.rustdoc :as rustdoc]))

(defn make-resolve-link [generator-inputs]
  (fn [target]
    ;; TODO: handle multiple resolved candidates
    (some (fn [{:keys [generator output-path]}]
            (some->> (g/resolve-link generator target)
                     (str output-path "/")))
          generator-inputs)))

(let [output-path "docs"
      generator-inputs [{:output-path "javadoc"
                         :generator (javadoc/make-generator
                                     {:sourcepath "../java/src/main/java"
                                      :subpackages "demo"})}
                        {:output-path "rustdoc"
                         :generator (rustdoc/make-generator
                                     {:manifest-path "../rust/Cargo.toml"})}]
      resolve-link (make-resolve-link generator-inputs)]
  (dinodoc/generate
   {:inputs (cons
             {:path "."}
             ; nil)
             generator-inputs)
    :output-path output-path
    :resolve-apilink resolve-link}))

