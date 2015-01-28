;;;; Copyright (C) 2014 Anton Vodonosov (avodonosov@yandex.ru)
;;;; Extensions by Allgress Inc.
;;;; See LICENSE for details.

(ns allgress.datomic-helpers
  #+cljs (:require [datascript :as d]))

(declare tempid)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; TO-TRANSACTION

(def dbids (atom {}))

(defn- translate-value
  [v]
  ;; Returns a vector of two elements:
  ;; 1. The replacement for V (new :db/id value if V is a map,
  ;;    a vector with maps replaced by :db/id's if V is a vector, etc.)
  ;; 2. The sequence of maps which were replaced by their new :db/id's,
  ;;    each map already contains the :db/id.
  (letfn [(translate-values [values]
                            (let [mapped (map translate-value values)]
                              [(reduce conj [] (map first mapped))
                               (reduce concat '() (map second mapped))]))]
    (cond (map? v) (if-let [existing-id (some #(when (identical? v (first %)) (second %)) @dbids)]
                     [existing-id nil]
                     (let [id (tempid :db.part/user)
                           d (swap! dbids assoc v id)
                           translated-vals (translate-values (vals v))
                           translated-map (zipmap (keys v)
                                                  (first translated-vals))]
                       [id (cons (assoc translated-map :db/id id)
                                 (second translated-vals))]))
          (vector? v) (translate-values v)
          :else [#+clj v #+cljs (if (keyword? v) [:db/ident v] v) nil])))

(defn to-transaction [data-map]
  (reset! dbids {})
  (vec (second (translate-value data-map))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; TO-SCHEMA-TRANSACTION

(defn ext [extra-props type]
  (list 'ext type extra-props))

(defn- third [s]
  (second (rest s)))

(defn- strip-props [type]
  (loop [props {} inner-val type]
    (cond (vector? inner-val)
          (do (assert (= 1 (count inner-val)) (str "vector should only contain single element: " inner-val))
              (recur (assoc props :db/cardinality :db.cardinality/many)
                     (first inner-val)))
          (and (list? inner-val) (= 'ext (first inner-val)))
          (recur (merge props (third inner-val))
                 (second inner-val))
          :else
          [props inner-val])))

(defn to-schema-transaction- [type]
  (cond (map? type) (mapcat (fn [[attr val]]
                              (if-not (symbol? val)
                                (let [[extra-props inner-val] (strip-props val)]
                                  (cons (merge {:db/ident              attr
                                                :db/valueType          (if (or (map? inner-val)
                                                                               (set? inner-val))
                                                                         :db.type/ref
                                                                         inner-val)
                                                :db/cardinality        :db.cardinality/one ; just a default, may be overriden by extra-props
                                                :db.install/_attribute :db.part/db
                                                :db/id                 (tempid :db.part/db)}
                                               extra-props)
                                        (to-schema-transaction- inner-val)))))
                            (filter #(not= :db/ident (first %1))
                                    (seq type)))
        (set? type) (for [enum-val type]
                      {:db/ident enum-val
                       :db/id    (tempid :db.part/user)})))

(defn- cleanup-duplicates [attribute-specifications]
  (let [groups (group-by :db/ident attribute-specifications)]
    (for [attr (keys groups)]
      ;; ignore :db/id as it is a different temporary
      ;; ID we generated for each (probably otherwise equal)
      ;; attribute definition
      (if (apply not= (map #(dissoc %1 :db/id)
                           (groups attr)))
        (throw #+clj (IllegalArgumentException.
                       (str "Different definitions of attribute " attr " : "
                            (groups attr)))
               #+cljs (js/Error.
                        (str "Different definitions of attribute " attr " : "
                             (groups attr))))
        (first (groups attr))))))

(defn to-schema-transaction [type]
  (doall (cleanup-duplicates (to-schema-transaction- type))))

(defn to-schema
  [type]
  #+clj
  (to-schema-transaction type)

  #+cljs
  (let [s (filter :db.install/_attribute (to-schema-transaction type))]
    (into {} (map (fn [x] [(:db/ident x) (into {} (filter (fn [[k v]] (or (not (#{:db.install/_attribute :db/valueType} k))
                                                                          (and (= :db/valueType k) (= :db.type/ref v)))) x))]) s))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Datomic API

;;; We can't directly depend on datomic peer library,
;;; because we don't want to stick to concrete
;;; implementation (-pro or -free, nor to any particular version number)
;;; and datomic does not provide datomic.api as a component
;;; we can depend upon.
;;; Therefore we use reflection to invoke datomic.Peer/tempid
;;; method.
#+clj
(defn tempid
  [partition]
  (.invoke (.getMethod (Class/forName "datomic.Peer")
                       "tempid"
                       (into-array Class [Object]))
           nil
           (to-array [partition])))

#+cljs
(defn tempid
  [partition]
  (d/tempid partition))
;;;
;;; If you covnert huge amount of data and want to avoid
;;; reflection for speedup, just redefine the above
;;; function in your app, where Datomic is loaded already:
;;;
;;;   (defn datomic-helpers/tempid [partition]
;;;     (datomic.Peer/tempid partition))
