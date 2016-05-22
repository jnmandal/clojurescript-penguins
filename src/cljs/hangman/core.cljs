(ns hangman.core
  (:require [reagent.core :as reagent]
            [clojure.string :as string]
            [hangman.images :as images]))

(defonce letters (string/split "abcdefghijklmnopqrstuwvxyz" ""))
(defonce guess-limit 5)

;; state and actions
(defonce app-state
  (reagent/atom
    ; TODO use set/list here
    {:guessed-letters []
    :word "clojure"}))

(defn word-contains? [letter]
  (contains? (set (string/split (:word @app-state) "")) letter))

(defn letter-guessed? [letter]
  (contains? (set (:guessed-letters @app-state)) letter))

(defn guess-letter! [letter]
  (swap! app-state update-in [:guessed-letters] conj letter))

(defn game-won? []
  (every? true?
    (map letter-guessed? (string/split (:word @app-state) ""))))

;; naive implementation example for demo
; (defn game-won? []
;   (=
;     (sort (:guessed-letters @app-state))
;     (sort (string/split (:word @app-state) ""))))

(defn correct-guesses []
  (filterv word-contains? (:guessed-letters @app-state)))

(defn penguins-left []
  (- guess-limit
    (-
      (count (:guessed-letters @app-state))
      (count (correct-guesses)))))

;; components
(defn letter-button [letter]
  [:button {:type "submit"
            :on-click (fn [e] (guess-letter! letter))} letter])

(defn letter-chooser [guessed-letters letters]
  [:ul {:class "letter-chooser"}
    (doall (for [letter letters]
      (when (not (letter-guessed? letter))
        ^{:key letter}
        [:li (letter-button letter)])))])

(defn word-display [guessed-letters word]
  [:ul
    (doall (for [letter word]
      ^{:key letter}
      [:li (if (letter-guessed? letter)
        [:span letter]
        [:span "_"])]))])

(defn victory-view []
  [:section {:id "victory"}
    [:h2 "🎉 You Win! 🎉"]])

(defn app []
  [:main
    [:h1 "Hangman (as penguins)"]
    [:section {:id "display"}
      [images/penguins (penguins-left)]
      [word-display (:guessed-letters @app-state) (:word @app-state)]]
    (if (game-won?)
      [victory-view]
      [:section {:id "controls"}
        [letter-chooser (:guessed-letters @app-state) letters]])])

;; render the app
(reagent/render
  [app]
  (.getElementById js/document "cljs-target"))
