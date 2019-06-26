(ns client.event
  (:require [re-frame.core :as rf]))

(def db {})

(rf/reg-event-db
 :initialize
 (fn [_ _] db))

(rf/reg-event-db
 :change-page
 (fn [db [_ location]]
   (assoc db :page location)))
