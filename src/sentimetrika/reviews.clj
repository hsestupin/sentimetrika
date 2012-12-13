(ns sentimetrika.reviews
  (:use
    [korma.core]
    [sentimetrika.models.korma])
  (:require
    [yandex-blogs :as yb]
    [pl.danieljanus.tagsoup :as html]))

(defn untagged? [{:keys [description title published-date uri]}]
  (println (html/parse-string (:value description)) "\n")
  (if-let [revs (select reviews (where {:body [like (html/parse-string (:value description))]}))]
    (empty? revs)))

(defn untagged? [{:keys [description title published-date uri]}]
  true)

(defn collect [q]
  (filter untagged? (:entries (yb/get {:text q}))))
