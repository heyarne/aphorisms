(ns aphorisms.one
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def radius 75)
(def color-progress 1.7)

(defn settings []
  (q/pixel-density (q/display-density)))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) color-progress) 255)})

(defn draw-state [state]
  (q/background 240)
  (q/no-fill)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (dotimes [i 12]
      (let [color (mod (+ (:color state) (* (/ i 12) color-progress)) 255)
            angle (* i (/ q/TWO-PI 12))
            x (* radius (q/sin angle))
            y (* radius (q/cos angle))]
        (q/stroke color 255 255)
        (q/ellipse x y (* 2 radius) (* 2 radius))))))

(q/defsketch aphorism-one
  :title "One"
  :size [500 500]
  :settings settings
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
