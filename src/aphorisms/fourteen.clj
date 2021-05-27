(ns aphorisms.fourteen
  (:require [thi.ng.math.core :as m]
            [thi.ng.math.noise :as n]
            [quil.core :as q]
            [quil.middleware :as qm]))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb 360 100 100)
  (q/rect-mode :center)
  (q/background 350)
  {})

(defn update-state [state]
  (update state :attractor (partial drop 100)))

(def margin 40)

(defn draw-state [state]
  (let [x (* 0.5 (q/width))
        r 12]
    (q/background 180 10 90)
    (q/fill 180 10 90)
    (doseq [y (range (+ margin 32) (- (inc (q/height)) margin) (/ r 3))
            :let [yt (+ y (* 0.02 (q/millis)))
                  n (q/noise (* 0.05 yt))
                  size-l (+ r (* 64 n))
                  shrink-l (* m/PI (q/noise (* 0.07 yt)))
                  end-l (+ shrink-l (- m/PI))
                  size-r (+ r (* 64 (- 1 n)))
                  shrink-r (* m/PI (q/noise (* 0.06 yt)))
                  end-r (- 0 shrink-r)
                  x-off (* 20 n)]]
      ;; debug
      ;; (q/stroke 20)
      ;; (q/ellipse (+ x x-off) y 1 1)

      ;; draw the oddly moving ridge
      (q/stroke 320 50 20)
      (q/arc (- (+ x x-off) (* 0.5 size-l)) y size-l size-l end-l 0)
      (q/arc (+ (+ x x-off) (* 0.5 size-r)) y size-r size-r (- m/PI) end-r)

      ;; draw extensions on both sides
      (when (< end-l (* -5/8 m/PI))
        (q/stroke 180 10 80)
        (q/arc (- (+ x x-off) (* 0.5 size-l)) y size-l size-l (- end-l (* 70/360 m/PI)) (- end-l (* 50/360 m/PI))))

      (when (< end-r (* -5/8 m/PI))
        (q/stroke 180 10 80)
        (q/arc (+ (+ x x-off) (* 0.5 size-r)) y size-r size-r (+ end-r (* 50/360 m/PI)) (+ end-r (* 80/360 m/PI)))))))

(q/defsketch fourteen
  :title "Fourteen"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
