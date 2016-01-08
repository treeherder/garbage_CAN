(defproject curler "0.1.0-SNAPSHOT"
  :description "an offshoot of another project by the same name"
  :url "http://packetfire.org/"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.7.0"]
  		[org.clojure/data.json "0.2.6"]
 		 [clj-http "2.0.0"]]
  :main ^:skip-aot curler.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
