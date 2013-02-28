dr.eval
=======

REST-like clojure web REPL server

## Goal

* Simplicity

## Usage

```clojure
(def eval-server (dr.eval/->server {:ssl? true
                                    :port 8999
                                    :ssl-port 9000
                                    :join? false
                                    :keystore "path/to/keystore"
                                    :key-password "password"
				    :router (-> dr.eval/eval-handler
				                (dr.eval/wrap-password "password")
						ring.middleware.multipart-params/wrap-multipart-params}))
```

See `send.html` for example request, but basically you just need to
make a POST request with parameter eval. `dr.eval` will return the
result of evaluating the expression