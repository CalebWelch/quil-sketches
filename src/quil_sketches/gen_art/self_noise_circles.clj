(ns quil-sketches.gen-art.self-noise-circles
  (:require [quil.core :as q]
            [quil.helpers.drawing :refer [line-join-points]]
            [quil.helpers.calc :refer [mul-add]]
            [quil.helpers.seqs :refer [steps seq->stream range-incl tap]]))


(defn setup []

  (q/no-loop)
  (q/smooth))

(defn draw-line-color [& line]
  (q/stroke-weight 10)
  (q/stroke (q/random 0.5 1) (q/random 0.5 1) (q/random 0.5 1))
  (q/line (nth line 0) (nth line 1)
          (nth line 2) (nth line 3))
  )

(defn draw []
  (let [xs (range-incl 0 (q/width) 0.1)
        rads (map q/radians (range))
        ys (map q/tan rads)
        ys (map #(q/pow % 3) ys)
        ys (mul-add ys 10 (/ (q/height) 2))
        line-args (line-join-points xs ys)]
    (let [image1 (q/create-graphics (q/width) (q/height))
          image2 (q/create-graphics (q/width) (q/height))]
      (q/with-graphics image1
                       (q/background 0)
                       (q/color-mode :hsb 1)
                       (q/background 0)
                       (dorun (map #(apply draw-line-color %) line-args)))
      (q/image image1 0 0)
      (q/with-graphics image2
                       (q/blend-mode :overlay)
                       (q/fill 255 255 255)
                       (q/text-align :center :center)
                       (q/text-size 40)
                       (q/text "L Y S" (/ (q/width) 2) (/ (q/height) 2)))
      (q/image image2 0 0)
      ))
  )

(q/defsketch self-art
             :title "my shit"
             :setup setup
             :draw draw
             :size [800 800])