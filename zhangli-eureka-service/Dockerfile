FROM java:8-jre AS zhangli-eureka
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8761
ENTRYPOINT ["/usr/bin/java","-jar","/home/zhangli-eureka-service.jar"]
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/zhangli-eureka-service.jar
