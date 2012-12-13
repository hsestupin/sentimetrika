(ns sentimetrika.views.common
  (:use
    [noir.core :only [defpartial defpage]]
    [hiccup.page :only [include-css include-js html5]]
    [hiccup.element :only [link-to]]
    [sentimetrika.locale :only [i18n]])
  (:require
    [sentimetrika.db :as db]))

(def tabs {:scanning (link-to "/scanning" (i18n :scan-reviews))
           :training (link-to "/training" (i18n :system-training))
           :contact (link-to "/contact" (i18n :contact))})

(defpartial main-layout [active-tab & content]
  (html5
    [:head
     [:title (i18n :sentimetrika)]
     ; Bootstrap
     (include-css "css/bootstrap.min.css")
     [:style "body { padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */}"]
     (include-css "css/bootstrap-responsive.min.css")]
    [:body
     [:div.navbar.navbar-inverse.navbar-fixed-top
      [:div.navbar-inner
       [:div.container
        [:a.btn.btn-navbar {:data-toggle "collapse" :data-target ".nav-collapse"}
         [:span.icon-bar]
         [:span.icon-bar]
         [:span.icon-bar]]
        [:a.brand {:href "/"} (i18n :sentimetrika)]
        [:div.nav-collapse.collapse
         [:ul.nav
          (map
            #(let [tab-content (% tabs)]
              (if (= % active-tab)
                [:li.active tab-content]
                [:li tab-content]))
            (keys tabs))
          ]]]]]
     [:div.container
      content
      ;      [:hr]
      ;      [:div.footer
      ;       [:p (str "&copy; " (i18n :sentimetrika) " 2012")]]
      ]
     (include-js "js/jquery.min.js")
     (include-js "js/bootstrap.min.js")
     (include-js "js/sentimetrika.js")]))

;For AWS Beanstalk status checking
(defpage [:head "/"] [] "")

;For displaying current internal info
(defpage "/info" []
  (main-layout :unactive-all-tabs
    (str "Database: " (:db @db/db-spec))))

;sentimetrika=Sentimetrika
;system-training=Обучение системы
;scan-reviews=Сканирование отзывов
;search=Поиск
;improve-dataset=Качество сканирования отзывов может быть частично повышено с помощью расширения базы "знаний" системы
;contact=Контакты
;description1=Sentimetrika - это система автоматического мониторинга и анализа мнений, содержащихся в сообщениях пользователей интернета: блогах, твиттере, форумах и социальных сетях. Она позволяет отслеживать упоминания на заданную тему и производить автоматический анализ их тональности, выделяя положительные, отрицательные и нейтральные упоминания. И представляя результаты анализа в виде наглядных графических отчетов.
;description2=Sentimetrika будет помогать людям выбирать качественные продукты и услуги. А компании смогут с ее помощью анализировать мнения покупателей.
;description3=Система самообучается на основе введенной пользователем информации. Вы можете поучаствовать в этом процессе, вручную задавая метки для найденных упоминаний.
