(ns client.routing
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [bidi.router :refer [start-router!]]
            [re-frame.core :as rf]
            [goog.string :as gstr]))

(def app-routes
  ["/app" {"" :index}])

(defn start!
  []
  (accountant/configure-navigation!
   {:nav-handler  (fn
                    [path]                   
                    (let [match        (bidi/match-route app-routes path)
                          current-page (:handler match)
                          route-params (:route-params match)]
                      (rf/dispatch [:change-page match])))
    :path-exists? (fn [path]
                    (boolean (bidi/match-route app-routes path)))})
  (accountant/dispatch-current!))

(defn path-for
  [component]
  (bidi/path-for app-routes component))

(defn clock
  [now]
  (let [now  @now
        mins (js/Math.floor (/ now 60))
        secs (js/Math.round  (- now (* mins 60)))]
    [:div (gstr/format "%02d:%02d" mins secs)]))

(defmulti page :handler)

(defmethod page :index
  [params]
  (let [now (rf/subscribe [:clock])]
    [:div 
     [:button {:on-click #(rf/dispatch [:start "work"])}
      "Start work"]
     [:br]
     [:button {:on-click #(rf/dispatch [:start "break"])}
      "Start break"]
     [clock now]]))
