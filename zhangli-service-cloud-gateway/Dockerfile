FROM java:8-jre AS zhangli-service-cloud-gateway
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 9001
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/zhangli-service-cloud-gateway.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/zhangli-service-cloud-gateway.jar"]