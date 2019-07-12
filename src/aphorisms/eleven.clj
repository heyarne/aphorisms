(ns aphorisms.eleven
  (:require [thi.ng.geom.vector :as v]
            [thi.ng.geom.circle :as c]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]
            [quil.core :as q]
            [quil.middleware :as qm]))


(defn setup []
  (q/frame-rate 1)
  (q/color-mode :hsb)
  {:shapes [(->
             (c/circle 60)
             (g/as-polygon 120))]})

(defn update-state [state]
  state)

(defn mix-hsb [[h1 s1 b1] [h2 s2 b2] t]
  (mapv + (map (partial * (- 1 t)) [h1 s1 b1])
        (map (partial * t) [h2 s2 b2])))

(def color1 [180 (* 255 0.5) (* 255 0.6)])
(def color2 [210 (* 255 0.5) (* 255 0.6)])

(defn draw-state [state]
  (q/background 0 0 (* 255 0.92))
  (q/no-fill)
  (q/no-stroke)
  (q/with-translation [(* 0.5 (q/width)) (* 0.5 (q/height))]
    (q/begin-shape)
    (doseq [shape (:shapes state)
            pt (:points shape)]
      (apply q/fill (mix-hsb color1 color2 (q/map-range (first pt) -25 25 0 1)))
      (apply q/vertex pt))
    (q/end-shape :close)))

(q/defsketch eleven
  :title "Eleven"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :renderer :p2d
  :draw draw-state
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
