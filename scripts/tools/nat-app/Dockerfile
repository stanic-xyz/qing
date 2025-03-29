FROM ubuntu:latest AS builder

RUN apt-get update && apt-get install -y  wget

WORKDIR /

RUN wget https://download.natapp.cn/assets/downloads/clients/2_4_0/natapp_linux_amd64/natapp -O /usr/local/bin/natapp

FROM alpine AS prod
WORKDIR /
USER root
COPY --from=0 /usr/local/bin/natapp /usr/local/bin/natapp
ENV TZ=Asia/Shanghai
RUN chmod 777 /usr/local/bin/natapp
ENV AUTHTOKEN=eb7b479d3f8b090f
ENTRYPOINT ["/usr/local/bin/natapp"]
CMD ["-authtoken=${AUTHTOKEN}"]
