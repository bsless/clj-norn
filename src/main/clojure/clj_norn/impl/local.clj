(ns clj-norn.impl.local
  (:import
   (java.util.concurrent Callable)
   (java.lang ScopeLocal)))

(set! *warn-on-reflection* true)

(defn scope-local
  ([]
   (ScopeLocal/newInstance)))

(defn $
  {:inline (fn
             ([s] `(.get ~(with-meta s {:tag "java.lang.ScopeLocal"})))
             ([s else] `(.orElse ~(with-meta s {:tag "java.lang.ScopeLocal"}) ~else)))
   :inline-arities #{1 2}}
  ([^ScopeLocal s] (.get s))
  ([^ScopeLocal s else] (.orElse s else)))

(defmacro where
  {:style/indent 1}
  [[sl expr & bs] & body]
  (let [-body `(reify Callable (call [~'_] ~@body))]
    (if (seq bs)
      (let [bs (partition 2 bs)
            wheres (map (fn [[l e]] `(where ~l ~e)) bs)]
        `(.. (ScopeLocal/where ~sl ~expr)
             ~@wheres
             (call ~-body)))
      `(ScopeLocal/where ~sl ~expr ~-body))))

(comment
  (def a (scope-local))
  (def b (scope-local))
  (def c (scope-local))

  (defn foo [x y z]
    (where
        [a x
         b y
         c z]
      (+ ($ a) ($ b) ($ c))))

  (foo 1 2 3))
