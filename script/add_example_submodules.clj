#!/usr/bin/env bb
;; -*- clojure -*-
;; vim: set filetype=clojure:
(ns add-example-submodules
  (:require
   [babashka.process :refer [shell]]
   [clojure.string :as str]))

(def ring-repos
  (->> "https://github.com/ring-clojure/ring
https://github.com/ring-clojure/ring-accept
https://github.com/ring-clojure/ring-anti-forgery
https://github.com/ring-clojure/ring-codec
https://github.com/ring-clojure/ring-cps
https://github.com/ring-clojure/ring-defaults
https://github.com/ring-clojure/ring-headers
https://github.com/ring-clojure/ring-json
https://github.com/ring-clojure/ring-mock
https://github.com/ring-clojure/ring-session-timeout
https://github.com/ring-clojure/ring-spec
https://github.com/ring-clojure/ring-ssl
https://github.com/ring-clojure/ring-websocket-async"
       (str/split-lines)
       (map (fn [repo-url]
              (let [project-name (re-find #"[^/]+$" repo-url)]
                [repo-url (str "ring/projects/" project-name)])))))

(def repos
  [["https://github.com/ring-clojure/ring.wiki.git" "ring/wiki"]
   ["https://github.com/polyfy/polylith" "polylith/polylith"]
   ["https://github.com/funcool/promesa" "promesa/promesa"]
   ["https://github.com/metosin/reitit" "reitit/reitit"]])

(defn -main [& _args]
  (doseq [[repo-url path] (concat ring-repos repos)]
    (shell "git submodule add" repo-url (str "examples/" path))))

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
