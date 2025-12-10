# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre-jammy

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建非root用户
RUN groupadd -r spring && useradd -r -g spring spring

# 设置工作目录
WORKDIR /app

ADD target/auth-interfaces.jar ./app.jar
ADD src/main/resources/application-prod.yml ./application-prod.yml

# 更改所有权
RUN chown -R spring:spring /app

# 切换到非root用户
USER spring

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
 CMD curl -f http://localhost:8080/actuator/health || exit 1

# 环境变量
ENV SPRING_PROFILES_ACTIVE=default
ENV JAVA_OPTS=""
ENV JVM_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
ENV MODULE_NAME=auth-interfaces
ENV CONFIG_SERVER_ENABLED=false
ENV EUREKA_SERVER_ENABLED=false

ENV POSTGRES_HOST=postgres
ENV POSTGRES_PORT=5432
ENV POSTGRES_USERNAME=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB_NAME=qing-auth

ENV RABBIT_MQ_HOST=rabbitmq
ENV RABBIT_MQ_USERNAME=guest
ENV RABBIT_MQ_PASSWORD=guest

ENV REDIS_HOST=rabbitmq
ENV REDIS_DATABASE=0
ENV REDIS_PORT=6379

# 入口点
ENTRYPOINT ["java", "-jar", "app.jar","--spring.profiles.active=prod"]
