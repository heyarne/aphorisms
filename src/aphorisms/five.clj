(ns aphorisms.five
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; ---
;; agitation
;; ---

(defn denominators [n]
  (->> (range 1 (int (/ n 2)))
       (filter #(= 0 (rem n %)))))

(def width 500)
(def height 500)

(def margin 36)
(def n-lines 107)

(defn settings []
  (q/pixel-density (q/display-density)))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  (q/stroke 20)
  ;; we build up our lines as a grid
  {:lines (let [h (- height (* 2 margin))
                w (- width (* 2 margin))
                ys (->> (range n-lines)
                        (map #(+ margin (* % (/ h n-lines)))))
                xs (->> (range n-lines)
                        (map #(+ margin (* % (/ w n-lines)))))]
            (into {} (map (fn [y] [y xs]) ys)))})



(def update-state identity)

;; determines how fast the noise progresses
;; the higher the friction the slower the progression
(def friction 100)

;; how much influence does the noise have?
(def amplification 4)

(defn draw-state [state]
  (q/background 240)
  (doseq [[y xs] (:lines state)
          [x1 x2] (->> (interleave xs (rest xs))
                       (partition 2))]
    (q/line x1 (+ (* (q/noise (+ (/ (q/frame-count) friction) x1) y) amplification) y)
            x2 (+ (* (q/noise (+ (/ (q/frame-count) friction) x2) y) amplification) y))))

(q/defsketch aphorism-five
  :title "Agitation"
  :size [500 500]
  :settings settings
  :setup setup
  :update update-state
  :draw draw-state
  :renderer :p3d
  :features [:keep-on-top :no-bind-output]
  :middleware [m/fun-mode m/pause-on-error])
