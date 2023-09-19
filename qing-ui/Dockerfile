FROM nginx:latest AS qing-web-bangumi
USER root
MAINTAINER Stan<1576302867@qq.com>
EXPOSE 80
WORKDIR /usr/local
COPY src/main/nginx/conf/default.conf /etc/nginx/conf.d/default.conf
COPY src/main/frontend/dist/ /usr/share/nginx/html/
