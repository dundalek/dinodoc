(ns doc
  (:require
   [clojure.java.io :as io]))

(let [app (requiring-resolve 'example.server/app)]
  (io/copy
   (:body (app {:request-method :get :uri "/openapi.json"}))
   (io/file "reitit.json")))
