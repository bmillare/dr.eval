(ns dr.adapters
  (:require [ring.util.response :as rur]))

(defprotocol Iadapter
  (response [result content-type]))

(extend-type java.io.File
  Iadapter
  (response [result content-type]
    (-> result
        rur/response
        (rur/content-type content-type))))

(extend-type String
  Iadapter
  (response [result content-type]
    (-> result
        rur/response
        (rur/charset "UTF-8")
        (rur/content-type content-type))))

(extend-type Object
  Iadapter
  (response [result content-type]
    (-> result
        pr-str
        rur/response
        (rur/charset "UTF-8")
        (rur/content-type content-type))))

(defn default-adapter [result content-type]
  (response result content-type))

