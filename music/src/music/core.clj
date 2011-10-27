(ns music.core)

(use 'overtone.core)
(boot-external-server)

(definst square-wave [freq 440] (square freq))

(def times (take 220 (iterate #(+ 30 %) 0)))

(defn change-pitch [t f inst] (at (+ t (now)) (ctl inst :freq f)))

(defn falling-pitches [start] (take (/ start 2) (iterate dec start)))
(defn rising-pitches [start] (take start (iterate inc start)))

(defn slide [pitches inst] (map (fn [x y] (change-pitch x y inst)) times pitches))

(square wave)
(slide (falling-pitches 440) square-wave)
(slide (rising-pitches 220) square-wave)
(stop)