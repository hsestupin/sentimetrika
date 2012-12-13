(ns sentimetrika.views.contact
  (:require
    [sentimetrika.views.common :as common])
  (:use
    [noir.core :only [defpage]]
    [sentimetrika.locale :only [i18n]]))

(defpage "/contact" []
  (common/main-layout :contact
    [:h4 "Email: sentimetrika@gmail.com"]))
