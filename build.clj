(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'bsless/clj-norn)
;; (def version (format "0.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def java-paths [ "src/main/java" ])

(defn javac
  [& args]
  (b/javac {:src-dirs java-paths :class-dir class-dir}))

