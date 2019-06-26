(ns user
  (:require 
   [clojure.tools.namespace.repl :refer [refresh]]
   [server.system :as app]))

(def config
  {:web {:port 3000}})

(def system nil)

(defn init []
  (alter-var-root #'system (constantly (app/create config))))

(defn start []
  (alter-var-root #'system app/start))

(defn stop []
  (alter-var-root #'system app/stop))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))
