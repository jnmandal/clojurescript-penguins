(ns hangman.core
  (:require [reagent.core :as reagent]
            [clojure.string :as string]
            [hangman.images :as images]))

;; state and actions
(defonce app-state
  (reagent/atom
   {:penguin-count 5
    :guessed-letters []
    :word "clojure"
    }))

;; constant: a vector of lettes
(def letters (string/split "abcdefghijklmnopqrstuwvxyz" ""))

(defn word-contains? [letter]
  (contains? (set (string/split (:word @app-state) "")) letter))

(defn guess-letter! [letter]
    (do
      (apply swap! app-state update-in [:guessed-letters] conj letter)
      (when (not (word-contains? letter))
        (swap! app-state assoc-in [:penguin-count] (- (:penguin-count @app-state) 1)))))

(defn game-won? [guessed-letters word]
  (= (sort guessed-letters) (sort (string/split word ""))))

; TODO write this better
(defn unguessed-letters [guessed-letters]
  (filterv (fn [letter] (not (contains? (set guessed-letters) letter))) letters))

;; components
(defn letter-button [letter]
  [:button {:type "submit"
            :on-click (fn [e] (guess-letter! letter))} letter])

(defn letter-chooser [available-letters]
  [:ul {:class "letter-chooser"}
    (for [letter available-letters]
      ^{:key letter}
      [:li (letter-button letter)])])

(defn word-display [guessed-letters word]
  [:ul (for [l word]
    ^{:key l}
    [:li (if
      (contains? (set guessed-letters) l)
      [:span l]
      [:span "_"])])])

(defn victory-view []
  [:section {:id "victory"}
    [:h2 "🎉 You Win! 🎉"]])

;; full app
(defn app []
  [:main
    [:h1 "Hangman (as penguins)"]
    [:section {:id "display"}
      (images/penguins (:penguin-count @app-state))
      [word-display (:guessed-letters @app-state) (:word @app-state)]]
    (if (game-won? (:guessed-letters @app-state) (:word @app-state))
      [victory-view]
      [:section {:id "controls"}
        [letter-chooser (unguessed-letters (:guessed-letters @app-state))]])
    ])

;; render the app
(reagent/render
  [app]
  (.getElementById js/document "cljs-target"))
