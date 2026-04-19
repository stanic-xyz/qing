FROM eclipse-temurin:21-jre-jammy

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建非root用户
RUN groupadd -r spring && useradd -r -g spring spring

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar文件
COPY target/qing-service-llm.jar /app/app.jar

# 更改所有权
RUN chown -R spring:spring /app

# 切换到非root用户
USER spring

# 暴露端口
EXPOSE 8087

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD curl -f http://localhost:8087/actuator/health || exit 1

# 环境变量
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""
ENV JVM_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 启动命令
ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} ${JAVA_OPTS} -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
