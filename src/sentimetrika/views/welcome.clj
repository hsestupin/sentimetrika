(ns sentimetrika.views.welcome
  (:require
    [sentimetrika.views.common :as common])
  (:use
    [noir.core :only [defpage]]
    [sentimetrika.locale :only [i18n]]))

(defpage "/" []
  (common/main-layout :unactive-all-tabs
    [:div.hero-unit
     [:h1 (i18n :sentimetrika)]
     [:hr]
     [:p (i18n :description1)]
     [:p (i18n :description2)]
     [:p (i18n :description3)]]))

