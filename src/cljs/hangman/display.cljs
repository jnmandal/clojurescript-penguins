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

(defn scoreboard [penguin-count]
  (if (> penguin-count 0)
    [penguins penguin-count]
    [whale]))
