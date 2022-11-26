FROM openjdk:17-slim AS qing-service-anime
USER root
MAINTAINER Stan<1576302867@qq.com>
ENV ENV prod
EXPOSE 80
WORKDIR /usr/local
ENV EUREKA_HOST localhost
ENV QING_REGISTRY_EUREKA_PORT 8761
COPY target/qing-service-anime.jar app.jar
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
