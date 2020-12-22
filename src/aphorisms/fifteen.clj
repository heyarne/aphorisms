(ns aphorisms.fifteen
  (:require [thi.ng.math.core :as m]
            [thi.ng.geom.core :as g]
            [thi.ng.geom.physics.core :as p]
            [thi.ng.geom.vector :as v]
            [thi.ng.geom.rect :as r]
            [thi.ng.math.noise :as n]
            [quil.core :as q]
            [quil.middleware :as qm]))

(defn make-world []
  (let [[a b :as particles] [(-> (p/particle (v/vec2 250 250))
                                 (p/lock))
                             (p/particle (v/vec2 0 10) 10)]
        springs [(p/spring a b 100 0.25)]
        gravity (p/gravity (v/vec2 0 9.81))
        world-bounds (p/shape-constraint-inside (r/rect [500 500]))]
    (p/physics {:particles particles
                :springs springs
                :behaviors {:gravity gravity}
                :constraints {:bounds world-bounds}})))

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb 360 100 100)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/background 350)
  {:world (make-world)})

(def radius 10)

(defn register-mouse [state ev]
  (assoc state :mouse (v/vec2 ev)))

(defn unregister-mouse [state _]
  (dissoc state :mouse))

(defn key-pressed [state ev]
  (case (:key ev)
    :r (assoc state :world (make-world))
    state))

(defn hit-particle [{:keys [mouse world]}]
  (when mouse
    (let [r (* radius radius)]
      (->> (filter #(<= (g/dist-squared (p/position %) mouse) r) (:particles world))
           (first)))))

(defn move-particle-with-mouse [state]
  (cond-> state
    (:hit state)
    (update-in
      [:world :particles]
      (fn [particles]
        (mapv (fn [p]
                (cond-> p
                  (and (not (p/locked? p))
                       (= p (:hit state)))
                  (p/set-position (:mouse state))))
              particles)))))

(defn update-state [state]
  #_(prn (:mouse state))
  (->
   (update state :world p/timestep 1)
   (assoc :hit (hit-particle state))
   (move-particle-with-mouse)))

(defn draw-state [state]
  (q/background 180 10 90)
  (q/no-stroke)
  (doseq [particle (get-in state [:world :particles])]
    (let [[x y] (p/position particle)
          color (if (and (not (p/locked? particle))
                         (= particle (:hit state)))
                  [0 80 80]
                  [0 20 80])]
      (apply q/fill color)
      (q/ellipse x y radius radius))))

#_:clj-kondo/ignore
(q/defsketch fifteen
  :title "Fifteen"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :key-pressed key-pressed
  :mouse-pressed register-mouse
  :mouse-dragged register-mouse
  :mouse-released unregister-mouse
  :update update-state
  :draw draw-state
  :renderer :p2d
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
