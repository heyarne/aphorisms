(ns aphorisms.ten
  (:require [thi.ng.geom.vector :as v]
            [thi.ng.math.core :as m]
            [thi.ng.math.noise :as n]
            [thi.ng.geom.core :as g]
            [thi.ng.geom.circle :as c]
            [quil.core :as q]
            [quil.middleware :as qm]))

(def circle (c/circle 60))
(def noise-step 0.01)
(def movement (v/vec2 1 0))
(def resolution 75)

(defn setup []
  (q/frame-rate 24)
  (q/color-mode :rgb)
  (q/background 240)
  {:noise-pos 0
   :circle-pos (v/vec2 (* (q/width) -0.5) 0)})

(defn update-state [state]
  (update state :noise-pos (partial + noise-step)))

(def noise-scale 0.3)

(defn disturb-circle
  [circle t]
  (let [disturbed (map (fn [[x y :as pt]]
                         (let [n (q/noise (+ t (* noise-scale x))
                                          (* noise-scale y))]
                           (m/* pt (q/map-range n 0 1 1 1.1))))
                       (:points (g/as-polygon circle resolution)))]
    (take (+ resolution 3) (cycle disturbed))
    #_(concat disturbed [(first disturbed) (second disturbed) (nth disturbed 2)])))

(def max-t 20)

(defn generate-circles [circle n max-offset]
  (->> (repeat n circle)
       (map-indexed
        (fn [idx circle]
          (->>
           (disturb-circle circle (* idx 0.4))
           ;; add offset
           (map (partial m/+ (v/vec2 (* (/ max-offset n) idx) 0)))
           ;; change size
           (map #(m/* % (q/sin (q/map-range idx (dec n) 0 q/HALF-PI q/QUARTER-PI)))))))))

(defn draw-state [state]
  (let [max-offset 100
        circles (generate-circles circle 20 max-offset)]
    ;; clear previous drawing
    (q/fill 240)
    (q/no-stroke)
    (q/rect 0 0 (q/width) (q/height))
    ;; draw new circle
    (q/translate (* (q/width) 0.5)
                 (* (q/height) 0.5))
    (q/translate (* max-offset -0.5) 0)
    (q/no-fill)
    (q/stroke 120)
    (doseq [circle circles]
      (q/begin-shape)
      (doseq [[x y] circle]
        (q/curve-vertex x y))
      (q/end-shape))))

(q/defsketch ten
  :title ""
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :renderer :p2d
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
