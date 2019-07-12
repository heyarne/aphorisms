(ns aphorisms.nine
  (:require [quil.core :as q]
            [quil.middleware :as qm]))

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :hsb)
  {})

(defn update-state [state]
  state)

(defn draw-state [state]
  (let [half-width (* 0.5 (q/width))
        half-height (* 0.5 (q/height))
        top (+ 10 (- half-height))
        right (- half-width 10)
        bottom (- half-height 10)
        left (+ 10 (- half-width))]
    (q/background 240)
    (q/with-translation [half-width half-height]
      (dotimes [i 24]
        (q/rotate (* i (/ q/PI 360)))
        (q/stroke (+ 190 (* 2 i)) 120 250)
        (q/line (+ (* i 10) -100) (+ (* i 10) -100) 100 100)))))

(q/defsketch nine
  :title ""
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
