FROM openjdk:17-slim AS qing-bootstrap-service
USER root
MAINTAINER Stan<1576302867@qq.com>
EXPOSE 8080
WORKDIR /usr/local
COPY target/qing-domain.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
