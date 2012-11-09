(ns sentimetrika.views.common
  (:use
    [noir.core :only [defpartial]]
    [hiccup.page :only [include-css include-js html5]]
    [sentimetrika.locale :only [i18n]]))

(defpartial layout [& content]
  (html5
    [:head
     [:title "web"]
     (include-css "/css/reset.css")]
    [:body
     [:div#wrapper
      content]]))

(defpartial main-layout [& content]
  (html5
    [:head
     [:title (i18n :sentimetrika)]
     (include-css "css/bootstrap.min.css")
     [:style "body { padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */}"]
     ; Bootstrap
     (include-js "js/bootstrap.min.js")
     (include-css "css/bootstrap-responsive.min.css")]
    [:body
     [:div.navbar.navbar-inverse.navbar-fixed-top
      [:div.navbar-inner
       [:div.container
        [:a.btn.btn-navbar {:data-toggle "collapse" :data-target ".nav-collapse"}
         [:span.icon-bar]
         [:span.icon-bar]
         [:span.icon-bar]]
        [:a.brand {:href "#"} (i18n :sentimetrika)]
        [:div.nav-collapse.collapse
         [:ul.nav
          [:li.active [:a {:href "#"} (i18n :scan-reviews)]]
          [:li [:a {:href "#"} (i18n :system-training)]]
          [:li [:a {:href "#"} (i18n :contact)]]]]]]]
     [:div.container content]
     (include-js "js/jquery.min.js")]))

;sentimetrika=Sentimetrika
;system-training=Обучение системы
;scan-reviews=Сканирование отзывов
;search=Поиск
;improve-dataset=Качество сканирования отзывов может быть частично улучшено при помощи увеличения базы "знаний" системы
;contact=Контакты
