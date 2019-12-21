FROM java:8 AS zhangli
MAINTAINER Stan<1576302867@qq.com>
VOLUME ["/temp","/home"]
ADD zhangli-config-git/target/zhangli-config-git.jar /temp/zhangli-config.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/temp/zhangli-config.jar"]