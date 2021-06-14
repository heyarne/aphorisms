(ns aphorisms.sixteen
  (:require [thi.ng.math.core :as m]
            [thi.ng.geom.core :as g]
            [thi.ng.geom.vector :as v]
            [thi.ng.geom.line :as l]
            [thi.ng.math.noise :as n]
            [thi.ng.geom.rect :as r]
            [quil.core :as q]
            [quil.middleware :as qm]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb 360 100 100)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/background 180 10 90)
  {})


(defn update-state [state]
  state)

(defn grid [r divisions]
  (->>
   (g/subdivide r {:num divisions})
   (map (comp v/vec2 (juxt r/left r/top)))
   #_(take (inc (* divisions divisions)))))

(def grid-points
  (->> (g/subdivide (r/rect [72 72] [428 428]) {:num 25})
       (map g/edges)
       (apply concat)
       (apply concat)
       (dedupe)
       (map v/vec2)))

(defn line-crossing [v length]
  (l/line2 (m/- v (v/vec2 [(* length 0.5) 0]))
           (m/+ v (v/vec2 [(* length 0.5) 0]))))

(defn draw-state [state]
  (q/background 180 10 90)
  (q/no-fill)
  (doseq [[p1 p2 :as pt] grid-points
          :let [length (m/map-interval p2 [72 428] [3 18])
                [[x1 y1] [x2 y2]] (-> (line-crossing pt length)
                                      (g/translate (m/- pt))
                                      (g/rotate (+ m/QUARTER_PI
                                                   (* (n/noise2 (* 0.52 p1) (* 0.02 p2)) m/QUARTER_PI)))
                                      (g/translate pt)
                                      (g/vertices))]]
    (q/line x1 y1 x2 y2)
    (q/stroke-cap :round)
    (q/stroke-weight 1.2)
    (q/stroke 340 20 90)))

#_:clj-kondo/ignore
(q/defsketch sixteen
  :title "Sixteen"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :renderer :p2d
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
