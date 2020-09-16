(ns aphorisms.thirteen
  (:require [thi.ng.math.core :as m]
            [quil.core :as q]
            [quil.middleware :as qm]))

(def step-size 0.05)
(def delta 0)

(defn delta= [delta [ax ay] [bx by]]
  (< (+ (Math/abs (- ax bx)) (Math/abs (- ay by))) delta))

;; let's define some methods to generate different kinds of sequences

(defn lissajous [a b]
  (sequence
   (comp (map (partial * step-size))
         (map (juxt #(Math/sin (+ delta (* a %)))
                    #(Math/cos (* b %))))
         (take 1000 #_(Math/ceil (/ (* 2 Math/PI) step-size (* a b)))))
   (range)))

(defn sieve
  ([] (sieve (drop 2 (range))))
  ([xs]
   (let [prime (first xs)]
     (lazy-seq (cons prime (sieve (remove #(zero? (mod % prime)) xs)))))))

(defn de-jong-attractor [a b c d]
  (rest (iterate (fn [[x y]]
                   [(- (Math/sin (* a y)) (Math/cos (* b x)))
                    (- (Math/sin (* c x)) (Math/cos (* d y)))])
                 [0 0])))

(def primes (sieve))

(defn setup []
  (let [seed (->> (take 10 primes)
                  (shuffle)
                  (take 4))]
    (q/frame-rate 30)
    (q/color-mode :hsb 360 100 100 100)
    (q/background 20)
    {:attractor (->> #_(map #(/ % (apply max seed)) seed)
                     (apply de-jong-attractor seed)
                     (map (partial mapv #(* 125 %)))
                     (partition 4))}))

(def fibonacci (map first (iterate (fn [[a b]] [b (+ a b)]) [0N 1N])))

(nth fibonacci 10000)

(defn update-state [state]
  (update state :attractor (partial drop 100)))

(defn draw-state [state]
  (q/no-fill)
  #_(q/background 350)
  (q/with-translation [(* 0.5 (q/width)) (* 0.5 (q/height))]
    (q/stroke (+ 220 (mod (* (q/millis) 0.1) 80)) 60 90 1)
    (doseq [[a b c d] (take 100 (:attractor state))]
      (q/begin-shape)
      (apply q/curve-vertex a)
      (apply q/curve-vertex b)
      (apply q/curve-vertex c)
      (apply q/curve-vertex d)
      (q/end-shape))))

(q/defsketch twelve
  :title "Twelve"
  :size [500 500]
  :settings #(q/pixel-density (q/display-density))
  :setup setup
  :update update-state
  :draw draw-state
  #_#_ :renderer :p2d
  :features [:keep-on-top :no-bind-output]
  :middleware [qm/pause-on-error qm/fun-mode])
