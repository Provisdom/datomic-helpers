(ns datomic-helpers
  (:require [datascript :as d]
            [allgress.datomic-helpers :refer [to-transaction]]))

(enable-console-print!)

(def db (d/empty-db {:shiz {:db.valueType :db.type/ref}}))
(def d (to-transaction [{:db/id 10 :shiz {:db/id 20 :db/ident :fleeb}} {:db/id 30 :shiz {:db/id 20 :db/ident :fleeb}}]))
(println (d/db-with db [{:db/id 10 :shiz {:db/id 20 :db/ident :fleeb}} {:db/id 30 :shiz {:db/id 20 :db/ident :fleeb}}]))
