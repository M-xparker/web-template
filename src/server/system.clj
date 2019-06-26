(ns server.system
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [run-server]]
            [server.ring :refer [app]]))

(defrecord WebServer [port]
  component/Lifecycle
  (start [this]
    (assoc this :server (run-server app {:port port})))
  (stop [this]
    (let [_ ((:server this) :timeout 0)]
      (dissoc this :server))))

(defn create
  [options] 
  (component/system-map 
   :web (map->WebServer (:web options))))

(defn start [system]
  (component/start system))

(defn stop [system]
  (component/stop system))
