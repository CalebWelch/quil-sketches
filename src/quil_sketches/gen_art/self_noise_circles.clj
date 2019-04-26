(ns quil-sketches.gen-art.self-noise-circles
  (:require [quil.core :as q]
            [quil.helpers.drawing :refer [line-join-points]]
            [quil.helpers.calc :refer [mul-add]]
            [quil.helpers.seqs :refer [steps seq->stream range-incl tap]]))


(defn setup []
  (q/frame-rate 20)
  (q/smooth))

(defn draw-line-color [& line]
  (q/stroke-weight 10)
  (let [rads (q/sin (q/radians (q/frame-count)))
        color (if (neg? rads) 0 rads)]
    (q/stroke color (+ color 0.5) (q/random 0.5 1))
    (q/line (nth line 0) (nth line 1)
            (nth line 2) (nth line 3)))
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

      (println rads)
      ;Text Image
      (q/with-graphics image2
                       (q/color-mode :hsb 1)
                       (q/fill 1 0.1 1)
                       (q/background 0)
                       (q/text-align :center :center)
                       (q/text-size 100)
                       (q/text "L Y S" (/ (q/width) 2) (/ (q/height) 2)))
      (q/image image2 0 0)

      ;Background image
      (q/with-graphics image1
                       (q/color-mode :hsb 1)
                       (q/background 0)
                       (dorun (map #(apply draw-line-color %) line-args))
                       (q/blend-mode :overlay)
                       ;(q/mask-image image2)
                       )
      (q/image image1 0 0)
      ))
  )

(q/defsketch self-art
             :title "my shit"
             :setup setup
             :draw draw
             :size [800 800])