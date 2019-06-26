(ns server.routes
  (:require [bidi.bidi :refer [match-route]]            
            [hiccup.page :refer [html5 include-js include-css]]))

(defn ^:private index
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

(def routes
  ["/" {"" :index}])

(def handlers
  {:index index})

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
