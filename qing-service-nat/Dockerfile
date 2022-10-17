FROM java:8-jre AS zhangli-service-natcross-provider
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8989
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/app.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/app.jar"]