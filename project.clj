(defproject allgress/datomic-helpers "2.1.4"
  :description "Convenience functions to populate Datomic DB with data, to define DB schema."
  :url "https://github.com/allgress/datomic-helpers"
  :license {:name "MIT License" :url "file://./LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [org.clojure/clojurescript "0.0-2760"]
                 [com.cemerick/clojurescript.test "0.3.3"]
                 [datascript "0.9.0"]]
  :repositories [["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username :env/datomic_username
                                    :password :env/datomic_password}]

                 ["releases" {:url "http://archiva:8080/repository/internal"
                              :username :env/archiva_username
                              :password :env/archiva_password}]])
