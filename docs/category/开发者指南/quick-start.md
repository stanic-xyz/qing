# 快速开始

本指南将帮助您快速搭建青（Qing）项目的开发环境，并运行第一个微服务。

## 🎯 前置要求

在开始之前，请确保您的开发环境满足以下要求：

### 必需软件

- **Java 17+** - 项目基于 Java 17 开发
- **Maven 3.8+** - 用于项目构建和依赖管理
- **Git** - 版本控制工具
- **Docker** - 用于运行基础设施服务
- **Docker Compose** - 用于编排多个容器

### 推荐工具

- **IntelliJ IDEA** - 推荐的 IDE
- **Postman** - API 测试工具
- **Redis Desktop Manager** - Redis 可视化工具
- **Navicat** - 数据库管理工具

## 📥 获取源码

### 1. 克隆项目

```bash
# 从 GitHub 克隆（推荐）
git clone https://github.com/stanic-xyz/qing.git
# 或者使用 Gitee（国内访问更快）
git clone https://gitee.com/stanic-xyz/qing.git

# 进入项目目录
cd qing
```

### 2. 检查项目结构

```bash
# 查看项目结构
tree -L 2

# 或使用 ls 命令
ls -la
```

## 🐳 启动基础设施

项目依赖一些基础设施服务，我们使用 Docker Compose 来快速启动它们。

### 1. 启动基础服务

```bash
# 进入脚本目录
cd scripts

# 启动基础设施服务（MySQL、Redis、RabbitMQ 等）
docker-compose up -d

# 查看服务状态
docker-compose ps
```

### 2. 验证服务状态

```bash
# 检查 MySQL
docker-compose logs mysql

# 检查 Redis
docker-compose logs redis

# 检查 RabbitMQ
docker-compose logs rabbitmq
```

### 3. 访问管理界面

- **RabbitMQ 管理界面**：http://localhost:15672
  - 用户名：`admin`
  - 密码：`admin123`

- **Redis**：localhost:6379
- **MySQL**：localhost:3306
  - 用户名：`root`
  - 密码：`root123`

## 🏗️ 构建项目

### 1. 编译整个项目

```bash
# 返回项目根目录
cd ..

# 清理并编译项目
mvn clean compile

# 运行测试
mvn test

# 打包项目
mvn clean package -DskipTests
```

### 2. 验证构建结果

```bash
# 检查构建产物
find . -name "*.jar" -type f
```

## 🚀 启动服务

按照以下顺序启动微服务：

### 1. 启动服务注册中心

```bash
# 进入 Eureka 服务目录
cd qing-eureka-server

# 启动服务
mvn spring-boot:run
```

等待服务启动完成，访问 http://localhost:8761 查看 Eureka 控制台。

### 2. 启动配置中心

打开新的终端窗口：

```bash
# 进入配置服务目录
cd qing-config-server

# 启动服务
mvn spring-boot:run
```

### 3. 启动 API 网关

打开新的终端窗口：

```bash
# 进入网关服务目录
cd qing-service-cloud-gateway

# 启动服务
mvn spring-boot:run
```

### 4. 启动业务服务

#### 启动认证服务

```bash
# 进入认证服务目录
cd qing-services/qing-service-auth/qing-service-auth-app

# 启动服务
mvn spring-boot:run
```

#### 启动动漫服务

```bash
# 进入动漫服务目录
cd qing-services/qing-service-anime/qing-service-anime-app

# 启动服务
mvn spring-boot:run
```

## 🧪 验证部署

### 1. 检查服务注册

访问 Eureka 控制台：http://localhost:8761

您应该看到以下服务已注册：

- `qing-config-server`
- `qing-gateway`
- `qing-service-auth`
- `qing-service-anime`

### 2. 测试 API 接口

#### 健康检查

```bash
# 通过网关访问健康检查接口
curl http://localhost:8080/actuator/health

# 直接访问动漫服务
curl http://localhost:8081/actuator/health
```

#### 获取动漫列表

```bash
# 通过网关访问动漫服务
curl http://localhost:8080/anime/api/v1/animes
```

### 3. 使用 Postman 测试

导入项目中的 Postman 集合文件：`docs/postman/qing-api.postman_collection.json`

## 🎉 恭喜！

如果您能看到以上所有服务正常运行，说明您已经成功搭建了青（Qing）项目的开发环境！

## 🔧 开发工具配置

### IntelliJ IDEA 配置

1. **导入项目**

- 选择 "Open or Import"
- 选择项目根目录的 `pom.xml`
- 选择 "Open as Project"

2. **配置 JDK**

- File → Project Structure → Project
- 设置 Project SDK 为 Java 17

3. **安装推荐插件**

- Lombok
- MyBatis Log Plugin
- RestfulTool
- Maven Helper

### 代码格式化

项目提供了统一的代码格式化配置：

```bash
# 导入 IDEA 代码格式化配置
# File → Settings → Editor → Code Style → Java
# 点击齿轮图标 → Import Scheme → IntelliJ IDEA code style XML
# 选择 checkstyle/idea-java-style.xml
```

## 📚 下一步

现在您已经成功运行了青（Qing）项目，可以继续学习：

- [项目结构说明](./project-structure) - 深入了解项目架构
- [编码规范](./coding-standards) - 学习编码规范
- [API 设计规范](./api-standards) - 了解 API 设计原则
- [单元测试](./unit-testing) - 编写高质量的测试代码

## ❓ 常见问题

### Q: 服务启动失败怎么办？

A: 请检查以下几点：

1. 确保基础设施服务（MySQL、Redis 等）正常运行
2. 检查端口是否被占用
3. 查看服务日志，定位具体错误
4. 确保 Java 版本为 17+

### Q: 无法访问 Eureka 控制台？

A: 请确认：

1. Eureka 服务已正常启动
2. 端口 8761 未被占用
3. 防火墙设置允许访问

### Q: 数据库连接失败？

A: 请检查：

1. MySQL 容器是否正常运行
2. 数据库连接配置是否正确
3. 数据库用户权限是否足够

---

如果您遇到其他问题，请查看 [常见问题](../文档/faq) 或在 GitHub 上提出 Issue。
