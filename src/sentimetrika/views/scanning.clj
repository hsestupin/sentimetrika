(ns sentimetrika.views.scanning
  (:require
    [sentimetrika.views.common :as common]
    [sentimetrika.reviews :as reviews]
    [clojure.string :as str])
  (:use
    [noir.core :only [defpage]]
    [sentimetrika.locale :only [i18n]]
    hiccup.form
    sentimetrika.views.utils))

(defpage "/scanning" {:keys [#^String q]} []
  (common/main-layout :scanning
    [:h3 (i18n :scan-reviews)]
    (form-to {:class "form-search"} [:get "/scanning"]
      [:div.input-append
       (text-field {:class "span2 search-query"} "q")
       (submit-button {:class "btn"} (i18n :search))])
    (when-not (str/blank? q)
      (let [reviews (reviews/collect q)
            odd-reviews (take-nth 2 reviews)
            even-reviews (take-nth 2 (rest reviews))]
        [:div.row-fluid
         [:div.span6 (map review-div odd-reviews)]
         [:div.span6 (map review-div even-reviews)]]))))
