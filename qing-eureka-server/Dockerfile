FROM openjdk:17-slim AS qing-config-git
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8000
WORKDIR /home
ADD target/qing-config-git.jar /home/app.jar
ENTRYPOINT ["java","-jar","/home/app.jar"]
