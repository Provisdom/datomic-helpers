(defproject provisdom/datomic-helpers "2.3.1"
  :description "Convenience functions to populate Datomic DB with data, to define DB schema."
  :url "https://github.com/allgress/datomic-helpers"
  :license {:name "MIT License" :url "file://./LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [datascript "0.15.0"]]
  :repositories [["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username :env/datomic_username
                                    :password :env/datomic_password}]])
