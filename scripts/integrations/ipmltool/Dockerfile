FROM alpine:latest

# 安装必要工具
RUN apk add --no-cache \
    ipmitool \
    mosquitto-clients \
    bc

# 复制控制脚本
COPY control_script.sh /usr/local/bin/control_script.sh
RUN chmod +x /usr/local/bin/control_script.sh

# 设置入口点
ENTRYPOINT ["/usr/local/bin/control_script.sh"]
