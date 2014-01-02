(ns riaktor.core
  (:gen-class)
  (:require [clojure.core.async :refer [thread chan go]]
            [taoensso.carmine :as car :refer (wcar)])
  (:use [org.httpkit.server]
        [hiccup.core]))

(def redis-server-conn {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar redis-server-conn ~@body))

(defn gen-redis-html []
  (let [key "foo"
        val "bar"]
    (wcar* (car/ping)
      (car/set key val)
      (let [get-val (car/get key)]
        [:h1 get-val]))))

(defn gen-html []
  (html [:h1 "Hello there"] (gen-redis-html)))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (gen-html)})

(defn init-server [& {:keys [port]
                      :or {port 8090}}]
  (run-server app {:port port}))

(defn -main [& args]
  (let [http-port 8090]
    (str "Now serving on port" http-port)
    (init-server app {:port http-port})))
