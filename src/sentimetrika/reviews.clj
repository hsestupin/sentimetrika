(ns sentimetrika.reviews
  (:require
    [yandex-blogs :as yb]))

(defn collect [q]
  (:entries (yb/get {:text q})))

(def f (collect "Путин"))
