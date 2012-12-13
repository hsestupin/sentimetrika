(ns sentimetrika.tests
  (:use
    [clojure.test]
    [korma core db]
    [sentimetrika.models.korma]
    [clojure.tools.logging :only (info error)])
  (:require
    [sentimetrika.db :as db]
    [noir.options :as opts]))

;redefine db for tests
(defdb testdb (db/reset-db-spec! {:db "testdb"}))
(db/clear-db)

;(->
;  (Logger/getLogger "com.microsoft.sqlserver.jdbc.SQLServerStatement")
;  (.setLevel (Level/FINE)))

(defn insert-data [with-tags]
  (let [review-id (:GENERATED_KEYS (insert reviews (values {:title "title1" :body "йцук"})))]
    (when with-tags
      (insert tags (values {:review_id review-id :name "name1"}))))
  (let [review-id (:GENERATED_KEYS (insert reviews (values {:title "title2" :body "asd"})))]
    (when with-tags
      (insert tags (values {:review_id review-id :name "name2"})))))

(defn init [with-tags]
  (db/init-db)
  (insert-data with-tags))

(defn verify-tag [tag expected-review-id expected-name]
  (is (= expected-review-id (:review_id tag)))
  (is (= expected-name (:name tag))))

; NEED NEW CLEAR MSSQL DATABASE
(deftest apply-tag-test
  (let [start-time (System/currentTimeMillis)]
    (testing "Testing apply-tag method"

      (testing "in case review doesn't exist in db"
        (init false)
        (try
          (db/apply-tag "title3" "not_existed" "good")
          (let [reviews (select reviews)]
            (is (= 3 (count reviews))))
          (let [inserted-reviews (select reviews (where {:title "title3" :body [like "not_existed"]}))]
            (is (= 1 (count inserted-reviews)))
            (let [tags (select tags)
                  new-tag (first tags)]
              (is (= 1 (count tags)))
              (verify-tag new-tag (:id (first inserted-reviews)) "good")))
          (finally
            (db/clear-db))))

      (testing "in case review exists in db and tag doesn't"
        (init false)
        (try
          ; review with such body is already existed
          (db/apply-tag "title3" "йцук" "bad")
          ; none reviews were inserted!
          (let [reviews (select reviews)]
            (is (= 2 (count reviews))))
          (let [inserted-tags (select tags)
                new-tag (first inserted-tags)
                review-id (:id (first (select reviews (where {:body [like "йцук"]}))))]
            (is (= 1 (count inserted-tags)))
            (verify-tag new-tag review-id "bad"))
          (finally
            (db/clear-db))))

      (testing "in case review and tag exist in db (tag should be rewritten in db)"
        (init true)
        (try
          ; review with such body is already existed
          (db/apply-tag "title3" "asd" "whatever")
          ; none reviews were inserted!
          (let [reviews (select reviews)]
            (is (= 2 (count reviews))))
          (let [all-tags (select tags)
                review-id (:id (first (select reviews (where {:body [like "asd"]}))))
                review-tags (select tags (where {:review_id review-id}))]
            (is (= 2 (count all-tags)))
            (is (= 1 (count review-tags)))
;            review had tag "name2", now it should be overwritten by "whatever" one
            (verify-tag (first review-tags) review-id "whatever"))
          (finally
            (db/clear-db)))))

    (println "Taken time to RDS tests:" (- (System/currentTimeMillis) start-time) "ms")))

