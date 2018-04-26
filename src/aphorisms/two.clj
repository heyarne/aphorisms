(ns aphorisms.two
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
  {:color 0})

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (assoc state :color (mod (+ (:color state) color-progress) 255)))

(defn draw-state [state]
  (q/background 240)
  (q/no-fill)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (dotimes [i 18]
      (let [color (- (:color state) (* i color-progress))
            angle (- (/ (* q/TWO-PI (q/frame-count)) 60) (* i (/ q/PI 6)))
            x (* radius (q/sin angle))
            y (* radius (q/cos angle))]
        (q/stroke color 255 255)
        (q/line (* 0.6 x) (* 0.6 y) x y)))))

(q/defsketch aphorism-two
  :title "Two"
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
  :middleware [m/fun-mode m/pause-on-error])
