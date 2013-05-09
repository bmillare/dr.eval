(ns dr.adapters
  (:require [ring.util.response :as rur]))

(defn default-adapter [result content-type]
  (case (str (type result))
    "class java.io.File" (-> result
                             rur/response
                             (rur/content-type content-type))
    "class java.lang.String" (-> result
                                 rur/response
                                 (rur/charset "UTF-8")
                                 (rur/content-type content-type))
    (-> result
        pr-str
        rur/response
        (rur/charset "UTF-8")
        (rur/content-type content-type))))

