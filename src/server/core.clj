(ns server.core
  (:gen-class)
  (:require [server.system :as system]))

(def port (Integer. (or (System/getenv "PORT") 3000)))

(def config
  {:web {:port port}})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (-> config
      system/create
      system/start))
