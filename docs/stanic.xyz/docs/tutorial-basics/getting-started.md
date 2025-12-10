---
sidebar_position: 1
---

# 快速开始

本指南将帮助您快速搭建和运行青（Qing）项目的开发环境。

## 📋 环境准备

### 必需软件

- **Java Development Kit (JDK)**: 17 或更高版本
- **Maven**: 3.8 或更高版本
- **Node.js**: 18.0 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本

### 开发工具推荐

- **IDE**: IntelliJ IDEA 或 Visual Studio Code
- **数据库管理**: Navicat 或 DBeaver
- **API测试**: Postman 或 Apifox
- **Git客户端**: SourceTree 或命令行

## 🚀 项目启动

### 1. 克隆项目

```bash
# 克隆后端项目
git clone https://github.com/stanic-xyz/qing.git
# 或使用 Gitee 镜像（国内访问更快）
# git clone https://gitee.com/stanChen/qing.git
cd qing

# 克隆前端项目（可选）
git clone https://github.com/stanic-xyz/qing-frontend.git
# 或使用 Gitee 镜像
# git clone https://gitee.com/stanChen/qing-frontend.git
```

### 2. 数据库配置

#### 创建数据库

```sql
-- 创建主数据库
CREATE DATABASE qing_main DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建动漫数据库
CREATE DATABASE qing_anime DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建认证数据库
CREATE DATABASE qing_auth DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 配置数据库连接

修改 `application-dev.yml` 文件中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/qing_main?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    rawPassword: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. Redis 配置

确保 Redis 服务正在运行，默认配置：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    rawPassword: # 如果有密码请填写
    database: 0
```

### 4. 启动后端服务

#### 方式一：IDE 启动

1. 使用 IntelliJ IDEA 打开项目
2. 等待 Maven 依赖下载完成
3. 找到各个模块的主启动类：

- `QingEurekaServerApplication` - 服务注册中心
- `QingConfigServerApplication` - 配置中心
- `QingGatewayApplication` - 网关服务
- `AnimeApplication` - 动漫服务
- `AuthApplication` - 认证服务

4. 按顺序启动各个服务

#### 方式二：命令行启动

```bash
# 编译项目
mvn clean compile

# 启动服务注册中心
cd qing-eureka-server
mvn spring-boot:run

# 启动配置中心（新终端）
cd qing-config-server
mvn spring-boot:run

# 启动网关服务（新终端）
cd qing-gateway
mvn spring-boot:run

# 启动业务服务（新终端）
cd qing-services/qing-service-anime
mvn spring-boot:run
```

### 5. 启动前端项目（可选）

```bash
cd qing-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

## 🔍 验证安装

### 检查服务状态

1. **Eureka 控制台**: http://localhost:8761
2. **网关健康检查**: http://localhost:8080/actuator/health
3. **动漫服务 API 文档**: http://localhost:8080/anime/doc.html
4. **前端应用**: http://localhost:3000

### 测试 API 接口

使用 Postman 或 curl 测试基础接口：

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 获取动漫列表
curl http://localhost:8080/anime/api/v1/animes
```

## 🛠️ 开发配置

### IDE 配置

#### IntelliJ IDEA

1. **导入代码风格**：

- File → Settings → Editor → Code Style
- 导入项目根目录下的 `code-style.xml`

2. **配置 Checkstyle**：

- File → Settings → Tools → Checkstyle
- 添加配置文件：`checkstyle/checkstyle.xml`

3. **启用 EditorConfig**：

- File → Settings → Editor → Code Style
- 勾选 "Enable EditorConfig support"

#### Visual Studio Code

推荐安装以下扩展：

- Extension Pack for Java
- EditorConfig for VS Code
- Checkstyle for Java
- Spring Boot Extension Pack

### 环境变量配置

创建 `.env` 文件（可选）：

```bash
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_USERNAME=your_username
DB_PASSWORD=your_password

# Redis 配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# 应用配置
SERVER_PORT=8080
EUREKA_SERVER_URL=http://localhost:8761/eureka
```

## 🐛 常见问题

### 端口冲突

如果遇到端口冲突，可以修改各服务的端口配置：

```yaml
server:
  port: 8081  # 修改为可用端口
```

### 数据库连接失败

1. 检查 MySQL 服务是否启动
2. 验证数据库用户名和密码
3. 确认数据库已创建
4. 检查防火墙设置

### Maven 依赖下载失败

配置国内镜像源，在 `~/.m2/settings.xml` 中添加：

```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Aliyun Central</name>
    <url>https://maven.aliyun.com/repository/central</url>
  </mirror>
</mirrors>
```

### Redis 连接失败

1. 检查 Redis 服务是否启动：`redis-cli ping`
2. 验证 Redis 配置
3. 检查防火墙设置

## 📚 下一步

- 查看 [用户指南](./user-guide) 了解详细功能
- 阅读 [开发指南](../tutorial-extras/development) 学习开发规范
- 参考 [API 文档](../tutorial-extras/api-docs) 了解接口详情
- 查看 [部署指南](./deployment) 了解生产环境部署

## 🆘 获取帮助

如果在安装过程中遇到问题，可以通过以下方式获取帮助：

- 📖 查看完整文档
- 🐛 [提交 Issue](https://github.com/stanic-xyz/qing/issues)
- 💬 [参与讨论](https://github.com/stanic-xyz/qing/discussions)
- 📧 发送邮件：support@example.com
