(ns curler.core
(:require [clojure.data.json :as json])
(require [clj-http.client :as client])

(:gen-class) )

(defn get_id
  "return ID from summoner name"
  [api_key summoner_name] 

  (println "posting http GET to league API for relevant data...")

(let [API_DATA (json/write-str (client/get (format "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s" summoner_name api_key)))]
  (let [PARSED ((json/read-str API_DATA :key-fn keyword) :body)]
 	(let [BODY ((json/read-str PARSED :key-fn keyword) (keyword summoner_name))]
 	 
 	(println (BODY :id))	
))))

(defn -main
  "put everything together, use the same arguments"
  [api_key summoner_name]
  (get_id api_key summoner_name)

 )