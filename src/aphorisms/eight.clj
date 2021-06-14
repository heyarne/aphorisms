(ns aphorisms.eight
  (:require [quil.core :as q]
            [quil.middleware :as qm]))

(def size 500)
(def padding 20)

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/smooth 16)
  {})

(defn update-state [state]
  state)

(defn draw-line [y]
  (let [p (* 0.0001 (q/millis))
        n (- (q/noise (+ p (* 0.005 y))) 0.5)
        w (- (* size 0.5) padding)]
    (q/with-translation [(* size 0.5) y]
      (q/with-rotation [(* q/PI n 0.25)]
        (q/stroke (+ 200 (* n 50)) 40 (* (q/map-range n -0.5 0.5 0.5 1) 230))
        (q/line (- w) 0 w 0)))))

(defn draw-state [state]
  (q/background 40)
  (q/no-fill)
  (q/stroke-weight 1.5)
  (doseq [y (range padding (- (q/height) (* padding 0.75)) (* padding 0.25))]
    (draw-line y)))

(q/defsketch eight
  :title "Eight"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :renderer :opengl
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
