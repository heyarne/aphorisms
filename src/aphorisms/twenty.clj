(ns aphorisms.twenty
  (:require [quil.core :as q]
            [quil.middleware :as qm]
            [thi.ng.geom.rect :as r]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]))

;; https://10print.org/

(def bounds (r/rect 500 500))

(defn setup []
  (q/frame-rate 1)
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
  (let [gap (* 0.3 tile-size)
        top (+ y gap)
        right (- (+ x tile-size) gap)
        bottom (- (+ y tile-size) gap)
        left (+ x gap)]
    (case tile-type
      :a (q/line left top right bottom)
      :b (q/line left bottom right top))))

(defn draw-state [state]
  (q/background 40 10 100)
  (q/no-fill)
  (q/stroke-weight 2)
  (q/stroke-cap :square)
  (q/stroke 10 20 100)
  (doseq [y (range (-> canvas :p :y) (+ (-> canvas :p :y) (-> canvas :size :y)) tile-size)
          x (range (-> canvas :p :x) (+ (-> canvas :p :x) (-> canvas :size :x)) tile-size)]
    (draw-tile [x y] (rand-nth [:a :b]))))

(g/scale bounds 0.92)

(g/scale (:size bounds) 0.92)

#_:clj-kondo/ignore
(q/defsketch twenty
  :title "Twenty"
  :size (:size bounds)
  :Settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :renderer :p2d
  :middleware [qm/pause-on-error qm/fun-mode])
