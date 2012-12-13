(ns sentimetrika.ml.dataset
  (:use [clojure.tools.logging :only (info error)]
        [sentimetrika.ml.features])
  (:require [clj-ml.io :as store]
            [sentimetrika.s3 :as s3]
            [clj-ml.data :as data]
            [sentimetrika.db :as db])
  (:import [java.io ByteArrayOutputStream ByteArrayInputStream]))

;(def reviews (atom nil))
;(compare-and-set! reviews nil (collect-reviews))

(defn- create-dataset []
  (reset-features)
  (data/make-dataset "reviews" (create-feature-keys amount)
    (vec
      (map extract-features (db/collect-reviews)))))

(defn- csv-input-stream [ds]
  (let [byte-os (new ByteArrayOutputStream)]
    (store/save-instances :csv byte-os ds)
    (new ByteArrayInputStream (.toByteArray byte-os))))

;should return
;f1,f2,sentiment
;0,0,good
(defn load-dataset [with-reload?]
  (if (or with-reload? (not (s3/object-exists? :dataset )))
    (let [start-time (System/currentTimeMillis)
          ds (create-dataset)]
      (s3/put-object :dataset (csv-input-stream ds))
      (info "Reviews dataset was constructed in" (- (System/currentTimeMillis) start-time) "ms")
      ds)
    (store/load-instances :csv (:content (s3/get-object :dataset )))))




