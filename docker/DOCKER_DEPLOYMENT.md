# Qing 微服务 Docker Compose 部署指南

本文档详细介绍如何使用 Docker Compose 部署 Qing 微服务系统。

## 📋 目录

- [系统要求](#系统要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [服务架构](#服务架构)
- [部署步骤](#部署步骤)
- [服务管理](#服务管理)
- [故障排除](#故障排除)
- [性能优化](#性能优化)

## 🔧 系统要求

### 硬件要求

- **CPU**: 4核心以上
- **内存**: 8GB 以上
- **存储**: 20GB 以上可用空间
- **网络**: 稳定的互联网连接

### 软件要求

- **Docker**: 20.10.0 或更高版本
- **Docker Compose**: 2.0.0 或更高版本
- **操作系统**:
  - Linux (推荐 Ubuntu 20.04+, CentOS 8+)
  - macOS 10.15+
  - Windows 10/11 (需要 WSL2)

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd qing
```

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑配置文件
vim .env  # 或使用其他编辑器
```

### 3. 启动服务

**Linux/macOS:**

```bash
# 给脚本执行权限
chmod +x deploy.sh

# 启动所有服务
./deploy.sh start
```

**Windows:**

```cmd
# 运行批处理脚本
deploy.bat start
```

### 4. 验证部署

访问以下地址验证服务是否正常运行：

- API 网关: http://localhost:9527
- 认证服务: http://localhost:8088
- 动漫服务: http://localhost:8080
- Consul 控制台: http://localhost:8500

## ⚙️ 配置说明

### 环境变量配置 (.env)

#### 数据库配置

```env
# PostgreSQL 数据库配置
POSTGRES_DB=qing                    # 数据库名称
POSTGRES_USER=postgres              # 数据库用户名
POSTGRES_PASSWORD=admin@123456      # 数据库密码
POSTGRES_PORT=5432                  # 数据库端口
```

#### Redis 配置

```env
# Redis 缓存配置
REDIS_PORT=6379                     # Redis 端口
REDIS_PASSWORD=                     # Redis 密码（可选）
```

#### 服务端口配置

```env
# 各服务端口配置
GATEWAY_PORT=9527                   # API 网关端口
AUTH_SERVICE_PORT=8088              # 认证服务端口
ANIME_SERVICE_PORT=8080             # 动漫服务端口
CONFIG_SERVER_PORT=8888             # 配置中心端口
```

#### JWT 配置

```env
# JWT 令牌配置
JWT_SECRET=qingSecretKey            # JWT 密钥
JWT_EXPIRATION=18000                # JWT 过期时间（秒）
```

## 🏗️ 服务架构

### 基础设施服务

| 服务         | 端口         | 描述      |
|------------|------------|---------|
| PostgreSQL | 5432       | 主数据库    |
| Redis      | 6379       | 缓存服务    |
| Consul     | 8500       | 服务注册与发现 |
| MinIO      | 9000/9001  | 对象存储服务  |
| RabbitMQ   | 5672/15672 | 消息队列    |

### 应用服务

| 服务                    | 端口   | 描述          |
|-----------------------|------|-------------|
| qing-config-server    | 8888 | 配置中心        |
| qing-gateway          | 9527 | API 网关      |
| qing-auth-service     | 8088 | 认证服务        |
| qing-anime-service    | 8080 | 动漫服务        |
| qing-sa-token-service | 8089 | SA-Token 服务 |

### 监控服务

| 服务              | 端口    | 描述                |
|-----------------|-------|-------------------|
| qing-monitor    | 10086 | Spring Boot Admin |
| qing-bus-server | 8071  | 消息总线              |

## 📦 部署步骤

### 1. 准备工作

#### 检查系统要求

```bash
# 检查 Docker 版本
docker --version

# 检查 Docker Compose 版本
docker-compose --version

# 检查可用内存
free -h

# 检查磁盘空间
df -h
```

#### 配置 Docker

```bash
# 启动 Docker 服务
sudo systemctl start docker
sudo systemctl enable docker

# 将当前用户添加到 docker 组（可选）
sudo usermod -aG docker $USER
```

### 2. 构建镜像

如果需要从源码构建镜像：

```bash
# 构建所有镜像
./deploy.sh build

# 或者手动构建单个镜像
docker build -t qing/qing-config-server:latest qing-config-server/
docker build -t qing/qing-service-cloud-gateway:latest qing-service-cloud-gateway/
```

### 3. 分步启动

#### 启动基础设施服务

```bash
# 启动数据库和缓存
docker-compose up -d postgres redis consul

# 等待服务启动
sleep 30

# 检查服务状态
docker-compose ps
```

#### 启动应用服务

```bash
# 启动配置中心
docker-compose up -d qing-config-server

# 等待配置中心启动
sleep 30

# 启动其他应用服务
docker-compose up -d qing-gateway qing-auth-service qing-anime-service
```

### 4. 验证部署

```bash
# 检查所有服务状态
docker-compose ps

# 检查服务健康状态
docker-compose exec postgres pg_isready
docker-compose exec redis redis-cli ping

# 查看服务日志
docker-compose logs qing-gateway
```

## 🔧 服务管理

### 常用命令

```bash
# 启动所有服务
./deploy.sh start

# 停止所有服务
./deploy.sh stop

# 重启所有服务
./deploy.sh restart

# 查看服务状态
./deploy.sh status

# 查看服务日志
./deploy.sh logs [服务名]

# 清理所有数据
./deploy.sh clean
```

### 单独管理服务

```bash
# 启动单个服务
docker-compose up -d qing-gateway

# 停止单个服务
docker-compose stop qing-gateway

# 重启单个服务
docker-compose restart qing-gateway

# 查看单个服务日志
docker-compose logs -f qing-gateway

# 进入服务容器
docker-compose exec qing-gateway bash
```

### 扩展服务

```bash
# 扩展服务实例数量
docker-compose up -d --scale qing-anime-service=3

# 查看扩展后的服务
docker-compose ps
```

## 🔍 故障排除

### 常见问题

#### 1. 服务启动失败

**问题**: 服务容器启动后立即退出

**解决方案**:

```bash
# 查看服务日志
docker-compose logs qing-gateway

# 检查配置文件
cat .env

# 检查网络连接
docker network ls
docker network inspect qing_qing-network
```

#### 2. 数据库连接失败

**问题**: 应用服务无法连接到数据库

**解决方案**:

```bash
# 检查数据库服务状态
docker-compose ps postgres

# 测试数据库连接
docker-compose exec postgres psql -U postgres -d qing -c "SELECT 1;"

# 检查网络连通性
docker-compose exec qing-auth-service ping postgres
```

#### 3. 端口冲突

**问题**: 端口已被占用

**解决方案**:

```bash
# 查看端口占用
netstat -tulpn | grep :8080

# 修改 .env 文件中的端口配置
vim .env

# 重启服务
docker-compose down
docker-compose up -d
```

#### 4. 内存不足

**问题**: 容器因内存不足被杀死

**解决方案**:

```bash
# 检查系统内存使用
free -h

# 检查 Docker 内存使用
docker stats

# 调整容器内存限制
# 在 docker-compose.yml 中添加:
# deploy:
#   resources:
#     limits:
#       memory: 512M
```

### 日志分析

```bash
# 查看所有服务日志
docker-compose logs

# 查看特定服务日志
docker-compose logs qing-gateway

# 实时跟踪日志
docker-compose logs -f qing-gateway

# 查看最近的日志
docker-compose logs --tail=100 qing-gateway

# 根据时间过滤日志
docker-compose logs --since="2024-01-01T00:00:00" qing-gateway
```

## ⚡ 性能优化

### 1. 资源限制

在 `docker-compose.yml` 中为每个服务设置资源限制：

```yaml
services:
  qing-gateway:
    # ... 其他配置
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 512M
        reservations:
          cpus: '0.5'
          memory: 256M
```

### 2. 数据库优化

#### PostgreSQL 优化

```sql
-- 调整 PostgreSQL 配置
ALTER
SYSTEM SET shared_buffers = '256MB';
ALTER
SYSTEM SET effective_cache_size = '1GB';
ALTER
SYSTEM SET maintenance_work_mem = '64MB';
SELECT pg_reload_conf();
```

#### Redis 优化

```bash
# 在 config/redis.conf 中调整配置
maxmemory 512mb
maxmemory-policy allkeys-lru
```

### 3. 网络优化

```yaml
# 使用自定义网络
networks:
  qing-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

### 4. 存储优化

```yaml
# 使用命名卷提高性能
volumes:
  postgres_data:
    driver: local
    driver_opts:
      type: none
      o: bind
      device: /opt/qing/postgres
```

## 🔒 安全配置

### 1. 密码安全

```bash
# 生成强密码
openssl rand -base64 32

# 在 .env 文件中设置强密码
POSTGRES_PASSWORD=$(openssl rand -base64 32)
REDIS_PASSWORD=$(openssl rand -base64 32)
```

### 2. 网络安全

```yaml
# 限制服务只在内部网络通信
services:
  postgres:
    networks:
      - qing-network
    # 不暴露端口到主机
    # ports:
    #   - "5432:5432"
```

### 3. 文件权限

```bash
# 设置配置文件权限
chmod 600 .env
chmod 644 docker-compose.yml
```

## 📊 监控和维护

### 1. 健康检查

所有服务都配置了健康检查，可以通过以下命令查看：

```bash
# 查看服务健康状态
docker-compose ps

# 查看详细健康检查信息
docker inspect qing-gateway | grep -A 10 Health
```

### 2. 备份和恢复

#### 数据库备份

```bash
# 备份数据库
docker-compose exec postgres pg_dump -U postgres qing > backup_$(date +%Y%m%d_%H%M%S).sql

# 恢复数据库
docker-compose exec -T postgres psql -U postgres qing < backup_20240101_120000.sql
```

#### 配置备份

```bash
# 备份配置文件
tar -czf qing_config_backup_$(date +%Y%m%d_%H%M%S).tar.gz .env docker-compose.yml config/
```

### 3. 日志轮转

```yaml
# 在 docker-compose.yml 中配置日志轮转
services:
  qing-gateway:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

## 🆙 升级指南

### 1. 服务升级

```bash
# 拉取最新镜像
docker-compose pull

# 重启服务以使用新镜像
docker-compose up -d

# 清理旧镜像
docker image prune
```

### 2. 配置升级

```bash
# 备份当前配置
cp .env .env.backup
cp docker-compose.yml docker-compose.yml.backup

# 更新配置文件
# ... 手动编辑或使用脚本更新

# 重启服务
docker-compose down
docker-compose up -d
```

## 📞 支持和帮助

如果在部署过程中遇到问题，可以通过以下方式获取帮助：

1. **查看日志**: 使用 `./deploy.sh logs` 查看详细日志
2. **检查配置**: 确认 `.env` 文件配置正确
3. **重启服务**: 尝试重启相关服务
4. **清理重建**: 使用 `./deploy.sh clean` 清理后重新部署

## 📝 更新日志

- **v1.0.0** (2024-01-01): 初始版本，支持基础服务部署
- **v1.1.0** (2024-01-15): 添加监控服务和性能优化
- **v1.2.0** (2024-02-01): 增强安全配置和备份功能

---

**注意**: 本部署方案适用于开发和测试环境。生产环境部署请根据实际需求进行相应的安全加固和性能优化。
