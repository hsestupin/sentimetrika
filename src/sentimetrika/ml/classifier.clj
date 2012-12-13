(ns sentimetrika.ml.classifier
  (:require
    [clj-ml.classifiers :as ml]
    [sentimetrika.ml.dataset :as ds]
    [sentimetrika.ml.features :as features]))

(def classifier (agent nil))

;(defn- init []
;  (send classifier
;    (fn [_]
;      (let [c (ml/make-classifier :bayes :naive)
;            ds (ds/load-dataset)]
;        (clj-ml.data/dataset-set-class ds (ds/get-feature-count))
;        (clj-ml.classifiers/classifier-train c ds)))))

(defn- ready? []
  (not (nil? @classifier)))


(defn retrain []
  (send classifier
    (fn [_]
      (let [c (ml/make-classifier :bayes :naive)
            ds (ds/load-dataset true)]
        (clj-ml.data/dataset-set-class ds features/amount)
        (clj-ml.classifiers/classifier-train c ds)))))
