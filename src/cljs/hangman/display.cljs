(ns hangman.display
  (:require [reagent.core :as reagent]))

(defn penguin []
  [:div {:class "penguin"}])

(defn penguins [count]
  [:div {:class "penguins"
         :style {:width (* count 75)}}
    (for [x (range count)]
      ^{:key x}
      [penguin])])

(defn whale []
  [:div {:class "whale"}])

(defn play-whale-sound []
  (.play (new js/Audio
    (rand-nth '("/sounds/orca1.mp3" "/sounds/orca2.mp3")))))

(defn scoreboard [penguin-count]
  (let [animation (reagent/atom false)]
    (reagent/create-class
      {:component-will-receive-props
        (fn []
          (do
            (reset! animation true)
            (play-whale-sound)
            (js/setTimeout #(reset! animation false) 1000)))
       :reagent-render
          (fn [penguin-count]
            (if (or (= penguin-count 0) @animation)
              [whale]
              [penguins penguin-count]))})))
