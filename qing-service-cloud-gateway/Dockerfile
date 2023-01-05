FROM openjdk:17-slim AS qing-service-cloud-gateway
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 9527
WORKDIR /home
ADD target/qing-service-cloud-gateway.jar /usr/local/app.jar
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
