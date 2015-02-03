allgress/datomic-helpers
========================
Revision 2.1.1
--------------
1. to-schema modified to accept set of allowed-attributes

Revision 2.0.2
--------------
1. Filter out ref attributes from CLJS version of schema to avoid Datascript validation error.
2. Only make ref attribute to enums in CLJ (Datascript does not support :db/ident).

Revision 2.0.1
--------------
1. Track entity definitions by reference (Clojure identical? function) and replace with :db/id
if the same entity is used in multiple places.