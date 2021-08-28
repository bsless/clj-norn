(ns clj-norn.impl.local
  (:import
   (java.lang ScopeLocal)))

(defn scope-local
  ([]
   (ScopeLocal/newInstance)))

(defn get-scope [^ScopeLocal s] (.get s))

(defmacro let-where
  [[sl expr] & body]
  `(ScopeLocal/where ~sl ~expr (fn* [] ~@body)))
