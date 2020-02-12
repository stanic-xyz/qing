FROM java:8-jre AS zhangli-zuul-gateway
USER root
MAINTAINER Stan<1576302867@qq.com>
LABEL maintainer="Stan <1576302867@qq.com>>"
EXPOSE 8761
WORKDIR /home
ARG JAR_FILE
ADD target/${JAR_FILE} /home/${JAR_FILE}
ENTRYPOINT ["/usr/bin/java","-jar","/home/${JAR_FILE}"]
