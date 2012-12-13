(ns sentimetrika.s3
  (:require [aws.sdk.s3 :as s3]))

(def cred {:access-key "AKIAI2FXHUKACRGW34AQ"
           :secret-key "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"})

(def bucket "sentimetrika")

(when-not (s3/bucket-exists? cred bucket)
  (s3/create-bucket cred bucket))

(defn get-object [^clojure.lang.Keyword key]
  (s3/get-object cred bucket (name key)))

(defn object-exists? [^clojure.lang.Keyword key]
  (s3/object-exists? cred bucket (name key)))

(defn put-object [^clojure.lang.Keyword key value]
  (s3/put-object cred bucket (name key) value))

(defn delete-object [^clojure.lang.Keyword key]
  (s3/delete-object cred bucket (name key)))

