(ns sentimetrika.ml.features
  (:use [clojure.tools.logging :only (info error)])
  (:require [pymorphy]
            [sentimetrika.s3 :as s3]
            [clojure.string :as str]
            [sentimetrika.db :as db])
  (:import [java.io InputStream StringWriter]))

(def amount 2)

(def stop-words #{"И" "НЕ" "ЧТО" "НА" "ФИЛЬМ" "ОН" "ОНА" "В" "КОТОРЫЙ" "ИЗ" "К" "ТАК" "С" "ТОТ"
                  "ВЕСЬ" "НО" "А" "КАК" "Я" "ЭТО" "ЭТОТ" "ПО" "ТАКОЙ" "ДЛЯ" "БЫ" "ЖЕ" "О" "ОДИН" "ЧЕЛОВЕК" "У"
                  "САМ" "ГЕРОЙ" "ТОЛЬКО" "ЗА" "СЮЖЕТ" "ЕСЛИ" "10" "ДАЖЕ" "СВОЙ" "ДО" "ПРОСТО" "УЖЕ" "ЭТОМ"
                  "ЭТОГО" "ИХ" "ДА" "РЕЖИССЕР" "ИЛИ" "ВО" "ЕСТЬ" "МОЖНО" "ЕЩЕ" "ЗРИТЕЛЬ" "ЗДЕСЬ" "БОЛЕЕ" "ЧЕМ" "КАЖДЫЙ"
                  "ЕЕ" "БЫЛ" "НЕТ" "НУ" "МОМЕНТ" "ВС" "ЛИШЬ" "СКАЗАТЬ" "БЫТЬ" "ДАННЫЙ" "ТАКЖЕ"
                  "КАКОЙ-ТО" "Т" "ОНИ" "ОТ" "БЫЛО" "КОГДА" "АКТЕР" "МНЕ" "КИНО" "ВОТ" "РАЗ"})

(defn process-review [{review-text :body}]
  (pymorphy/normalize (re-seq #"[а-яА-Я_\-0-9]+" review-text)))

(defn- join-words
  ([]
    (apply concat
      (map process-review (db/collect-reviews))))
  ([positive?]
    (apply concat
      (map process-review (keys (filter #(= (val %) positive?) (db/collect-reviews positive?)))))))

(defn- top-words []
  (let [freq (frequencies (join-words))]
    (into (sorted-map-by #(> (freq %1) (freq %2)))
      (apply dissoc freq stop-words))))

(defn- feature-words
  ([] (feature-words amount))
  ([some]
    (vec (take some (keys (top-words))))))

(defn- create-features []
  (info "Creating feature-set from all words...")
  (let [start-time (System/currentTimeMillis)
        new-features (feature-words)]
    (info "Feature-set was created in" (- (System/currentTimeMillis) start-time) "ms")
    new-features))

(defn- get-features []
  (if (s3/object-exists? :features )
    (with-open [^InputStream content (:content (s3/get-object :features ))
                writer (new StringWriter)]
      (clojure.java.io/copy content writer)
      (str/split (.toString writer) #","))
    (let [new-features (create-features)]
      (s3/put-object :features (str/join "," new-features))
      new-features)))

(defn- feature-val
  "Returns 1 if word contains in features-words set otherwise 0."
  [words feature-word]
  (if (some #{feature-word} words) 1 0))

(defn- extract-features*
  "Returns a sequence of 1 (meaning that word from review is containing
  in feature word's set) and 0"
  [review]
  (let [review-words (process-review review)]
    (vec (map #(feature-val review-words %) (get-features)))))

(defn extract-features [{class-attribute :tag, :as review}]
  (try
    (conj (extract-features* review) class-attribute)
    (catch Throwable th
      (error "Error during extracting features from" (:body review) "\n" th))))

(defmacro create-feature-keys
  "Create vector [:f1 :f2 .. :fN {:sentiment [:good :bad]}] where N is the number of features"
  [args-count]
  `(conj
     (vec
       (map #(keyword (str "f" (inc %)))
         (range ~args-count)))
     {:sentiment [:good :bad :neutral]}))

(defn reset-features []
  (s3/delete-object :features ))
