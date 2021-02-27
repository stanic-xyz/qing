FROM java:8-jre AS zhangli-service-provider
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8080
#WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]