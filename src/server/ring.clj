(ns server.ring
  (:require [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [server.routes :as routes]))

(def app
  (-> (routes/make-routes) 
      (wrap-reload) 
      wrap-json-response      
      (wrap-json-body {:keywords? true})
      (wrap-params)
      (wrap-session {:store (cookie-store {:key "a 16-byte secret"})})
      (wrap-resource "public")))
