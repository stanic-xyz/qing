FROM java:8 AS zhangli-eureka
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ARG JAR_FILE
COPY ${JAR_FILE} /home/zhangli-eureka-service.jar
WORKDIR /home
EXPOSE 8761
ENTRYPOINT ["java","-jar","/home/zhangli-eureka-service.jar"]