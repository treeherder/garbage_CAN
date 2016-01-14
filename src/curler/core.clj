
(ns curler.core

(:require 
  [clojure.data.json :as json] 
  [clj-http.client :as client])
(use [clojure.java.shell :only [sh]])
(:gen-class) )


(defn by_summoner_name
  "return ID from summoner name"
  [api_key summoner_name] 

  (println "posting http GET to league API for relevant data...")
  (try
    (let [api_data (json/write-str (client/get 
      (format "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s" summoner_name api_key)))]
      (let [parsed ((json/read-str api_data :key-fn keyword) :body)]
 	      (let [body ((json/read-str parsed :key-fn keyword) (keyword summoner_name))]
 	        (let [sum_id (body :id)](println (format "got id  %s" sum_id)) sum_id))))
  (catch Exception e
    (print "EXCEPT:")
    (println "no summoner by that name, try removing spaces")))  ;;this needs a method to check for spaces in the string

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


(defn last_chunk
  "read data from last chunk call to get current keyframe and chunks"
  [game_id]
  (try
      (let [api_data (json/write-str (client/get 
        (format "http://spectator.na.lol.riotgames.com/observer-mode/rest/consumer/getLastChunkInfo/NA1/%s/1/token" game_id)))]
        (let [parsed ((json/read-str api_data :key-fn keyword) :body)]
              parsed))
    (catch Exception e
      (print "EXCEPT:")
      (println "last frame not saved"))))



(defn get_replay
  "download a chunk + keyframe "
  [game_id chunk_num]
  (let [chunk_64  (client/get (format "http://spectator.na.lol.riotgames.com/observer-mode/rest/consumer/getGameDataChunk/NA1/%s/%s/token" game_id chunk_num))])
  (let [key_frame_64   (client/get (format "https://na.api.pvp.net/observer-mode/rest/consumer/getKeyFrame/NA1/%s/%s/token" game_id chunk_num))])
)
;;getting chunks isn't going to help us until we can decode the binary file that 
;;results from the decrypted / decompressed chunk



(defn start_observer
  "launch observer client from command line using batch file  currently only available for windows, easy enough to apply to osX"
  [summoner_name]
  (try
    (sh (format "resources/%s.bat" summoner_name))
  (catch Exception e
    (prn e))))


(defn -main
  "save all the data as distinct files .. needs a file creation system"
  [api_key summoner_name]
  (let [game_data (query_game (by_summoner_name api_key summoner_name) api_key)]
    (let [[participants game_id observers] game_data]
      ;;write the data to files for debugging + analysis
      (spit (format "resources/%s-participants" game_id) participants)  ;; this gives us a field will all the current game participants ala lolnexus
      (spit (format "resources/%s-lastchunk.json" game_id) (last_chunk game_id))

      (let [base (slurp "resources/windows")]
        (spit (format "resources/%s.bat" summoner_name) (format "%s %s %s NA1\"" base (observers :encryptionKey) game_id)))
        
  )))