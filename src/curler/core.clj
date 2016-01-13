
(ns curler.core

(:require [clojure.data.json :as json])
(:require [clj-http.client :as client])
(:require [clojure.string :as strings])

(:gen-class) )


(defn by_summoner_name
  "return ID from summoner name"
  [api_key summoner_name] 

  (println "posting http GET to league API for relevant data...")

(let [api_data (json/write-str (client/get 
  (format "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s" summoner_name api_key)))]
  (let [parsed ((json/read-str api_data :key-fn keyword) :body)]
 	(let [body ((json/read-str parsed :key-fn keyword) (keyword summoner_name))]
 	(let [sum_id (body :id)](println "got id") sum_id))))
)

(defn query_game
	"use summoner ID from name to return collection of relevant game data  ...
	including other summoners in the game, but most importantly, the encryption key 
	for the observer stream cypher"
	[summoner_id api_key]
  (try
      (let [api_data (json/write-str (client/get 
        (format "https://na.api.pvp.net/observer-mode/rest/consumer/getSpectatorGameInfo/NA1/%s?api_key=%s" summoner_id api_key)))]
        (let [parsed ((json/read-str api_data :key-fn keyword) :body)]
          (let[body (json/read-str parsed :key-fn keyword)]  ;; not sure why this is necessary... seems like json datatype should persist through levels
              (let [[participants, game_id observer_data :as game_data]
                [(body :participants)
                (body :gameId)
                (body :observers)]]
         game_data))))
    (catch Exception e
      (print "EXCEPT:")
      (println "no active game")))
)


(defn get_chunk
  "download a chunk"
  [game_id chunk_num] 
  (client/get (format "http://spectator.na.lol.riotgames.com/observer-mode/rest/consumer/getGameDataChunk/NA1/%s/%s/token" game_id chunk_num)))
;;getting chunks isn't going to help us until we can decode the binary file that 
;;results from the decrypted / decompressed chunk


(defn observer
  "launch observer client"
  [api_key summoner_name]
  (try 
    (let [game_id (query_game (by_summoner_name api_key summoner_name) api_key)]
      (println game_id))
    (catch Exception e
      (prn "EXCEPT:"))))


(defn -main
  "put everything together, use the same arguments"
  [api_key summoner_name]
  (let [game_data (query_game (by_summoner_name api_key summoner_name) api_key)]
    (let [[participants game_id observers] game_data]
      ;;(println participants)  ;; this gives us a field will all the current game participants ala lolnexus
      (println )
      (let [base (slurp "resources/windows")]
          (spit "dev-resources/runme" (format "%s %s %s NA1\"" base (observers :encryptionKey) game_id))
          ))))