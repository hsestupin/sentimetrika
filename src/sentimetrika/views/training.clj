(ns sentimetrika.views.training
  (:require
    [sentimetrika.views.common :as common]
    [sentimetrika.reviews :as reviews]
    [sentimetrika.db :as db]
    [noir.response :as response]
    [clojure.string :as str]
    [sentimetrika.ml.classifier :as classifier])
  (:use
    [noir.core :only [defpage]]
    [sentimetrika.locale :only [i18n]]
    hiccup.form
    sentimetrika.views.utils))

(defpage "/training" {:keys [#^String q]} []
  (common/main-layout :training
    [:h3 (i18n :improve-dataset)]
    [:div.row
     [:div.span4 (form-to {:class "form-search"} [:get "/training"]
       [:div.input-append
        (text-field {:class "span2 search-query"} "q")
        (submit-button {:class "btn"} (i18n :search))])]
     [:div.span3
      [:a.btn {:href "/retrain"} (i18n :retrain)]]]

    ;    search results
    (when-not (str/blank? q)
      (let [reviews (reviews/collect q)
            odd-reviews (take-nth 2 reviews)
            even-reviews (take-nth 2 (rest reviews))]
        [:div.row-fluid
         [:div.span6 (map review-div odd-reviews)]
         [:div.span6 (map review-div even-reviews)]]))))

(defpage [:post "/reviews"] {:keys [title body tag]} []
  (if-let [review-id (db/apply-tag title body tag)]
    (response/json {:id review-id})
    (response/empty)))

(defpage "/retrain" []
  (classifier/retrain)
  (response/redirect "/training"))
