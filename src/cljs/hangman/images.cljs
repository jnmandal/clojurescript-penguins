(ns hangman.images
  (:require [reagent.core :as reagent]))

(defn penguin []
  [:div {:class "penguin"}])

(defn penguins [count]
  [:div
    (for [x (range count)]
      ^{:key x}
      [penguin])])

(defn whale []
  [:div {:class "whale"}])
