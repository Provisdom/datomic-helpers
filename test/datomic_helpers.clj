 (ns datomic-helpers
  (:require [allgress.datomic-helpers :refer :all])
  (:use clojure.test))

 (comment ;; try it
  (translate-value {:a 1 :b 2 :c {:subA 11 :subB 12}})
  (translate-value [{:a 1 :b 2}])
  )

 (with-test



  (is (= [{} :db/keyword]
         (strip-props :db/keyword)))

  (is (= [{:db/cardinality :db.cardinality/many}
          :db/keyword]
         (strip-props [ :db/keyword ])))

  (is (= [{:db/index true}
          :db/keyword]
         (strip-props (ext {:db/index true} :db/keyword))))

  (is (= [{:db/index true :db/cardinality :db.cardinality/many} :db/keyword]
         (strip-props [(ext {:db/index true} :db/keyword)])))
  (is (= [{:db/index true :db/cardinality :db.cardinality/many} :db/keyword]
         (strip-props (ext {:db/index true} [ :db/keyword ])))))

