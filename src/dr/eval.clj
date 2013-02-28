(ns dr.eval
  (:require [ring.adapter.jetty :as raj]
            [ring.util.response :as rur]
            [ring.middleware.multipart-params :as rmmp]
            [ring.middleware.stacktrace :as stacktrace]))

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

(defn eval-response [{:keys [process code content-type charset]}]
  (let [result (-> code
                   read-string
                   eval)]
    (case process
      "str" (-> result
                str
                rur/response
                (rur/charset charset)
                (rur/content-type content-type))
      "file" (-> result
                 rur/response
                 (rur/content-type content-type)))))

(defn wrap-password [handler password]
  (fn [request]
    (if (= ((:multipart-params request) "password")
           password)
      (handler request))))

(defn eval-handler [request]
  (let [params (-> request
                   :multipart-params)]
    (eval-response {:process (params "process")
                    :code (params "code")
                    :content-type (params "content-type")
                    :charset (params "charset")})))

(def router
  (-> eval-handler
      rmmp/wrap-multipart-params
      stacktrace/wrap-stacktrace))

(defn ->server [options]
  (raj/run-jetty (if-let [r (:router options)]
                   r
                   #'router)
                 options))