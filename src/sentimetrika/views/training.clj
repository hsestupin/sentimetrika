(ns sentimetrika.views.training
  (:require
    [sentimetrika.views.common :as common]
    [noir.content.getting-started]
    [sentimetrika.reviews :as reviews]
    [clojure.string :as str])
  (:use
    [noir.core :only [defpage]]
    [sentimetrika.locale :only [i18n]]
    hiccup.form))

(defn build-review-div [{:keys [description title published-date uri]}]
  [:div.span6 {:style "padding-bottom:20px"}
    [:h4 title]
    [:p published-date]
    [:p [:a {:href uri} uri]]
    [:p (:value description)]
    [:div.btn-toolbar
     [:div.btn.btn-success "good"]
     [:div.btn.btn-info "neutral"]
     [:div.btn.btn-danger "bad"]]])

(defpage "/training" {:keys [#^String q]} []
  (common/main-layout

     [:h1 (i18n :improve-dataset)]
     (form-to {:class "form-search"} [:get "/training"]
       [:div.input-append
        (text-field {:class "span2 search-query"} "q")
        (submit-button {:class "btn"} (i18n :search))])
     (when-not (str/blank? q)
       [:div.row
        (map build-review-div (reviews/collect q))])))
