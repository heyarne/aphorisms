(ns aphorisms.three
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def golden-ratio 1.61803)

(defn settings []
  (q/pixel-density (q/display-density)))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {})

(def update-state identity)

(defn draw-state [_]
  (q/background 240)
  (q/rect-mode :center)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]
    (doseq [i (range 1 7)]
      (let [side (/ 100 (* i golden-ratio))
            color (mod (+ 240 (/ 255 (* i golden-ratio))) 255)]
        (q/no-stroke)
        (q/fill color 255 255)
        (q/rect side side side side)))))

(q/defsketch aphorism-thrre
  :title "Three"
  :size [500 500]
  :settings settings
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode m/pause-on-error])
