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

(rf/reg-event-db
 :task-started
 (fn [db [_ task]]
   (assoc db
          :timer (js/setInterval (fn []
                                   (rf/dispatch [:time-now (js/Date.)]))
                                 1000)
          :task  task)))

(rf/reg-event-db
 :time-now
 (fn [db [_ now]]
   (let [{:keys [start mins]} (:task db)]
     (if (>= (- now start) (* 1000 60 mins))
       (-> db
           (update :timer js/clearInterval))
       (-> db
           (assoc :clock (/ (- (* 1000 60 mins) (- now start))
                            1000)))))))

(rf/reg-event-fx
 :start
 (fn [_ [_ task]]
   {:post-http
    {:path     "/app/task"
     :params   {:name task}
     :callback (fn [resp]
                 (rf/dispatch
                  [:task-started (-> resp
                                     :body
                                     (update :start #(js/Date. %1)))]))}}))
