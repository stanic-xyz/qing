FROM java:8-jre AS zhangli-service-provider
USER root
ENV ENV pro
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8080
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/zhangli-service-provider.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/zhangli-service-provider.jar"]