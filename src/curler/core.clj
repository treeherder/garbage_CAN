(ns curler.core
(:require [clojure.data.json :as json])
(require [clj-http.client :as client])

(:gen-class) )

(defn by_summoner_name
  "return ID from summoner name"
  [api_key summoner_name] 

  (println "posting http GET to league API for relevant data...")

(let [api_data (json/write-str (client/get (format "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s" summoner_name api_key)))]
  (let [parsed ((json/read-str api_data :key-fn keyword) :body)]
 	(let [body ((json/read-str parsed :key-fn keyword) (keyword summoner_name))]
 	(let [sum_id (body :id)](println "got id") sum_id))))
)

(defn query_game
	"use summoner ID from name to"
	[summoner_id api_key]
	(println "using id")
	(let [api_data (json/write-str (client/get (format "https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/%s?api_key=%s" summoner_id api_key)))]
		(let [parsed ((json/read-str api_data :key-fn keyword) :body)]
		parsed)))

(defn -main
  "put everything together, use the same arguments"
  [api_key summoner_name]
  (println (query_game(by_summoner_name api_key summoner_name) api_key)))