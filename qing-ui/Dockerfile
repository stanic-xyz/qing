FROM nginx:stable-alpine3.17-slim AS qing-ui
USER root
MAINTAINER Stan<1576302867@qq.com>
EXPOSE 80
WORKDIR /usr/local
COPY target/classes/META-INF/codegen-ui /usr/share/nginx/html/
