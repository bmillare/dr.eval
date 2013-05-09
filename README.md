dr.eval
=======

REST-like clojure web REPL server

## Goal

* Allow clients to evaluate clojure code on the web server
* Transform evaluation result into a client consumable

## Design

* web clients must specify the clojure function + args, server response adapter, and client response handler
* this library provides the `eval-handler`, and utilities
