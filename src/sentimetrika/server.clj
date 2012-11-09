(ns sentimetrika.server
  (:require [noir.server :as server]))

(require '(sentimetrika.views common training welcome))

(server/load-views-ns 'sentimetrika.views)

(def handler (server/gen-handler {:mode :dev
                                  :ns 'sentimetrika.training}))

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                         :ns 'sentimetrika})))


