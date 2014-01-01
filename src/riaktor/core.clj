(ns riaktor.core
  (:gen-class)
  (:require [clojurewerkz.welle.core :as wc]
            [clojurewerkz.welle.buckets :as wb]
            [clojurewerkz.welle.kv :as kv]
            [clojure.core.async :refer [thread chan go]])
  (:use [org.httpkit.server]
        [hiccup.core]))



(defn init-riak-data [& {:keys [port]
                   :or {port 8087}}]
  (let [bucket "things"
        key "some-key"
        val (.getBytes "value")]
    (kv/store bucket key val)
    (kv/fetch bucket key)))


(defn init-riak []
  (wc/connect! "http://127.0.0.1:8087")
  (wb/create "things")
  (kv/store "things" "a-key" "value"))

(defn riak-connect! [& [port]]
  (wc/connect! (str "http://127.0.0.1:" (or port 8087))))

(defn riak-transaction [f & [port]]
  (go
   (riak-connect!)
   (f)
  ))

(defn riak-fetch [b k & [port]]
  (riak-connect!)
  (kv/fetch b k))

(defn gen-riak-html []
  (init-riak)
  (let [v (kv/fetch "things" "some-key")]
    [:h1 (.String v)]))

(defn gen-html []
  (html [:h1 "Hello there"] (gen-riak-html)))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (gen-html)})

(defn init-server [& {:keys [port]
                      :or {port 8090}}]
  (run-server app {:port port}))

(defn -main [& args]
  (let [http-port 8090
        riak-port 8087]
    (str "Now serving on port" http-port)
    (init-server app {:port http-port})))



""" experimental """

