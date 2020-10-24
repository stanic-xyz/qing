FROM java:8-jre AS zhangli-config-git
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8000
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/app.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/app.jar"]