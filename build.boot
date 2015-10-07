(set-env!
  :source-paths #{"src" "test"}
  :resource-paths #{"src"}
  :asset-paths #{}
  :repositories [["clojars" "http://clojars.org/repo/"]
                 ["maven-central" "http://repo1.maven.org/maven2/"]
                 ["my.datomic.com" {:url "https://my.datomic.com/repo"
                                    :username (System/getenv "DATOMIC_USERNAME")
                                    :password (System/getenv "DATOMIC_PASSWORD")}]]
  :dependencies '[[adzerk/boot-cljs "1.7.48-5" :scope "test"]
                  [adzerk/boot-reload "0.4.0" :scope "test"]
                  [pandeiro/boot-http "0.6.3" :scope "test"]
                  [allgress/boot-tasks "0.2.3" :scope "test"]
                  [com.datomic/datomic-pro "0.9.5130" :scope "test" :exclusions [org.apache.httpcomponents/httpclient]]])

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-reload :refer :all]
  '[allgress.boot-tasks :refer :all]
  '[pandeiro.boot-http :refer :all])

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
               (cljs)
               #_(reload)))