(ns dr.eval)

(defn eval-in-ns [code namespace]
  (binding [*ns* (the-ns (symbol namespace))]
    (-> code
        read-string
        eval)))

(defn eval-handler
  "
code must return a response

params must be provided
"
  [request]
  (let [{:keys [params]} request]
    (eval-in-ns (params "code")
                (params "namespace"))))