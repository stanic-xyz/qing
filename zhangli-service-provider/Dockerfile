FROM java:8-jre AS zhangli-service-provider
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8080
WORKDIR /home
RUN echo ${JAR_FILE}
ARG JAR_FILE
ADD target/${JAR_FILE} /home/zhangli-service-provider.jar
ENTRYPOINT ["/usr/bin/java","-jar","/home/zhangli-service-provider.jar"]