(ns aphorisms.four
  (:require [thi.ng.geom.vector :as v]
            [thi.ng.math.core :as m]
            [quil.core :as q]
            [quil.middleware :as qm]))

(def padding 20)
(def width 500)
(def height 300)

(defn setup []
  (q/frame-rate 60)
  (q/color-mode :hsb 255 100 100)
  (q/smooth 8)
  {})

(defn update-state [state]
  state)

(def segments 50)
(def line-dist 10)
(def step-size 0.004)

(defn draw-state [state]
  (q/background 31 11 100)
  (q/no-fill)
  (q/stroke 316 12 95)
  ;; wavy wayves
  (dotimes [i 6]
    (q/begin-shape)
    (doseq [seg (range 51)]
      (let [range (- width padding padding)
            x (* seg (/ range segments))
            y (/ height 3)
            step (+ (q/map-range x 0 range 0 q/TWO-PI)
                    (* (q/millis) step-size)
                    (* i q/PI 1/8))]
        (q/vertex (+ padding x) (+ (+ y (* line-dist (Math/sin step)))
                                   (* i line-dist)))))
    (q/end-shape))
  ;; cubes
  (dotimes [i 3]
    (let [x 30
          y 30
          op (if (even? (quot (q/millis) 1000)) + -)]
      (when (<= i (quot (mod (q/millis) 1000) 75))
        (q/rect (+ x (* i 3))
                (op y (* i 6))
                30 30)))))


(q/defsketch four
  :title "mndsgn."
  :size [width height]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
