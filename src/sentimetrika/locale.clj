(ns sentimetrika.locale)

(def project-lang "ru")

(def locales #{"ru" "en"})

(defn- read-properties
  "Read properties from file-able."
  ([fileable]
    (into {} (map #(vector (keyword (key %)) (val %))
      (with-open [isr (java.io.InputStreamReader. (java.io.FileInputStream. (new java.io.File fileable)) "utf8")]
        (doto (new java.util.Properties)
          (.load isr)))))))

(def loaded-property-file
  (into {} (-> (str "locales/locale_" project-lang ".properties")
    clojure.java.io/resource
    .getFile
    read-properties)))


(defn i18n [code]
  (loaded-property-file code))

(println (i18n :contact))