(ns stat-bot.core
  (:use [clojure.contrib.str-utils :only [re-split]]))

(defn- get-text-for-tag [node tag]
  (some #(when (= tag (:tag %)) (first (:content %)))
    (xml-seq node)))

(defn get-tweets [s]
  (map
    (fn [entry]
      {:content
       (get-text-for-tag entry :title)
       
       :author
       (str "@"
        (first (re-split #"\s+" (get-text-for-tag entry :name))))
       })
    (filter #(= :entry (:tag %)) (xml-seq (clojure.xml/parse (str "http://search.twitter.com/search.atom?itemPerPage=1&q=" s))))))

(defn search [s]
   (rest (map (comp first :content ) (filter #(= :title (:tag %)) (xml-seq (clojure.xml/parse (str "http://search.twitter.com/search.atom?&q=" s)))))))
   
(defn extract-tokens [token s]
  (map #(.toLowerCase %1) (filter #(.startsWith % token)(re-split #"[\s:'\?]+" s))))

(defn extract-token-stream [token s]
  (mapcat (partial extract-tokens token) s))

(defn tag-analysis [tag]
  (->> tag get-tweets 
    (map :content) 
    (extract-token-stream "#") 
    frequencies))
  
(defn twit-analysis [tag]
  (->> tag get-tweets
    (mapcat (juxt :content :author))
    (extract-token-stream "@")
    frequencies))
