(ns quil-sketches.gen-art.clouds
  (:require
    [quil.helpers.seqs :refer [steps seq->stream range-incl tap]]
    [quil.helpers.calc :refer [mul-add]]
    [quil.core :as q]))

(defn draw-points [x y noise]
  (q/push-matrix)
  (q/no-stroke)
  (q/translate x y)
  (let [grey (mul-add noise 120 150)
        alpha (mul-add noise 00 150)]
    (q/fill grey)
    (q/ellipse 0 0 30 10)
    (q/pop-matrix)))

(defn draw-all-points [x-start y-start]
  (let [step-size 5
        x-index (range-incl 0 (/ (q/width) step-size))
        y-index (range-incl 0 (/ (q/height) step-size))]
    (doseq [x-indexs x-index
            y-indexs y-index]
      (let [x (* step-size x-indexs)
            y (* step-size y-indexs)
            x-noise (mul-add x-indexs 0.01 x-start)
            y-noise (mul-add y-indexs 0.1 y-start)
            ]
        (draw-points x y (q/noise x-noise y-noise))))))

(defn draw []
  (let [
        image1 (q/create-graphics (q/width) (q/height))
        image2 (q/create-graphics (q/width) (q/height))
        artistTitle "A | S"
        text-size (/ (q/width) 3)
        ]

    (q/with-graphics image2
                     (q/color-mode :hsb 1)
                     (q/fill 0 0 1)
                     (q/stroke 0 0 1)
                     (q/text-align :center :baseline)
                     (q/text-size text-size)
                     (q/text-font (q/create-font "Microsoft YaHei UI Light" 100))
                     (q/text artistTitle (/ (q/width) 2) (/ (q/height) 2))
                     (q/text-size (/ text-size 4))
                     (q/text "H o m e c o m i n g" (/ (q/width) 2) (- (q/height) (/ (q/height) 4))))
    (q/image image2 0 0)

    (q/with-graphics image1
                     (let [start-str (q/state :stream)
                           [x-start y-start] (start-str)]
                       (draw-all-points x-start y-start))
                     (q/blend-mode :difference)
                     (q/mask-image image2)
                     )
    (q/image image1 0 0)
    )
  )

(defn setup []
  (q/frame-rate 40)
  (q/color-mode :hsb 255 255 255)
  (q/background 0)
  (q/smooth)
  (let [x-start (steps (q/random 10) 0.05)
        y-start (steps (q/random 10) 0.06)
        starts (map list x-start y-start)
        start-stream (seq->stream starts)]
    (q/set-state! :stream start-stream)))

(q/defsketch clouds
             :setup setup
             :draw draw
             :size [1080 1080])
