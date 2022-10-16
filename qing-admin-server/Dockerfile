FROM java:8-jre AS zhangli-admin-server
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ENV ENV prod
EXPOSE 10086
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/zhangli-admin-server.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/zhangli-admin-server.jar"]