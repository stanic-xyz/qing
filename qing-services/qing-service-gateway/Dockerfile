FROM openjdk:21-slim AS qing-service-gateway
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 9527
WORKDIR /home
ADD target/qing-service-cloud-gateway.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
