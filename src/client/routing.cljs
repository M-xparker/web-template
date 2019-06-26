(ns client.routing
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [bidi.router :refer [start-router!]]
            [re-frame.core :as rf]))

(def app-routes
  ["/" {"" :index}])

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

(defmulti page :handler)

(defmethod page :index
  [params]
  [:div "Hello world!"])
