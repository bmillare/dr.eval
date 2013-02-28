(ns dr.eval
  (:require [ring.adapter.jetty :as raj]
            [ring.util.response :as rur]
            [ring.middleware.multipart-params :as rmmp]))

(def example-server-options
  {:ssl? true :port 8999
   :ssl-port 9000 :join? false
   :keystore "path/to/keystore"
   :key-password "password"
   :configurator (fn [jetty]
                   (doseq [connector (.getConnectors jetty)]
                     (.setHeaderBufferSize connector
                                           ;; needs to be large so that I can send a lot of data via url
                                           1048576)))})

(def router
  (-> (fn [request]
        (-> request
            :multipart-params
            (get "eval")
            read-string
            eval
            str
            rur/response
            (rur/charset "UTF-8")))
      rmmp/wrap-multipart-params))

(defn ->server [options]
  (raj/run-jetty #'router
                 options))