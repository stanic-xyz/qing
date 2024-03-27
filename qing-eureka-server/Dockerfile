FROM openjdk:17-slim AS qing-eureka-server
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8000
ADD qing-eureka-server.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
