---
sidebar_position: 2
---

# 用户指南

本指南将详细介绍青（Qing）项目的功能特性和使用方法。

## 🎯 项目概述

青（Qing）是一个基于 Spring Cloud 微服务架构的现代化应用平台，专注于提供高性能、可扩展的企业级解决方案。

### 核心特性

- **微服务架构**：基于 Spring Cloud 构建的分布式系统
- **服务治理**：集成 Eureka 服务注册与发现
- **配置管理**：统一的配置中心管理
- **API 网关**：统一的入口和路由管理
- **动漫管理**：专业的动漫信息管理系统
- **用户认证**：完整的用户认证和授权体系

## 🏗️ 系统架构

### 服务模块

```
qing/
├── qing-eureka-server/     # 服务注册中心
├── qing-config-server/     # 配置中心
├── qing-gateway/           # API 网关
├── qing-services/          # 业务服务
│   ├── qing-service-anime/ # 动漫服务
│   └── qing-service-auth/  # 认证服务
└── qing-common/            # 公共组件
```

### 技术栈

- **后端框架**：Spring Boot 3.x, Spring Cloud 2023.x
- **数据库**：MySQL 8.0+, Redis 6.0+
- **消息队列**：RabbitMQ（可选）
- **监控**：Spring Boot Actuator, Micrometer
- **文档**：Swagger/OpenAPI 3.0
- **构建工具**：Maven 3.8+

## 🚀 功能模块

### 1. 服务注册中心 (Eureka Server)

**功能描述**：提供服务注册与发现功能，管理所有微服务实例。

**访问地址**：http://localhost:8761

**主要功能**：

- 服务实例注册
- 服务健康检查
- 服务实例列表查看
- 服务状态监控

### 2. 配置中心 (Config Server)

**功能描述**：集中管理所有服务的配置信息，支持配置的动态刷新。

**配置仓库**：https://github.com/stanic-xyz/qing-config

**主要功能**：

- 集中配置管理
- 环境隔离（dev/test/prod）
- 配置版本控制
- 动态配置刷新

### 3. API 网关 (Gateway)

**功能描述**：统一的API入口，提供路由、过滤、限流等功能。

**访问地址**：http://localhost:8080

**主要功能**：

- 请求路由转发
- 跨域处理
- 请求限流
- 身份认证
- 日志记录

### 4. 动漫服务 (Anime Service)

**功能描述**：提供动漫信息的管理和查询功能。

**API文档**：http://localhost:8080/anime/doc.html

#### 主要功能

##### 动漫管理

- **动漫信息录入**：支持动漫基本信息、分类、标签等
- **动漫信息查询**：支持多条件搜索和分页
- **动漫信息更新**：支持动漫信息的修改和删除
- **动漫分类管理**：支持动漫分类的增删改查

##### 数据接口

```http
# 获取动漫列表
GET /anime/api/v1/animes

# 获取动漫详情
GET /anime/api/v1/animes/{id}

# 创建动漫
POST /anime/api/v1/animes

# 更新动漫
PUT /anime/api/v1/animes/{id}

# 删除动漫
DELETE /anime/api/v1/animes/{id}
```

##### 查询参数

- `page`：页码（从0开始）
- `size`：每页大小
- `sort`：排序字段
- `name`：动漫名称（模糊查询）
- `category`：分类筛选
- `status`：状态筛选

### 5. 认证服务 (Auth Service)

**功能描述**：提供用户认证、授权和用户管理功能。

**主要功能**：

- 用户注册和登录
- JWT Token 生成和验证
- 用户权限管理
- 密码加密和验证

## 📱 前端应用

### 技术栈

- **框架**：React 18+ / Vue 3+
- **UI库**：Ant Design / Element Plus
- **状态管理**：Redux Toolkit / Pinia
- **路由**：React Router / Vue Router
- **HTTP客户端**：Axios

### 主要页面

1. **首页**：项目介绍和快速导航
2. **动漫列表**：动漫信息浏览和搜索
3. **动漫详情**：详细的动漫信息展示
4. **用户中心**：用户信息管理
5. **管理后台**：系统管理功能

## 🔧 配置说明

### 数据库配置

#### MySQL 配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qing_main?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

#### Redis 配置

```yaml
spring:
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: 0
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
```

### 服务配置

#### Eureka 客户端配置

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
```

#### Gateway 路由配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: anime-service
          uri: lb://anime-service
          predicates:
            - Path=/anime/**
        - id: auth-service
          uri: lb://auth-service
          predicates:
            - Path=/auth/**
```

## 🔍 API 使用示例

### 动漫服务 API

#### 获取动漫列表

```bash
curl -X GET "http://localhost:8080/anime/api/v1/animes?page=0&size=10" \
  -H "Accept: application/json"
```

响应示例：

```json
{
  "content": [
    {
      "id": 1,
      "name": "进击的巨人",
      "description": "人类与巨人的战斗故事",
      "category": "动作",
      "status": "已完结",
      "createTime": "2024-01-01T00:00:00"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "size": 10,
  "number": 0
}
```

#### 创建动漫

```bash
curl -X POST "http://localhost:8080/anime/api/v1/animes" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "新动漫",
    "description": "动漫描述",
    "category": "冒险",
    "status": "连载中"
  }'
```

### 认证服务 API

#### 用户登录

```bash
curl -X POST "http://localhost:8080/auth/api/v1/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

响应示例：

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "user@example.com",
    "roles": ["USER"]
  },
  "expiresIn": 3600
}
```

## 🔒 安全配置

### JWT 配置

```yaml
jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 86400 # 24小时
  refresh-expiration: 604800 # 7天
```

### CORS 配置

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins: "*"
            allowed-methods:
              - GET
              - POST
              - PUT
              - DELETE
            allowed-headers: "*"
```

## 📊 监控和日志

### 健康检查

所有服务都提供健康检查端点：

```bash
# 检查服务健康状态
curl http://localhost:8080/actuator/health

# 查看详细信息
curl http://localhost:8080/actuator/info
```

### 日志配置

```yaml
logging:
  level:
    com.stanic.qing: DEBUG
    org.springframework.cloud: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
```

## 🚀 性能优化

### 数据库优化

1. **连接池配置**：

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

2. **查询优化**：

- 使用索引优化查询性能
- 避免 N+1 查询问题
- 使用分页查询大数据集

### 缓存策略

1. **Redis 缓存**：

- 热点数据缓存
- 查询结果缓存
- 会话缓存

2. **本地缓存**：

- 配置信息缓存
- 静态数据缓存

## 🐛 故障排查

### 常见问题

1. **服务注册失败**

- 检查 Eureka Server 是否启动
- 验证网络连接
- 检查配置文件

2. **数据库连接失败**

- 检查数据库服务状态
- 验证连接参数
- 检查防火墙设置

3. **Redis 连接失败**

- 检查 Redis 服务状态
- 验证连接配置
- 检查网络连通性

### 日志分析

查看关键日志信息：

```bash
# 查看应用日志
tail -f logs/application.log

# 查看错误日志
grep "ERROR" logs/application.log

# 查看特定服务日志
grep "anime-service" logs/application.log
```

## 📚 相关资源

- [快速开始](./getting-started) - 项目安装和启动指南
- [开发指南](../tutorial-extras/development) - 开发规范和最佳实践
- [API 文档](../tutorial-extras/api-docs) - 详细的 API 接口文档
- [部署指南](./deployment) - 生产环境部署指南

## 🆘 获取帮助

如果您在使用过程中遇到问题，可以通过以下方式获取帮助：

- 📖 查看完整文档
- 🐛 [提交 Issue](https://github.com/stanic-xyz/qing/issues)
- 💬 [参与讨论](https://github.com/stanic-xyz/qing/discussions)
- 📧 发送邮件：support@example.com

---

> 💡 **提示**：本文档会持续更新，建议定期查看最新版本。
