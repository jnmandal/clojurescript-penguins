(ns hangman.core
  (:require [reagent.core :as reagent]
            [clojure.string :as string]
            [hangman.images :as images]))

(defonce app-state
  (reagent/atom
   {:penguin-count 5
    :guessed-letters []
    :word "clojure"
    }))

(defn guess-letter! [letter]
  (apply swap! app-state update-in [:guessed-letters] conj letter))

(def letters (string/split "abcdefghijklmnopqrstuwvxyz" ""))

; TODO write this better
(defn unguessed-letters [guessed-letters]
  (filterv (fn [letter] (not (contains? (set guessed-letters) letter))) letters))

;; components
(defn letter-button [letter]
  [:button {:type "submit"
            :on-click (fn [e] (guess-letter! letter))} letter])

(defn letter-chooser [letters]
  [:ul {:class "letter-chooser"}
    (for [letter letters]
      ^{:key letter}
      [:li (letter-button letter)])])

(defn word-display [word guessed-letters]
  [:ul (for [l word]
    ^{:key l}
    [:li (if
      (contains? (set guessed-letters) l)
      [:span l]
      [:span "_"])])])

;; full app
(defn app []
  [:main
    [:h1 "Hangman (as penguins)"]
    [:section {:id "display"}
      (images/penguins (:penguin-count @app-state))
      [images/whale]
      [word-display (:word @app-state) (:guessed-letters @app-state)]]
    [:section {:id "controls"}
      [letter-chooser (unguessed-letters (:guessed-letters @app-state))]]])

;; render the app
(reagent/render
  [app]
  (.getElementById js/document "cljs-target"))
