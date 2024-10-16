FROM openjdk:21-slim AS qing-bootstrap-monitor
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"

EXPOSE 10086
WORKDIR /usr/local

# set environment
ENV JVM_XMS="256M" \
    QING_ADMIN_PORT=10086 \
    JVM_Mxs="128mm" \
    JVM_Xss="256m" \
    TIME_ZONE="Asia/Shanghai"
ENV QING_CONSUL_HOST=localhost
ENV QING_CONSUL_PORT=8500

COPY target/qing-bootstrap-monitor.jar /usr/local/app.jar

EXPOSE 10086
ENTRYPOINT ["java","-jar","/usr/local/app.jar"]
