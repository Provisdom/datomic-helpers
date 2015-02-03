(set-env!
  :source-paths #{"src" "test"}
  :resource-paths #{"src"}
  :asset-paths #{}
  :wagons '[[s3-wagon-private "1.1.2"]]
  :repositories [["clojars" "http://clojars.org/repo/"]
                 ["maven-central" "http://repo1.maven.org/maven2/"]
                 ["s3" {:url "s3p://aurora-repository/releases/" :username (System/getenv "AWS_KEY") :passphrase (System/getenv "AWS_SECRET")}]
                 ["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username (System/getenv "DATOMIC_USERNAME")
                                    :password (System/getenv "DATOMIC_PASSWORD")}]]
  :dependencies '[[adzerk/boot-cljs "0.0-2760-0" :scope "test"]
                  [adzerk/boot-cljs-repl "0.1.8" :scope "test"]
                  [adzerk/boot-reload "0.2.4" :scope "test"]
                  [pandeiro/boot-http "0.6.1" :scope "test"]
                  [deraen/boot-cljx "0.2.2" :scope "test"]
                  [cljsjs/boot-cljsjs "0.4.6"]
                  [allgress/boot-tasks "0.1.9" :scope "test"]
                  #_[adzerk/bootlaces "0.1.8" :scope "test"]
                  [com.datomic/datomic-pro "0.9.5130" :scope "test" :exclusions [org.apache.httpcomponents/httpclient]]])

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-cljs-repl :refer :all]
  '[adzerk.boot-reload :refer :all]
  '[allgress.boot-tasks :refer :all]
  '[pandeiro.boot-http :refer :all]
  '[deraen.boot-cljx :refer :all])

(set-project-deps!)

(default-task-options!)

(deftask web-dev
         "Developer workflow for web-component UX."
         []
         (comp
               (asset-paths :asset-paths #{"html"})
               (serve :dir "target/")
               (watch)
               (speak)
               (cljx)
               (cljs-repl)
               (cljs)
               #_(reload)))

