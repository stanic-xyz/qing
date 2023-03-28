FROM openjdk:17-slim AS qing-eureka-service
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ENV JVM_OPTS -Xmx64m -Xms64m -XX:MaxPermSize=32m
EXPOSE 80
WORKDIR /home
ADD target/qing-eureka-service.jar /usr/local/app.jar
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
