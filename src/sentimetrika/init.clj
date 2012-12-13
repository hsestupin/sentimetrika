(ns sentimetrika.init
  (:use
    [korma.db]
    [clojure.tools.logging :only (info error)])
  (:require
    [sentimetrika.db :as db]))

(defn go [dev-mode?]
  (info "Server was started in " (if dev-mode? "dev" "prod") "mode")
  (defdb korma-db (db/reset-db-spec! {:db (if dev-mode? "testdb" "sentimetrikadb")}))
  (db/clear-db)
  (db/init-db))