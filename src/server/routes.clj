(ns server.routes
  (:require [bidi.bidi :refer [match-route]]            
            [hiccup.page :refer [html5 include-js include-css]]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.local :as l]))

(defonce state (atom {:start (t/to-time-zone (t/now)
                                             (t/time-zone-for-offset -7))
                      :mins  25}))

(defn end
  [{:keys [start mins]}]
  (t/plus start (t/minutes (inc mins))))

(defn ^:private index
  [req]
  (let [end (end @state)]
    {:status  200
     :headers {"Content-Type" "text/html"} 
     :body    (html5
               [:head
                (include-css "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.4/css/bulma.min.css")
                (include-js "https://use.fontawesome.com/releases/v5.3.1/js/all.js")
                [:body
                 [:div (if (t/after? (t/now) end)
                         (str "Free")
                         (str "Next break is at "
                              (f/unparse (f/formatter-local "hh:mm") end)))]]])}))

(defn app
  [req]
  {:status  200
   :headers {"Content-Type" "text/html"} 
   :body    (html5
             [:head
              (include-css "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.4/css/bulma.min.css")
              (include-js "https://use.fontawesome.com/releases/v5.3.1/js/all.js")
              [:body
               [:div {:id "app"}]
               (include-js "main.js")]])})

(defn ->pomo
  [interval]
  {:start (t/to-time-zone (t/now)
                          (t/time-zone-for-offset -7))
   :mins  interval})

(defn task->interval
  [s]
  (case s
    "work"  25
    "break" 5))

(defn task
  [req]
  (let [pomo (-> req
                 :body
                 :name
                 task->interval
                 ->pomo)]
    (reset! state pomo)
    {:status 200
     :body   (update pomo
                     :start
                     (partial f/unparse
                              (f/formatters :date-time)))}))

(def routes
  ["/" {""     :index
        "app"  :app
        "app/" {""     :app
                "task" :task}}])

(def handlers
  {:index index
   :app   app
   :task  task})

(defn ring-handler
  [req] 
  (let [handler (->> req
                     :uri
                     (match-route routes)
                     :handler
                     handlers)]
    (handler req)))

(defn make-routes
  []
  #'ring-handler)
