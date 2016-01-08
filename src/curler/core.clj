(ns curler.core
(:require [clojure.data.json :as json])
(require [clj-http.client :as client])

(:gen-class) )

(defn -main
  "with given parameters, this function will return a page from the league API call"
  [api_key summoner_name] 
  (println "posting http GET to league API for relevant data...")

;; first take the json from the api

(let [API_DATA (client/get (format "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s" summoner_name api_key))]
 ( spit "/tmp/test.json"  (json/write-str API_DATA))

;; slurp the json

( let [keyworded
  (json/read-str (slurp "/tmp/test.json") :key-fn keyword)]
  (println (keyworded :body))))


;; end main

)

