(ns clojure-project.core)
(declare solve checkLine)
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "sudok!"))

;;solves a board entirely randomly. Solves the given board. 
(defn solveBoard
  [width height board]
  (let [
        randNum (+ 1 (rand-int (* width height)))
        vecFinal (solve board 0 randNum randNum width height (* width height))]

    (println vecFinal)
    vecFinal))

;;solves a board entirely randomly. This function solves a completely blank board. 
(defn main
  [width height]

  (let [my-vector (vec (take (* width height width height) (cycle [0])))
        randNum (+ 1 (rand-int (* width height)))
        vecFinal (solve my-vector 0 randNum randNum width height (* width height))]
    
    (println vecFinal)
    vecFinal
    )
)

;;generates a new random board with given height/width
;;randomness is used to set the difficulty of the board. Randomness around 1 results in a moderate board, with an easier board < 1 and harder > 1
;; Generates by randomly solving a blank board then replacing random parts with 0s
(defn generate
  [width height randomness]

  (let [my-vector (vec (take (* width height width height) (cycle [0])))
        randNum (+ 1 (rand-int (* width height)))
        vecFinal (solve my-vector 0 randNum randNum width height (* width height))]

    (loop [v vecFinal
                    remove (* randomness (* width height width height))]
               (if (> remove 0)
                 (let [randInt (rand-int (- (* width height width height) 1))
                       newVec (assoc v randInt 0)]
                   (recur newVec (- remove 1)))
                 v))
    ))

;; check if the box containing the given index is valid
(defn checkBox [board index checkedBox lineNum tryNum width height]
  (let [maxNum (* width height)
        lineStart (+ (* lineNum maxNum) (+ (* (quot index (* height maxNum)) (* height maxNum)) (* width (quot (mod index maxNum) width))))
        valid (if (= (nth board (+ lineStart checkedBox)) tryNum)
                false
                true)]

    (if (= (mod (+ checkedBox 1) width) 0)
      (if (= (+ lineNum 1) height)
        valid
        (and valid (checkBox board index 0 (+ lineNum 1) tryNum width height)))
      (and valid (checkBox board index (+ checkedBox 1) lineNum tryNum width height)))))

;; Check if the horizontal line containing the given index is valid
(defn checkHorizontalLine [board index checkedLine tryNum width height]
  (let [lineStart (* (* width height) (quot index (* width height)))
    valid (if (= (nth board (+ lineStart checkedLine)) tryNum)
      false
      true
      )]

    (if (= (+ checkedLine 1) (* width height))
      valid
      (and valid (checkHorizontalLine board index (+ checkedLine 1) tryNum width height))
    )
  )
)

;; check if the vertical line containing the given index is valid
(defn checkVerticalLine [board index checkedLine tryNum width height]
  (let [lineStart (mod index (* width height))
        valid (if (= (nth board (+ lineStart (* (* width height) checkedLine))) tryNum)
                false
                true)]

    (if (= (+ checkedLine 1) (* width height))
      valid
      (and valid (checkVerticalLine board index (+ checkedLine 1) tryNum width height))))
)

;; solve a given board
(defn solve [board index startNum currNum width height maxNum]
  
  (if (= index (* maxNum maxNum))
    board

  (if (= (get board index) 0)
    (let [modCurrNum (+ (mod currNum maxNum) 1)

          newBoard (assoc board index modCurrNum)]

      (if (and
           (checkVerticalLine board index 0 modCurrNum width height)
           (checkHorizontalLine board index 0 modCurrNum width height)
           (checkBox board index 0 0 modCurrNum width height))
        (let [randNum (+ 1 (rand-int maxNum))
              recurseBoard (solve newBoard (+ index 1) randNum randNum width height maxNum)]
          (if recurseBoard
            recurseBoard
            (if (= modCurrNum startNum)
              false
              (solve (assoc newBoard index 0) index startNum (+ currNum 1) width height maxNum))))
        (if (= modCurrNum startNum)
          false
          (solve (assoc newBoard index 0) index startNum (+ currNum 1) width height maxNum))))
    (let [randNum (+ 1 (rand-int maxNum))]
       (solve board (+ index 1) randNum randNum width height maxNum)))))