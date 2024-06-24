FROM openjdk:17-slim AS qing-eureka-server
USER root
MAINTAINER Stan<1576302867@qq.com>
EXPOSE 9527
COPY qing-eureka-server.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]
