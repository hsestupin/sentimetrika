(ns sentimetrika.models.korma
  (:use
    [korma.core]))

(declare reviews)

(defentity tags
  ;; assumes tags.review_id = review.id
  (belongs-to reviews {:fk :review_id}))

(defentity reviews
  (entity-fields :body)

  ;; assumes review.id = tags.review_id
  ;; but gets the results in a second query
  ;; for each element
  (has-many tags {:fk :review_id}))
