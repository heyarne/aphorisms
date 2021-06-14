(ns aphorisms.eighteen
  (:require [quil.core :as q]
            [quil.middleware :as qm]
            [thi.ng.geom.vector :as v]
            [thi.ng.geom.rect :as r]
            [thi.ng.geom.core :as g]
            [thi.ng.math.core :as m]))

(def bounds (r/rect 500))

(defn arc [c start end]
  {:c c
   :start start
   :end end})

(defn radians [arc start-or-end]
  (g/angle-between (:c arc) (get arc start-or-end)))

(defn radius [arc]
  (m/mag (m/- (:c arc) (:start arc))))

(comment
  (def tmp (arc (v/vec2 0 0) (v/vec2 -100 0) (v/vec2 0 100)))

  (radians tmp :start) ;; => 3.141592653589793
  (radians tmp :end) ;; => 1.5707963267948966
  (radius tmp) ;; => 100.0

  )

;; a random sample inside bounds separates the space into four parts
;; i want to get the biggest part

(identity bounds)
;; => {:p [0.0 0.0], :size [500.0 500.0]}

(g/random-point-inside bounds)
;; => [3.722300840831416 95.03615495022028]


(defn subdivide-at [rect p]
  [(r/rect (:p rect) p)
   (r/rect (v/vec2 (:x p) (r/bottom rect)) (v/vec2 (r/right rect) (:y p)))
   (r/rect (v/vec2 (r/left rect) (:y p)) (v/vec2 (:x p) (r/top rect)))
   (r/rect (v/vec2 (:x p) (:y p)) (v/vec2 (r/top rect) (r/right rect)))])

(let [division bounds
      p (g/random-point-inside division)
      biggest-division (->> (subdivide-at bounds p)
                            (sort-by (comp - g/area))
                            (first))]
  [p
   biggest-division
   (-> (g/translate biggest-division (m/- p))
       (g/scale 0.66)
       (g/translate p))])

(defn reset-state [& _]
  (let [p (g/random-point-inside bounds)
        start (m/+ p (v/randvec2 50))
        end (-> (g/translate start (m/- p))
                (g/rotate (rand m/HALF_PI))
                (g/translate p))]
  {:arcs [(arc p start end)]}))

(defn setup []
  (q/frame-rate 1)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/color-mode :hsb 360 100 100)
  (q/background 10 20 100)
  (reset-state))

(defn update-state [state]
  state)

(defn draw-state [state]
  (q/background 10 20 100)
  (q/stroke 200 80 100)
  (q/no-fill)
  (doseq [{[x y] :c :as arc} (:arcs state)
          :let [start (radians arc :start)
                end (radians arc :end)]]
    (q/ellipse x y 5 5)
    (q/line (:c arc) (:start arc))
    (q/line (:c arc) (:end arc))
    (q/arc x y (radius arc) (radius arc) (min start end) (max start end))))

(defn origin-at-bottom-left
  "Make processing and thi.ng use the same coordinate system, with the orgin
  at the bottom left."
  [options]
  ;; note that this usually doesn't make any difference, but we're using
  ;; "r/bottom" and "r/top" here soooo… let's be consistent
  (update options :draw (fn [draw]
                          (fn [& args]
                            (q/translate 0 (get-in options [:size 1]))
                            (q/scale 1 -1)
                            (apply draw args)))))

#_:clj-kondo/ignore
(q/defsketch eighteen
  :title "Eighteen"
  :size (:size bounds)
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :mouse-pressed reset-state
  :draw draw-state
  ;; :renderer :p2d
  :middleware [qm/pause-on-error qm/fun-mode origin-at-bottom-left])
