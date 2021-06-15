(ns aphorisms.twenty-one
  (:require [quil.core :as q]
            [quil.middleware :as qm]
            [thi.ng.geom.rect :as r]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]))

;; https://10print.org/

(def bounds (r/rect 500 500))

(defn setup []
  (q/frame-rate 30)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/color-mode :hsb 360 100 100)
  {})

(defn update-state [state]
  state)

(def canvas
  (as-> (g/scale bounds 0.92) b
    (g/translate b (m/* (m/- (:size bounds) (:size b)) 0.5))))

(def tile-size 10)

(defn draw-tile [[x y] tile-type]
  (let [start (case tile-type
                :a 0
                :b q/HALF-PI)]
    (q/arc x y tile-size tile-size start (+ start q/PI))))

(defn pick-at [xs rand-val]
  (nth xs (Math/round (* rand-val (dec (count xs))))))

(defn draw-state [_]
  (q/background 200 10 100)
  (q/no-fill)
  (q/stroke-weight 1)
  (q/stroke-cap :square)
  (q/stroke 10 60 100)
  (doseq [y (range (-> canvas :p :y) (+ (-> canvas :p :y) (-> canvas :size :y) tile-size) tile-size)
          x (range (-> canvas :p :x) (+ (-> canvas :p :x) (-> canvas :size :x) tile-size) tile-size)
          :let [n (q/noise (* 0.0000022 x (q/millis))
                           (* 0.0000033 y (q/millis)))]]
    (draw-tile [x y] (pick-at [:a :b] n))))

(g/scale bounds 0.92)

(g/scale (:size bounds) 0.92)

#_:clj-kondo/ignore
(q/defsketch twenty-one
  :title "Twenty-One"
  :size (:size bounds)
  :Settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [qm/pause-on-error qm/fun-mode])
