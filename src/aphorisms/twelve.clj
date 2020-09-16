(ns aphorisms.twelve
  (:require [thi.ng.math.noise :as n]
            [quil.core :as q]
            [quil.middleware :as qm]))

(def noise-start 10.5)
(def noise-step 0.01)
(defn noise-seq [offset]
  (map #(n/noise1 (+ offset noise-start (* % noise-step))) (range)))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb 360 100 100 100)
  {:noise-move 0})

(defn update-state [state]
  (update state :noise-move (partial + 0.01)))

(defn draw-state [state]
  (q/background 350)
  (q/translate (* 0.5 (q/width)) (* 0.5 (q/height)))
  (doseq [r (take 100 (noise-seq (:noise-move state)))]
    (q/stroke (q/map-range (* r r) 0 1 270 255) 35 100 10)
    (q/with-rotation [(q/map-range r 0 1 0 q/QUARTER-PI)]
      (q/with-translation [(q/map-range (* r r) 0 1 -75 50) (* -0.5 (q/height))]
        (q/line 0 10 0 (- (q/height) 10))))))

(q/defsketch twelve
  :title "Twelve"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :renderer :p2d
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
