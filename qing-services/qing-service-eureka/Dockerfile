FROM openjdk:21-slim AS qing-eureka-server
USER root
LABEL MAINTAINER=Stan<1576302867@qq.com>
EXPOSE 8761
WORKDIR /usr/local
COPY target/qing-service-registry.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
