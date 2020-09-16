(ns aphorisms.eleven
  (:require [thi.ng.geom.vector :as v]
            [thi.ng.geom.circle :as c]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]
            [thi.ng.math.noise :as n]
            [quil.core :as q]
            [quil.middleware :as qm]))
(defn setup []
  (q/frame-rate 1)
  (q/color-mode :hsb 360 1.0 1.0 1.0)
  {:shapes (repeatedly 20 #(-> (m/* (v/randvec2) (q/random 25 175))
                               (c/circle 20)))})

(defn update-state [state]
  state)

(defn lerp-color [c1 c2 t]
  (mapv + (map (partial * (- 1 t)) (cond-> c1
                                     (< (count c1) 4) (conj 1.0)))
        (map (partial * t) (cond-> c2
                             (< (count c2) 4) (conj 1.0)))))

(def color1 [180 0.5 0.6 0.5])
(def color2 [210 0.5 0.6])

(defn draw-state [state]
  (q/background 0 0 0.98)
  (q/no-fill)
  (q/no-stroke)
  (q/with-translation [(* 0.5 (q/width)) (* 0.5 (q/height))]
    (doseq [shape (:shapes state)]
      (q/begin-shape)
      (doseq [pt (:points (g/as-polygon shape 120))]
        (let [color (lerp-color color1 color2 (q/map-range (first pt)
                                                           (- (first (:p shape))
                                                              (:r shape))
                                                           (+ (first (:p shape))
                                                              (:r shape)) 0 1))]
          (apply q/fill color)
          (apply q/vertex pt)))
      (q/end-shape :close))))

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
