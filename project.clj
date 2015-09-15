(defproject allgress/datomic-helpers "2.2.2"
  :description "Convenience functions to populate Datomic DB with data, to define DB schema."
  :url "https://github.com/allgress/datomic-helpers"
  :license {:name "MIT License" :url "file://./LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [datascript "0.12.1"]]
  :repositories [["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username :env/datomic_username
                                    :password :env/datomic_password}]

                 ["releases" {:url "http://archiva:8080/repository/internal"
                              :username :env/archiva_username
                              :password :env/archiva_password}]])
