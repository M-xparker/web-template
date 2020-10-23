(ns client.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]            
            [client.effect]
            [client.routing :as route]
            [client.subs]
            [client.event]))

(defn ui
  []
  (let [location @(rf/subscribe [:page])]
    (println location)
    [:div 
     (if (:handler location)
       (route/page location))]))

(defn ^:export run
  []
  (reagent/render [#'ui]
                  (js/document.getElementById "app")))

(defn main! []
  (rf/dispatch-sync [:initialize])
  (route/start!)
  (run))

(defn reload! []
  (run))
