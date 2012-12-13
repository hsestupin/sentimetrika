(ns sentimetrika.server
  (:require
    [noir.server :as server]
    [sentimetrika.init :as init]))

;for correct deploying as war file into servlet container
(require '(sentimetrika.views common welcome scanning training contact))

(server/load-views-ns 'sentimetrika.views)

(def handler (server/gen-handler {:mode :dev
                                  :ns 'sentimetrika}))

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (init/go (= mode :dev))
    (server/start port {:mode mode
                         :ns 'sentimetrika})))


