FROM openjdk:21-slim AS qing-config-git
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8000
ADD target/qing-config-git.jar /app.jar
ENTRYPOINT ["java","-jar","/home/app.jar"]
