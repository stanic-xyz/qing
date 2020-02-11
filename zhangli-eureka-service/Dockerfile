FROM java:8 AS zhangli-eureka
MAINTAINER Stan<1576302867@qq.com>
COPY ./target/zhangli-eureka-service.jar /home/zhangli-eureka-service.jar
WORKDIR /home
EXPOSE 8761
RUN ["ls"]
ENTRYPOINT ["java","-jar","/home/zhangli-eureka-service.jar"]