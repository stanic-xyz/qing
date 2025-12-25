# 多阶段构建：构建阶段
FROM maven:3.9.6-eclipse-temurin-21 as builder

# 设置工作目录
WORKDIR /build

# 复制整个项目
COPY qing-services/qing-service-auth/auth-interfaces .

# 设置构建参数
ARG MODULE_PATH=qing-services/qing-service-auth/auth-interfaces
ARG PROFILE=default
ARG BUILD_VERSION=1.0.0
ARG MAVEN_OPTS="-DskipTests -Dmaven.test.skip=true"

# 构建指定的模块
RUN mvn clean package \
    -pl ${MODULE_PATH} \
    -am \
    -f pom.xml \
    -P${PROFILE} \
    ${MAVEN_OPTS} \
    && echo "Build successful for module: ${MODULE_PATH}"

# 找到构建的jar文件
RUN find /build -name "*.jar" -type f | grep -v "sources.jar" | grep -v "javadoc.jar" > /tmp/jar-list.txt \
    && echo "Found jar files:" && cat /tmp/jar-list.txt

# 解压jar文件以便后续复制
RUN JAR_FILE=$(find /build -name "*.jar" -type f | grep -v "sources.jar" | grep -v "javadoc.jar" | head -1) \
    && mkdir -p /app/extracted \
    && (cd /app/extracted; jar -xf ${JAR_FILE}) \
    && echo "Extracted JAR: ${JAR_FILE}"

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre-jammy

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建非root用户
RUN groupadd -r spring && useradd -r -g spring spring

# 设置工作目录
WORKDIR /app

# 从构建阶段复制提取的文件
COPY --from=builder /app/extracted/BOOT-INF/lib /app/lib
COPY --from=builder /app/extracted/META-INF /app/META-INF
COPY --from=builder /app/extracted/BOOT-INF/classes /app/classes

# 复制原生库（如果有）
COPY --from=builder /app/extracted/BOOT-INF/lib/native* /app/lib/native/ 2>/dev/null || :

# 复制jar文件（如果需要）
COPY --from=builder /build/qing-services/qing-service-auth/auth-interfaces/target/*.jar /app/app.jar 2>/dev/null || :
COPY --from=builder /build/qing-services/qing-service-auth/auth-service/target/*.jar /app/app.jar 2>/dev/null || :

# 查找并复制实际的jar文件
RUN find /build -name "*.jar" -type f | grep -v "sources.jar" | grep -v "javadoc.jar" | head -1 | xargs -I {} cp {} /app/app.jar 2>/dev/null || true

# 检查是否存在app.jar
RUN if [ ! -f /app/app.jar ]; then \
    echo "ERROR: No jar file found! Searching in /build..." && \
    find /build -name "*.jar" -type f | grep -v "sources.jar" | grep -v "javadoc.jar" && \
    exit 1; \
    fi

# 复制启动脚本
COPY docker-entrypoint.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

# 复制外部配置文件（如果存在）
COPY qing-services/qing-service-auth/auth-interfaces/src/main/resources/ /app/config/ 2>/dev/null || :
COPY qing-services/qing-service-auth/auth-service/src/main/resources/ /app/config/ 2>/dev/null || :

# 更改所有权
RUN chown -R spring:spring /app

# 切换到非root用户
USER spring

# 暴露端口
EXPOSE 8080
EXPOSE 5005

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

# 入口点
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["java", "-jar", "app.jar"]
