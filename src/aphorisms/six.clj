(ns aphorisms.six
  (:require [quil.core :as q]
            [quil.middleware :as qm]))

(def n-pendulums 20)

(defn setup []
  (let [start 100
        end 400]
    (q/frame-rate 30)
    (q/color-mode :hsb)
    ;; each pendulum is just an x and a y coordinate
    {:pendulums (mapv (fn [i]
                        {:pos [(/ (q/width) 2)
                               (+ start (* i (/ (- end start) n-pendulums)))]
                         :rad 0})
                      (range n-pendulums))}))

(defn update-state [state]
  state
  #_(update state :pendulums #(map
                             (fn [p]
                               (update p :rad (partial + (q/sin (/ (q/millis) 2000)))))
                             %)))

(defn draw-state [state]
  (q/background 240)
  (q/no-fill)
  (q/stroke 230)
  (doseq [{[x y] :pos} (:pendulums state)]
    (let [x (+ x (* 10 (q/sin (* 0.00002 y (q/millis)))))
          y (+ y (* 2 (q/sin (* 0.00002 y (q/millis)))))]
      (q/line 250 50 x y)))
  (q/stroke 190)
  (doseq [{[x y] :pos} (:pendulums state)]
    (let [x (+ x (* (* y 0.003 10) (q/sin (* 0.00002 y (q/millis)))))
          y (+ y (* 2 (q/sin (* 0.00002 y (q/millis)))))]
      (q/ellipse x y 5 5))))

(q/defsketch six
  :title ""
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
