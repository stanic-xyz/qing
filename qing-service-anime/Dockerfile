FROM openjdk:17-slim AS qing-service-anime
USER root
MAINTAINER Stan<1576302867@qq.com>
ENV ENV prod
EXPOSE 80
WORKDIR /usr/local
COPY target/qing-service-anime.jar app.jar
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
