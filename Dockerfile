FROM java:8 AS zhangli
MAINTAINER Stan<1576302867@qq.com>
VOLUME ["/home"]
EXPOSE 8761
ENTRYPOINT ["java","-jar","/temp/zhangli-config.jar"]
RUN ['bash']