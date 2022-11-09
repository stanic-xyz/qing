# 写在最前面：强烈建议先阅读官方教程[Dockerfile最佳实践]（https://docs.docker.com/develop/develop-images/dockerfile_best-practices/）
# 选择构建用基础镜像（选择原则：在包含所有用到的依赖前提下尽可能提及小）。如需更换，请到[dockerhub官方仓库](https://hub.docker.com/_/java?tab=tags)自行选择后替换。
FROM maven:3.6.0-jdk-8-slim as build

# 指定构建过程中的工作目录
WORKDIR /app

# 将src目录下所有文件，拷贝到工作目录中src目录下
#COPY zhangli-service-provider/src /app/zhangli-service-provider/src

COPY zhangli-* /app/

# 将pom.xml文件，拷贝到工作目录下
#COPY settings.xml pom.xml /app/
#COPY zhangli-service-provider/pom.xml /app/zhangli-service-provider/
COPY zhangli-admin-server/pom.xml /app/zhangli-admin-server/
COPY zhangli-bus-server/pom.xml /app/zhangli-bus-server/
COPY zhangli-common/pom.xml /app/zhangli-common/
COPY zhangli-config-git/pom.xml /app/zhangli-config-git/
COPY zhangli-eureka-service/pom.xml /app/zhangli-eureka-service/
COPY zhangli-feign-client/pom.xml /app/zhangli-feign-client/
COPY zhangli-service-cloud-gateway/pom.xml /app/zhangli-service-cloud-gateway/
COPY zhangli-service-natcross-provider/pom.xml /app/zhangli-service-natcross-provider/
COPY zhangli-service-provider/pom.xml /app/zhangli-service-provider/
COPY pom.xml /app/

# 执行代码编译命令
# 自定义settings.xml, 选用国内镜像源以提高下载速度
# RUN mvn -s /app/settings.xml -f /app/pom.xml clean package
RUN mvn -f /app/pom.xml -pl zhangli-service-provider clean package

# 选择运行时基础镜像
FROM java:8-jre AS zhangli-service-provider
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
ENV ENV prod

# 指定运行时的工作目录
WORKDIR /app

# 将构建产物jar包拷贝到运行时目录中
COPY --from=build /app/zhangli-service-provider/target/zhangli-service-provider.jar .

# 暴露端口
EXPOSE 8080

# 执行启动命令
CMD ["java", "-jar", "/app/zhangli-service-provider.jar"]
