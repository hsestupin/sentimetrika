(defproject sentimetrika "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [noir "1.3.0-beta3"]
                 [org.clojars.hsestupin/yandex-blogs-api "1.0.0"]]
  :jvm-opts ["-Dfile.encoding=utf-8"]
  :plugins [[lein-beanstalk "0.2.6"]]
  :ring {:handler sentimetrika.server/handler}
  :main sentimetrika.server
  :aws {:beanstalk {:region "eu-west-1"}})

