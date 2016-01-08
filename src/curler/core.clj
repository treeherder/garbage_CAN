(ns curler.core
(:require [clojure.data.json :as json])
(require [clj-http.client :as client])

(:gen-class) )

(defn -main
  "with given parameters, this function will return a page from the league API call"
  [api_key summoner_name] 

  (println "posting http GET to league API for relevant data...")

(let [API_DATA (json/write-str (client/get (format "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/%s?api_key=%s" summoner_name api_key)))]
  (let [PARSED ((json/read-str API_DATA :key-fn keyword) :body)]
 	(let [BODY ((json/read-str PARSED :key-fn keyword) (keyword summoner_name))]
 	 
 	(println (BODY :id))	
))))