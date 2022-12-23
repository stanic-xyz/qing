FROM openjdk:17-slim AS qing-admin-server
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"

# set environment
ENV JVM_XMS="1g" \
    QING_ADMIN_IP=127.0.0.1 \
    QING_ADMIN_HOST=localhost \
    QING_ADMIN_PORT=10086 \
    QING_ADMIN_PROTOCOL=https \
    QING_REGISTRY_EUREKA_URL=http://127.0.0.1:8761/eureka \
    JVM_Mxs="256" \
    JVM_Xss="512m" \
    TIME_ZONE="Asia/Shanghai"
ADD target/qing-admin-server.jar /usr/local/app.jar

EXPOSE 80
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
