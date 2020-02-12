FROM java:8-jre AS zhangli-feign-client
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8761
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/zhangli-feign-client-service.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/zhangli-feign-client-service.jar"]