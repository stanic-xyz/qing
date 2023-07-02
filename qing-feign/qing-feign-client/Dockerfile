FROM openjdk:17-slim AS qing-feign-client
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 9001
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/qing-feign-client-service.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/qing-feign-client-service.jar"]
