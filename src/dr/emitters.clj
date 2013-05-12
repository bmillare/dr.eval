(ns dr.emitters)

(defn pr-single-quote-str [s]
  (str "'"
       (.replaceAll (str s)
                    "'"
                    "\\'")
       "'"))

(defn async-send-eval
  "
makes server evaluate clojure code, logs code to be sent and result in browser console"
  [url code response-handler adapter content-type]
  (let [wrapped-code (str "(" adapter " " code " " (pr-str content-type) ")")]
    (str "goog.net.XhrIo.send('" url "', " (response-handler wrapped-code) ", 'POST', goog.uri.utils.buildQueryDataFromMap({'code':'" wrapped-code "','namespace':'user'}))")))