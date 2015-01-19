(defproject allgress/datomic-helpers "2.0.2"
  :description "Convenience functions to populate Datomic DB with data, to define DB schema."
  :url "https://github.com/allgress/datomic-helpers"
  :license {:name "MIT License" :url "file://./LICENSE"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha5"]
                 [org.clojure/clojurescript "0.0-2629"]
                 [com.cemerick/clojurescript.test "0.3.1"]
                 [datascript "0.7.2"]]
  :repositories [["s3" {:url "s3p://aurora-repository/releases/"
                        :username :env/aws_key
                        :passphrase :env/aws_secret
                        :sign-releases false}]
                 ["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username :env/datomic_username
                                    :password :env/datomic_password}]])
