(ns stat-bot.test.core
  (:use [stat-bot.core])
  (:use [clojure.test]))

(deftest search-me ;; FIXME: write
  (is(not(empty? (search "clojure")))))
  
(deftest extract-tags-from-string
  (is(= '("#a" "#b")) (extract-tags "#a was her #b")))
  
(deftest extract-tags-from-stream
  (is(= '("#a" "#b" "#c" "#b") (extract-tags-stream ["#a was her #b" "and #c" "#b again"]))))

(deftest analysis-test
  (binding [get-tweets (constantly [{:content "#a was her #b"} {:content "and #c"} {:content "#b again"}])] 
    (is(= {"#a" 1 "#b" 2 "#c" 1} (tag-analysis "clojure" )))))

