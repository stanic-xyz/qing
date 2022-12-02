FROM openjdk:17-slim AS qing-admin-server
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ENV ENV prod
ENV QING_REGISTRY_EUREKA_URL "http://localhost:8761/eureka"
EXPOSE 10086
WORKDIR /home
ADD target/qing-admin-server.jar /usr/local/app.jar
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
