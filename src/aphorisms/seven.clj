(ns aphorisms.seven
  (:require [thi.ng.geom.vector :as v]
            [thi.ng.math.core :as m]
            [quil.core :as q]
            [quil.middleware :as qm]))

(set! *warn-on-reflection* true)

(defmacro
  ^{:requires-bindings true
    :processing-name nil
    :category "Rendering"
    :added "1.7"}
  with-graphics
  "All subsequent calls of any drawing function will draw on given
  graphics. 'with-graphics' cannot be nested (you can draw simultaneously
  only on 1 graphics)"
  [graphics & body]
  `(let [^processing.core.PGraphics gr# ~graphics]
     (binding [quil.core/*graphics* gr#]
       (.beginDraw gr#)
       ~@body
       (.endDraw gr#))))

(defn fit-to-screen [^processing.core.PImage img]
  (if (> (.-width img) (.-height img))
    (.resize img (* (.-width img) (/ (q/height) (.-height img))) (q/height))
    (.resize img (q/width) (* (.-height img) (/ (q/width) (.-width img)))))
  img)

(defn setup []
  (let [mask (q/create-graphics (q/width) (q/height) :p3d)
        img-canvas (q/create-graphics (q/width) (q/height) :p3d)]
    (q/frame-rate 30)
    (q/color-mode :hsb)
    (q/image-mode :center)
    (with-graphics mask
      (q/background 0))
    (with-graphics img-canvas
      (let [img (-> (q/load-image "ghost-of-cassiopeia.png")
                    fit-to-screen)]
        (q/image-mode :center)
        (q/image img (* 0.5 (q/width)) (* 0.5 (q/height)))))
    {:img img-canvas
     :mask mask
     :theta 0}))

(defn update-state [state]
  state)

(defn mouse-moved [state ev]
  (assoc state :mouse ev))

(defn draw-state [state]
  (when (:mouse state)
    (with-graphics (:mask state)
      (q/background 0)
      (q/fill (int (* 255 (/ (get-in state [:mouse :x]) (q/width)))))
      (q/ellipse-mode :center)
      (q/ellipse (get-in state [:mouse :x]) (get-in state [:mouse :y]) 100 100)))
  (q/fill 221 40 250)
  (q/rect 0 0 (q/width) (q/height))
  (q/with-translation [(* 0.5 (q/width)) (* 0.5 (q/height))]
    (q/mask-image (:img state) (:mask state))
    (q/image (:img state) 0 0)))

(q/defsketch seven
  :title "Ghost of Cassiopeia"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :renderer :p3d
  :setup setup
  :update update-state
  :draw draw-state
  :mouse-moved mouse-moved
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
