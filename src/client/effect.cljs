(ns client.effect
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :as rf]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))
(rf/reg-fx
 :post-http
 (fn [{:keys [params path callback]}]
   (go (let [response (<! (http/post path 
                                     {:with-credentials? false
                                      :json-params       params}))]
         (callback response)))))

(rf/reg-fx
 :get-http
 (fn [[path callback]]
   (go (let [response (<! (http/get path
                                    {:with-credentials? false}))] 
         (callback response)))))
