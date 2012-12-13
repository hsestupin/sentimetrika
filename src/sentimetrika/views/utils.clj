(ns sentimetrika.views.utils
  (:use
    [noir.core :only [defpartial]]
    [hiccup.element :only [link-to]]))

(defpartial review-div [{:keys [description title published-date uri]}]
  [:div.item
   [:h4.title {:style "padding-top:20px"} title]
   [:p published-date]
   [:p (link-to uri uri)]
   [:p.description (:value description)]
   [:div.btn-toolbar
    [:div.btn.btn-success {:tag :good} "Good"]
    [:div.btn.btn-info {:tag :neutral} "Neutral"]
    [:div.btn.btn-danger {:tag :bad} "Bad"]]])
