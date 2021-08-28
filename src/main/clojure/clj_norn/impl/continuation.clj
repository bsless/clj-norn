(ns clj-norn.impl.continuation
  (:refer-clojure :exclude [run!])
  (:import
   (norn Util Generator)
   (java.lang Continuation)))

(definline yield [x] `(Util/yield ~x))
(definline create [f] `(Util/create ~f))

(defmacro continuation
  [& body]
  `(create (^{:once true} fn* [] ~@body)))

(defn generator* [f] (new Generator f))

(defmacro generator
  [& body]
  `(generator* (^{:once true} fn* [] ~@body)))

(defn iterable-seq
  [^Iterable it]
  (iterator-seq (.iterator it)))

(comment
  (take
   10
   (iterable-seq
    (generator
     (yield 0)
     (loop [a 0 b 1]
       (yield b)
       (recur b (+' a b)))))))

(defn done? [^Continuation c] (.isDone c))
(defn run! [^Continuation c] (.run c))
(defn preempted? [^Continuation c] (.isPreempted c))
(definline cc [] `(Util/cc))

(defn call-cc
  [f]
  (Util/get (doto (continuation (f yield)) run!)))

(comment
  (defn f [return] (return 2) 3)
  (f identity)
  (call-cc f))
