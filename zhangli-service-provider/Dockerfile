FROM openjdk:17-slim AS zhangli-service-provider
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ENV ENV prod
EXPOSE 8080
ARG JAR_FILE
COPY target/${JAR_FILE}.jar app.jar
ENTRYPOINT ["/usr/bin/java","-jar","/app.jar"]
