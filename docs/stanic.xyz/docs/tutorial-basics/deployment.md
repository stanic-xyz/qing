---
sidebar_position: 3
---

# 部署指南

本指南将详细介绍青（Qing）项目在不同环境下的部署方法和最佳实践。

## 🎯 部署概述

青（Qing）项目支持多种部署方式，包括传统部署、容器化部署和云原生部署。本指南将逐步介绍各种部署方案。

### 部署架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Load Balancer │    │     Gateway     │    │   Eureka Server │
│    (Nginx)      │────│   (Port 8080)   │────│   (Port 8761)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Config Server  │    │  Anime Service  │    │   Auth Service  │
│   (Port 8888)   │    │   (Port 8081)   │    │   (Port 8082)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     MySQL       │    │      Redis      │    │   File Storage  │
│   (Port 3306)   │    │   (Port 6379)   │    │     (MinIO)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🏗️ 环境准备

### 系统要求

#### 最低配置

- **CPU**: 2核心
- **内存**: 4GB RAM
- **存储**: 20GB 可用空间
- **操作系统**: Linux (Ubuntu 20.04+, CentOS 7+) / Windows Server 2019+

#### 推荐配置

- **CPU**: 4核心或更多
- **内存**: 8GB RAM 或更多
- **存储**: 50GB SSD
- **网络**: 100Mbps 带宽

### 软件依赖

```bash
# Java 运行环境
Java 17+ (OpenJDK 推荐)

# 数据库
MySQL 8.0+
Redis 6.0+

# 反向代理 (可选)
Nginx 1.18+

# 容器化 (可选)
Docker 20.10+
Docker Compose 2.0+
```

## 📦 传统部署

### 1. 环境安装

#### 安装 Java

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel

# 验证安装
java -version
```

#### 安装 MySQL

```bash
# Ubuntu/Debian
sudo apt install mysql-server

# CentOS/RHEL
sudo yum install mysql-server

# 启动服务
sudo systemctl start mysql
sudo systemctl enable mysql

# 安全配置
sudo mysql_secure_installation
```

#### 安装 Redis

```bash
# Ubuntu/Debian
sudo apt install redis-server

# CentOS/RHEL
sudo yum install redis

# 启动服务
sudo systemctl start redis
sudo systemctl enable redis
```

### 2. 数据库初始化

#### 创建数据库

```sql
-- 连接到 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE qing_main DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE qing_anime DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE qing_auth DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户
CREATE USER 'qing'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON qing_*.* TO 'qing'@'%';
FLUSH PRIVILEGES;
```

#### 导入初始数据

```bash
# 导入数据库结构
mysql -u qing -p qing_main < sql/qing_main.sql
mysql -u qing -p qing_anime < sql/qing_anime.sql
mysql -u qing -p qing_auth < sql/qing_auth.sql
```

### 3. 应用部署

#### 构建应用

```bash
# 克隆项目
git clone https://github.com/stanic-xyz/qing.git
cd qing

# 构建项目
mvn clean package -DskipTests
```

#### 部署服务

```bash
# 创建部署目录
sudo mkdir -p /opt/qing/{eureka,config,gateway,anime,auth}
sudo mkdir -p /opt/qing/logs
sudo mkdir -p /opt/qing/config

# 复制 JAR 文件
sudo cp qing-eureka-server/target/qing-eureka-server-*.jar /opt/qing/eureka/
sudo cp qing-config-server/target/qing-config-server-*.jar /opt/qing/config/
sudo cp qing-gateway/target/qing-gateway-*.jar /opt/qing/gateway/
sudo cp qing-services/qing-service-anime/target/qing-service-anime-*.jar /opt/qing/anime/
sudo cp qing-services/qing-service-auth/target/qing-service-auth-*.jar /opt/qing/auth/
```

#### 配置文件

创建生产环境配置文件 `/opt/qing/config/application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qing_main?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: qing
    rawPassword: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  redis:
    host: localhost
    port: 6379
    rawPassword: your_redis_password
    database: 0

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

logging:
  level:
    com.stanic.qing: INFO
  file:
    name: /opt/qing/logs/application.log
```

#### 创建启动脚本

创建 `/opt/qing/start.sh`：

```bash
#!/bin/bash

# 设置 Java 选项
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
SPRING_PROFILES="prod"
CONFIG_PATH="/opt/qing/config"

# 启动 Eureka Server
echo "Starting Eureka Server..."
nohup java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES -Dspring.config.location=$CONFIG_PATH/ -jar /opt/qing/eureka/qing-eureka-server-*.jar > /opt/qing/logs/eureka.log 2>&1 &
sleep 30

# 启动 Config Server
echo "Starting Config Server..."
nohup java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES -Dspring.config.location=$CONFIG_PATH/ -jar /opt/qing/config/qing-config-server-*.jar > /opt/qing/logs/config.log 2>&1 &
sleep 20

# 启动 Gateway
echo "Starting Gateway..."
nohup java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES -Dspring.config.location=$CONFIG_PATH/ -jar /opt/qing/gateway/qing-gateway-*.jar > /opt/qing/logs/gateway.log 2>&1 &
sleep 20

# 启动业务服务
echo "Starting Anime Service..."
nohup java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES -Dspring.config.location=$CONFIG_PATH/ -jar /opt/qing/anime/qing-service-anime-*.jar > /opt/qing/logs/anime.log 2>&1 &

echo "Starting Auth Service..."
nohup java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES -Dspring.config.location=$CONFIG_PATH/ -jar /opt/qing/auth/qing-service-auth-*.jar > /opt/qing/logs/auth.log 2>&1 &

echo "All services started!"
```

#### 创建停止脚本

创建 `/opt/qing/stop.sh`：

```bash
#!/bin/bash

echo "Stopping Qing services..."

# 查找并停止所有 Qing 相关进程
pkill -f "qing-.*\.jar"

echo "All services stopped!"
```

#### 设置权限并启动

```bash
# 设置权限
sudo chmod +x /opt/qing/start.sh
sudo chmod +x /opt/qing/stop.sh
sudo chown -R qing:qing /opt/qing

# 启动服务
sudo -u qing /opt/qing/start.sh
```

### 4. 系统服务配置

#### 创建 systemd 服务

创建 `/etc/systemd/system/qing.service`：

```ini
[Unit]
Description=Qing Microservices
After=network.target mysql.service redis.service

[Service]
Type=forking
User=qing
Group=qing
ExecStart=/opt/qing/start.sh
ExecStop=/opt/qing/stop.sh
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 启用服务

```bash
# 重新加载 systemd
sudo systemctl daemon-reload

# 启用服务
sudo systemctl enable qing

# 启动服务
sudo systemctl start qing

# 查看状态
sudo systemctl status qing
```

## 🐳 容器化部署

### 1. Docker 镜像构建

#### 创建 Dockerfile

为每个服务创建 Dockerfile，以 Eureka Server 为例：

```dockerfile
# qing-eureka-server/Dockerfile
FROM openjdk:17-jre-slim

VOLUME /tmp

ARG JAR_FILE=target/qing-eureka-server-*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### 构建镜像

```bash
# 构建所有镜像
docker build -t qing/eureka-server:latest qing-eureka-server/
docker build -t qing/config-server:latest qing-config-server/
docker build -t qing/gateway:latest qing-gateway/
docker build -t qing/anime-service:latest qing-services/qing-service-anime/
docker build -t qing/auth-service:latest qing-services/qing-service-auth/
```

### 2. Docker Compose 部署

#### 创建 docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: qing-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: qing_main
      MYSQL_USER: qing
      MYSQL_PASSWORD: qingpassword
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d
    networks:
      - qing-network

  redis:
    image: redis:6.2-alpine
    container_name: qing-redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - qing-network

  eureka-server:
    image: qing/eureka-server:latest
    container_name: qing-eureka
    ports:
      - "8761:8761"
    environment:
      SPRING_PROFILES_ACTIVE: docker
    networks:
      - qing-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  config-server:
    image: qing/config-server:latest
    container_name: qing-config
    ports:
      - "8888:8888"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE: http://eureka-server:8761/eureka
    depends_on:
      eureka-server:
        condition: service_healthy
    networks:
      - qing-network

  gateway:
    image: qing/gateway:latest
    container_name: qing-gateway
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE: http://eureka-server:8761/eureka
    depends_on:
      - eureka-server
      - config-server
    networks:
      - qing-network

  anime-service:
    image: qing/anime-service:latest
    container_name: qing-anime
    environment:
      SPRING_PROFILES_ACTIVE: docker
      EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE: http://eureka-server:8761/eureka
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/qing_anime?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: qing
      SPRING_DATASOURCE_PASSWORD: qingpassword
      SPRING_REDIS_HOST: redis
    depends_on:
      - mysql
      - redis
      - eureka-server
    networks:
      - qing-network

  auth-service:
    image: qing/auth-service:latest
    container_name: qing-auth
    environment:
      SPRING_PROFILES_ACTIVE: docker
      EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE: http://eureka-server:8761/eureka
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/qing_auth?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      SPRING_DATASOURCE_USERNAME: qing
      SPRING_DATASOURCE_PASSWORD: qingpassword
      SPRING_REDIS_HOST: redis
    depends_on:
      - mysql
      - redis
      - eureka-server
    networks:
      - qing-network

volumes:
  mysql_data:
  redis_data:

networks:
  qing-network:
    driver: bridge
```

#### 启动容器

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## ☁️ 云原生部署 (Kubernetes)

### 1. 准备 Kubernetes 清单

#### 命名空间

```yaml
# namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: qing
```

#### ConfigMap

```yaml
# configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: qing-config
  namespace: qing
data:
  application.yml: |
    spring:
      profiles:
        active: k8s
    eureka:
      client:
        service-url:
          defaultZone: http://qing-eureka:8761/eureka
```

#### MySQL 部署

```yaml
# mysql-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: qing
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        env:
        - name: MYSQL_ROOT_PASSWORD
          value: "rootpassword"
        - name: MYSQL_DATABASE
          value: "qing_main"
        - name: MYSQL_USER
          value: "qing"
        - name: MYSQL_PASSWORD
          value: "qingpassword"
        ports:
        - containerPort: 3306
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
      volumes:
      - name: mysql-storage
        persistentVolumeClaim:
          claimName: mysql-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: mysql
  namespace: qing
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
```

#### Eureka Server 部署

```yaml
# eureka-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: qing-eureka
  namespace: qing
spec:
  replicas: 1
  selector:
    matchLabels:
      app: qing-eureka
  template:
    metadata:
      labels:
        app: qing-eureka
    spec:
      containers:
      - name: eureka
        image: qing/eureka-server:latest
        ports:
        - containerPort: 8761
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "k8s"
        volumeMounts:
        - name: config-volume
          mountPath: /config
      volumes:
      - name: config-volume
        configMap:
          name: qing-config
---
apiVersion: v1
kind: Service
metadata:
  name: qing-eureka
  namespace: qing
spec:
  selector:
    app: qing-eureka
  ports:
  - port: 8761
    targetPort: 8761
  type: ClusterIP
```

### 2. 部署到 Kubernetes

```bash
# 应用所有清单
kubectl apply -f k8s/

# 查看部署状态
kubectl get pods -n qing
kubectl get services -n qing

# 查看日志
kubectl logs -f deployment/qing-eureka -n qing

# 端口转发（用于测试）
kubectl port-forward service/qing-gateway 8080:8080 -n qing
```

## 🔧 负载均衡配置

### Nginx 配置

```nginx
# /etc/nginx/sites-available/qing
upstream qing_gateway {
    server 127.0.0.1:8080;
    # 如果有多个网关实例
    # server 127.0.0.1:8081;
}

server {
    listen 80;
    server_name your-domain.com;

    # 重定向到 HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;

    # SSL 证书配置
    ssl_certificate /path/to/your/certificate.crt;
    ssl_certificate_key /path/to/your/private.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384;
    ssl_prefer_server_ciphers off;

    # 安全头
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
    add_header Strict-Transport-Security "max-age=63072000; includeSubDomains; preload";

    # 代理配置
    location / {
        proxy_pass http://qing_gateway;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket 支持
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        
        # 超时配置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 静态文件缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        proxy_pass http://qing_gateway;
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # 健康检查
    location /health {
        access_log off;
        proxy_pass http://qing_gateway/actuator/health;
    }
}
```

## 📊 监控和日志

### 1. 应用监控

#### Prometheus 配置

```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'qing-services'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080', 'localhost:8081', 'localhost:8082']
```

#### Grafana 仪表板

导入 Spring Boot 监控仪表板，监控关键指标：

- JVM 内存使用
- HTTP 请求量和响应时间
- 数据库连接池状态
- 缓存命中率

### 2. 日志聚合

#### ELK Stack 配置

```yaml
# docker-compose-elk.yml
version: '3.8'

services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.15.0
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch_data:/usr/share/elasticsearch/data

  logstash:
    image: docker.elastic.co/logstash/logstash:7.15.0
    volumes:
      - ./logstash/config:/usr/share/logstash/pipeline
    ports:
      - "5044:5044"
    depends_on:
      - elasticsearch

  kibana:
    image: docker.elastic.co/kibana/kibana:7.15.0
    ports:
      - "5601:5601"
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
    depends_on:
      - elasticsearch

volumes:
  elasticsearch_data:
```

## 🔒 安全配置

### 1. 防火墙配置

```bash
# Ubuntu/Debian (UFW)
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw allow from 10.0.0.0/8 to any port 8761  # Eureka (内网)
sudo ufw enable

# CentOS/RHEL (firewalld)
sudo firewall-cmd --permanent --add-service=ssh
sudo firewall-cmd --permanent --add-service=http
sudo firewall-cmd --permanent --add-service=https
sudo firewall-cmd --permanent --add-port=8761/tcp --source=10.0.0.0/8
sudo firewall-cmd --reload
```

### 2. SSL/TLS 配置

#### 获取 Let's Encrypt 证书

```bash
# 安装 Certbot
sudo apt install certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d your-domain.com

# 自动续期
sudo crontab -e
# 添加：0 12 * * * /usr/bin/certbot renew --quiet
```

### 3. 数据库安全

```sql
-- 创建只读用户
CREATE USER 'qing_readonly'@'%' IDENTIFIED BY 'readonly_password';
GRANT SELECT ON qing_*.* TO 'qing_readonly'@'%';

-- 限制连接数
ALTER USER 'qing'@'%' WITH MAX_CONNECTIONS_PER_HOUR 100;

-- 启用 SSL
-- 在 my.cnf 中添加：
-- [mysqld]
-- ssl-ca=/path/to/ca.pem
-- ssl-cert=/path/to/server-cert.pem
-- ssl-key=/path/to/server-key.pem
```

## 🚀 性能优化

### 1. JVM 调优

```bash
# 生产环境 JVM 参数
JAVA_OPTS="
  -Xms2g -Xmx4g
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -XX:+UseStringDeduplication
  -XX:+OptimizeStringConcat
  -Djava.security.egd=file:/dev/./urandom
  -Dspring.profiles.active=prod
"
```

### 2. 数据库优化

```sql
-- MySQL 配置优化 (my.cnf)
[mysqld]
innodb_buffer_pool_size = 2G
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 2
query_cache_size = 128M
max_connections = 200
```

### 3. Redis 优化

```bash
# Redis 配置优化 (redis.conf)
maxmemory 1gb
maxmemory-policy allkeys-lru
save 900 1
save 300 10
save 60 10000
```

## 🔄 备份和恢复

### 1. 数据库备份

```bash
#!/bin/bash
# backup.sh

DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backup/mysql"
DB_USER="qing"
DB_PASS="your_password"

# 创建备份目录
mkdir -p $BACKUP_DIR

# 备份所有 qing 数据库
mysqldump -u$DB_USER -p$DB_PASS --single-transaction --routines --triggers qing_main > $BACKUP_DIR/qing_main_$DATE.sql
mysqldump -u$DB_USER -p$DB_PASS --single-transaction --routines --triggers qing_anime > $BACKUP_DIR/qing_anime_$DATE.sql
mysqldump -u$DB_USER -p$DB_PASS --single-transaction --routines --triggers qing_auth > $BACKUP_DIR/qing_auth_$DATE.sql

# 压缩备份文件
tar -czf $BACKUP_DIR/qing_backup_$DATE.tar.gz $BACKUP_DIR/*_$DATE.sql

# 删除原始 SQL 文件
rm $BACKUP_DIR/*_$DATE.sql

# 删除 7 天前的备份
find $BACKUP_DIR -name "qing_backup_*.tar.gz" -mtime +7 -delete

echo "Backup completed: qing_backup_$DATE.tar.gz"
```

### 2. 自动备份

```bash
# 添加到 crontab
crontab -e

# 每天凌晨 2 点备份
0 2 * * * /opt/qing/scripts/backup.sh
```

## 🐛 故障排查

### 1. 常见问题

#### 服务启动失败

```bash
# 检查端口占用
netstat -tlnp | grep :8080

# 检查 Java 进程
jps -l

# 查看详细错误日志
tail -f /opt/qing/logs/application.log
```

#### 内存不足

```bash
# 检查内存使用
free -h
top -p $(pgrep -f "qing.*jar")

# 检查 JVM 内存
jstat -gc $(pgrep -f "qing.*jar")
```

#### 数据库连接问题

```bash
# 测试数据库连接
mysql -h localhost -u qing -p -e "SELECT 1;"

# 检查连接数
mysql -u root -p -e "SHOW PROCESSLIST;"
```

### 2. 健康检查脚本

```bash
#!/bin/bash
# health-check.sh

SERVICES=("eureka:8761" "gateway:8080" "anime:8081" "auth:8082")

for service in "${SERVICES[@]}"; do
    name=$(echo $service | cut -d: -f1)
    port=$(echo $service | cut -d: -f2)
    
    if curl -f -s "http://localhost:$port/actuator/health" > /dev/null; then
        echo "✅ $name service is healthy"
    else
        echo "❌ $name service is unhealthy"
        # 发送告警通知
        # send_alert "$name service is down"
    fi
done
```

## 📚 相关资源

- [快速开始](./getting-started) - 项目安装和启动指南
- [用户指南](./user-guide) - 详细的功能使用说明
- [开发指南](../tutorial-extras/development) - 开发规范和最佳实践
- [API 文档](../tutorial-extras/api-docs) - 详细的 API 接口文档

## 🆘 获取帮助

如果在部署过程中遇到问题，可以通过以下方式获取帮助：

- 📖 查看完整文档
- 🐛 [提交 Issue](https://github.com/stanic-xyz/qing/issues)
- 💬 [参与讨论](https://github.com/stanic-xyz/qing/discussions)
- 📧 发送邮件：support@example.com

---

> 💡 **提示**：建议在生产环境部署前，先在测试环境验证所有配置和流程。
