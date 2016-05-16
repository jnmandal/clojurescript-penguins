(ns hangman.core
  (:require [reagent.core :as reagent]
            [clojure.string :as string]
            [hangman.images :as images]))

(defn letters []
  (string/split "abcdefghijklmnopqrstuwvxyz" ""))

(defn letter-button [letter]
  [:button {:type "submit"} letter])

(defn letter-chooser [letters]
  [:ul {:class "letter-chooser"}
    (for [letter letters]
      ^{:key letter}
      [:li (letter-button letter)])])

(defn app []
  [:main
    [:h1 "Hangman (as penguins)"]
    [:section {:id "display"}
      (images/penguins 5)
      [images/whale]]
    [:section {:id "controls"}
      [letter-chooser (letters)]]])

;; render the app
(reagent/render [app] (.getElementById js/document "cljs-target"))
