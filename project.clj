(defproject allgress/datomic-helpers "2.1.5"
  :description "Convenience functions to populate Datomic DB with data, to define DB schema."
  :url "https://github.com/allgress/datomic-helpers"
  :license {:name "MIT License" :url "file://./LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.clojure/clojurescript "0.0-3297"]
                 [datascript "0.11.2"]]
  :repositories [["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username :env/datomic_username
                                    :password :env/datomic_password}]

                 ["releases" {:url "http://archiva:8080/repository/internal"
                              :username :env/archiva_username
                              :password :env/archiva_password}]])
