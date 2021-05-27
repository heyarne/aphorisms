(ns aphorisms.seventeen
  (:require [quil.core :as q]
            [quil.middleware :as qm]
            [thi.ng.geom.circle :as circle]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]
            [thi.ng.math.noise :as n]
            [thi.ng.geom.circle :as c]
            [thi.ng.geom.vector :as v]))

(def circ
  (c/circle 100))

(n/noise1 0.1)

(defn setup []
  (q/frame-rate 30)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/color-mode :hsb 360 100 100)
  (q/background 180 10 90)
  {:pointer (v/vec2 250 250)})

(defn update-state [state]
  state)

(defn circle [seed radius]
  (let [c-off-x (n/noise1 (* 0.0001 seed))
        c-off-y (n/noise1 (* -0.0004 seed))]
    (c/circle (* c-off-x (/ radius 3)) (* c-off-y (/ radius 3)) radius)))

(defn mix-hsb [hsb-a hsb-b x]
  (mapv + (map (partial * (- 1 x)) hsb-a) (map (partial * x) hsb-b)))

(defn draw-state [state]
  (let [fg-color [20 120 90]
        bg-color [180 5 90]]
    (q/background 180 5 90)
    (q/with-translation [250 250]
      (doseq [[idx radius] (reverse (map-indexed vector (range 20 150 10)))]
        (let [n-idx (/ idx 12)
              paint (if (zero? idx) q/fill q/stroke)
              {p :p r :r} (circle (+ (q/millis) (* idx 100)) radius)
              p (g/translate p (m/- (m/* (m/- p (:pointer state)) (* (- 1 n-idx) 0.05))))]
          (q/no-fill)
          (q/no-stroke)
          (apply paint (mix-hsb fg-color bg-color (/ (inc idx) 12)))
          (q/ellipse (:x p) (:y p) r r))))))

(let [center (v/vec2 250 250)
      mouse (v/vec2 100 100)]
  (g/translate center (m/*! (m/- mouse center) 0.1)))

(defn mouse-moved [state ev]
  (assoc state :pointer (v/vec2 ev)))

#_:clj-kondo/ignore
(q/defsketch seventeen
  :title "Seventeen"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-moved mouse-moved
  ;; :renderer :p2d
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
