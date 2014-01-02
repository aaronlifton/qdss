(defproject qdss "0.1.0-SNAPSHOT"
  :description "Q Distritrubed Storage Service"
  :url "https://github.com/aaronlifton/qdss"
  :license {:name "MIT"
            :url "https://github.com/aaronlifton/qdss/blob/master/LICENSE"}
  :dependencies [
    [org.clojure/clojure "1.5.1"]
    [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
    [http-kit "2.1.13"]
    [hiccup "1.0.4"]
    [com.taoensso/carmine "2.4.0"]
    ]
  :main ^:skip-aot qdss.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
