(set-env!
 :source-paths   #{"src/clj" "src/cljc" "src/cljs"}
 :resource-paths #{"html"}

 :dependencies '[[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [adzerk/boot-cljs "1.7.170-3"]
                 [pandeiro/boot-http "0.7.0"]
                 [adzerk/boot-reload "0.4.2"]
                 [adzerk/boot-cljs-repl "0.3.0"]
                 [com.cemerick/piggieback "0.2.1"]
                 [weasel "0.7.0"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [reagent "0.6.0-alpha2"]
                 [ring "1.4.0"]
                 ])

(task-options!
  pom {:project 'hangman
      :version "1.0.0-SNAPSHOT"}
  aot {:namespace '#{hangman.core}}
  jar {:main 'hangman.core})

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

(deftask run-cljs
  "Launch immediate feedback dev environment"
  []
  (comp
    (watch)
    (reload)
    (cljs-repl)
    (cljs)
    ))

(deftask run-clj []
  (with-pre-wrap fileset
    (require '[hangman.core :refer [dev-main]])
    ((resolve 'dev-main))
      fileset))

(deftask run []
 (comp (run-cljs) (run-clj)))

(deftask build-cljs []
 (comp (cljs :optimizations :advanced)))

(deftask build-clj []
 (comp (aot) (pom) (uber) (jar)))

(deftask build []
 (comp (build-cljs) (build-clj)))
