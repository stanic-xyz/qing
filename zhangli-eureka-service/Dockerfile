FROM java:8 AS zhangli
MAINTAINER Stan<1576302867@qq.com>
VOLUME ["/home"]
ADD ./target/zhangli-eureka-service.jar /home
WORKDIR /home
EXPOSE 8761
ENTRYPOINT ["java","-version"]