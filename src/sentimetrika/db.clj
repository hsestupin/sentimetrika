(ns sentimetrika.db
  (:use [korma core db]
        [sentimetrika.models.korma]
        [sentimetrika.models.schema]
        [clojure.tools.logging :only (info error)])
  (:require [clojure.java.jdbc :as sql]))

;need to create db manually :/
(def default-settings {:db "sentimetrikadb"
                       :host "aav1bkov98k52s.c8iyjawn53ef.us-east-1.rds.amazonaws.com"
                       :port 1433
                       :user "sentimetrika"
                       :password "sentimetrika"})

;for passing as argument to functions init-db and clear-db
(def db-spec (atom (mssql default-settings)))

(defn reset-db-spec! [opts]
  (reset! db-spec (mssql (merge default-settings opts))))

(defdb productiondb @db-spec)

(defn init-db []
  (sql/with-connection (deref db-spec)
    (sql/transaction
      (apply sql/create-table reviews-table)
      (apply sql/create-table tags-table))))

(defn clear-db []
  (try
    (sql/with-connection (deref db-spec)
      (sql/transaction
        (sql/drop-table (first reviews-table))
        (sql/drop-table (first tags-table))))
    ;    It raises if database is already cleared
    (catch Throwable ignored)))

(defn apply-tag [title body tag]
  (if-let [review (first (select reviews (where {:body [like body]})))]
    (transaction
      (delete tags (where {:review_id (:id review)}))
      (:GENERATED_KEYS (insert tags (values {:review_id (:id review) :name tag}))))
    (transaction
      (if-let [review-id (:GENERATED_KEYS (insert reviews (values {:title title :body body})))]
        (do
          (insert tags (values {:review_id review-id :name tag}))
          review-id)))))

(defn collect-reviews
  ([]
    (select tags (fields [:name :tag ])
      (with reviews (fields :body ))))
  ([good?]
    (select tags (fields [:name :tag ])
      (with reviews (fields :body ))
      (where {:name (if good? "good" "bad")}))))



