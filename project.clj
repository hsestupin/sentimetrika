(defproject sentimetrika "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [noir "1.3.0-beta3"]
                 [org.clojars.hsestupin/yandex-blogs-api "1.0.0"]
                 [korma "0.3.0-beta7"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
;mvn install:install-file -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=3.0 -Dpackaging=jar -Dfile=/path/to/file
                 [com.microsoft.sqlserver/sqljdbc4 "3.0"]
                 [org.clojure/tools.logging "0.2.3"]
                 [clojure-pymorphy "0.1.0"]
                 [clj-tagsoup "0.3.0"]
                 [com.leadtune/clj-ml "0.2.4"]
                 [clj-aws-s3 "0.3.3"]]
;                for proper pom.xml generation with "lein pom"
;                 [cheshire "3.0.0"]
;                 [org.codehaus.jackson/jackson-core-asl "1.9.5"]
;                 [org.codehaus.jackson/jackson-smile "1.9.5"]]
  :jvm-opts ["-Dfile.encoding=utf-8"]
  :plugins [[lein-beanstalk "0.2.6"]]
  :ring {:handler sentimetrika.server/handler}
  :main sentimetrika.server
  :aws {:beanstalk {:region "eu-west-1"}})

