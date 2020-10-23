(ns client.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :page
 (fn [db _]
   (:page db)))

(rf/reg-sub
 :clock
 (fn [db _]
   (:clock db)))
