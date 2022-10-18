FROM openjdk:17-slim AS qing-eureka-service
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ENV JVM_OPTS -Xmx64m -Xms64m -XX:MaxPermSize=32m
EXPOSE 8761
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/eureka-service.jar
ENTRYPOINT ["java","-jar","/home/eureka-service.jar"]
