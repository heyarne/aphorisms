(ns aphorisms.nineteen
  (:require [quil.core :as q]
            [quil.middleware :as qm]
            [thi.ng.geom.rect :as r]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]))

(def bounds (r/rect 500 500))

(defn setup []
  (q/frame-rate 1)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/color-mode :hsb 360 100 100)
  (q/background 10 20 100)
  {})

(defn update-state [state]
  state)

(defn draw-state [state]
  (let [canvas (g/scale (:size bounds) 0.92)]
    (q/background 40 10 100)
    (q/no-fill)
    (q/with-translation [250 250]
      (doseq [i (range 100)
              :let [norm (* 0.01 i)
                    x (m/map-interval i [0 99] [(* -0.5 (:x canvas)) (* 0.5 (:x canvas))])
                    x-off (m/map-interval (q/sin (* norm q/PI)) [-1 1] [-50 50])]]
        (q/line (+ x x-off) -20 (+ x x-off) 20)))))


#_:clj-kondo/ignore
(q/defsketch eighteen
  :title "Eighteen"
  :size (:size bounds)
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  ;; :renderer :p2d
  :middleware [qm/pause-on-error qm/fun-mode])
