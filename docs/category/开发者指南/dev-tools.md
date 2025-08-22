# 开发工具配置

本文档介绍青（Qing）项目推荐的开发工具及其配置方法，帮助开发者快速搭建高效的开发环境。

## 🛠️ 必需工具

### Java 开发环境

#### 1. JDK 17+

**推荐版本**：OpenJDK 17 或 Oracle JDK 17

```bash
# 验证 Java 版本
java -version
javac -version

# 应该显示类似输出
# openjdk version "17.0.2" 2022-01-18
```

**环境变量配置**：

```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

# Linux/macOS
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

#### 2. Maven 3.8+

**安装验证**：

```bash
mvn -version

# 应该显示类似输出
# Apache Maven 3.8.6
# Maven home: /usr/share/maven
# Java version: 17.0.2
```

**配置文件** (`~/.m2/settings.xml`)：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <!-- 本地仓库路径 -->
    <localRepository>${user.home}/.m2/repository</localRepository>
    
    <!-- 镜像配置（可选，提高下载速度） -->
    <mirrors>
        <mirror>
            <id>aliyun-maven</id>
            <name>Aliyun Maven Repository</name>
            <url>https://maven.aliyun.com/repository/public</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
    
    <!-- 配置文件激活 -->
    <activeProfiles>
        <activeProfile>dev</activeProfile>
    </activeProfiles>
    
    <profiles>
        <profile>
            <id>dev</id>
            <properties>
                <maven.compiler.source>17</maven.compiler.source>
                <maven.compiler.target>17</maven.compiler.target>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            </properties>
        </profile>
    </profiles>
</settings>
```

### 容器化工具

#### 1. Docker

**安装验证**：

```bash
docker --version
docker-compose --version

# 测试运行
docker run hello-world
```

**推荐配置**：

```json
// Docker Desktop 配置
{
  "builder": {
    "gc": {
      "defaultKeepStorage": "20GB",
      "enabled": true
    }
  },
  "experimental": false,
  "features": {
    "buildkit": true
  }
}
```

#### 2. Docker Compose

项目根目录的 `docker-compose.yml` 用于启动开发环境：

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 停止所有服务
docker-compose down
```

## 🎯 IDE 配置

### IntelliJ IDEA（推荐）

#### 1. 基础配置

**推荐版本**：IntelliJ IDEA 2023.1+

**必装插件**：

```
- Lombok                    # 简化 Java 代码
- Spring Boot Helper        # Spring Boot 支持
- Maven Helper             # Maven 依赖管理
- GitToolBox               # Git 增强
- SonarLint                # 代码质量检查
- CheckStyle-IDEA          # 代码风格检查
- Rainbow Brackets         # 彩色括号
- String Manipulation      # 字符串处理
- Translation              # 翻译插件
- Alibaba Java Coding Guidelines  # 阿里巴巴编码规范
```

#### 2. 代码格式化配置

**导入代码风格**：

1. File → Settings → Editor → Code Style → Java
2. 点击齿轮图标 → Import Scheme → IntelliJ IDEA code style XML
3. 选择项目中的 `checkstyle/idea-java-style.xml`

**自动格式化配置**：

```
File → Settings → Tools → Actions on Save
☑ Reformat code
☑ Optimize imports
☑ Rearrange code
☑ Run code cleanup
```

#### 3. 项目配置

**JDK 配置**：

```
File → Project Structure → Project
- Project SDK: 17
- Project language level: 17
```

**Maven 配置**：

```
File → Settings → Build → Build Tools → Maven
- Maven home path: /usr/share/maven
- User settings file: ~/.m2/settings.xml
- Local repository: ~/.m2/repository
```

**编码配置**：

```
File → Settings → Editor → File Encodings
- Global Encoding: UTF-8
- Project Encoding: UTF-8
- Default encoding for properties files: UTF-8
☑ Transparent native-to-ascii conversion
```

#### 4. 运行配置模板

**Spring Boot 应用配置**：

```
Run → Edit Configurations → Templates → Spring Boot
- Main class: xyz.stanic.qing.QingApplication
- Program arguments: --spring.profiles.active=dev
- VM options: -Xmx1024m -Dfile.encoding=UTF-8
- Environment variables: SPRING_PROFILES_ACTIVE=dev
```

**JUnit 测试配置**：

```
Run → Edit Configurations → Templates → JUnit
- VM options: -Xmx512m -Dfile.encoding=UTF-8
- Environment variables: SPRING_PROFILES_ACTIVE=test
```

#### 5. 实用快捷键

```
# 代码生成
Alt + Insert              # 生成构造函数、getter/setter等
Ctrl + Alt + T            # 包围代码（try-catch、if等）
Ctrl + Alt + V            # 提取变量
Ctrl + Alt + M            # 提取方法
Ctrl + Alt + C            # 提取常量

# 代码导航
Ctrl + N                  # 查找类
Ctrl + Shift + N          # 查找文件
Ctrl + Alt + Shift + N    # 查找符号
Ctrl + B                  # 跳转到声明
Ctrl + Alt + B            # 跳转到实现
Alt + F7                  # 查找用法

# 代码编辑
Ctrl + D                  # 复制行
Ctrl + Y                  # 删除行
Ctrl + Shift + Up/Down    # 移动代码块
Ctrl + Alt + L            # 格式化代码
Ctrl + Alt + O            # 优化导入

# 调试
F8                        # 单步执行
F7                        # 步入
Shift + F8                # 步出
F9                        # 继续执行
Ctrl + F8                 # 切换断点
```

### Visual Studio Code

#### 1. 必装扩展

```json
{
  "recommendations": [
    "vscjava.vscode-java-pack",
    "vmware.vscode-spring-boot",
    "gabrielbb.vscode-lombok",
    "redhat.vscode-xml",
    "ms-vscode.vscode-json",
    "bradlc.vscode-tailwindcss",
    "esbenp.prettier-vscode",
    "ms-vscode.vscode-typescript-next",
    "gitpod.gitpod-desktop"
  ]
}
```

#### 2. 配置文件

**settings.json**：

```json
{
  "java.home": "/usr/lib/jvm/java-17-openjdk",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "/usr/lib/jvm/java-17-openjdk",
      "default": true
    }
  ],
  "java.compile.nullAnalysis.mode": "automatic",
  "java.format.settings.url": "./checkstyle/eclipse-java-style.xml",
  "editor.formatOnSave": true,
  "editor.codeActionsOnSave": {
    "source.organizeImports": true
  },
  "files.encoding": "utf8",
  "spring-boot.ls.problem.application-properties.enabled": true
}
```

## 🔧 开发辅助工具

### 1. 数据库工具

#### DBeaver（推荐）

**连接配置**：

```
# PostgreSQL 连接
Host: localhost
Port: 5432
Database: qing_dev
Username: qing
Password: qing123

# MySQL 连接
Host: localhost
Port: 3306
Database: qing_dev
Username: qing
Password: qing123
```

#### DataGrip

**项目数据源配置**：

```sql
-- 开发环境数据源
Host: localhost
Port: 5432
Database: qing_dev
User: qing
Password: qing123
URL: jdbc:postgresql://localhost:5432/qing_dev
```

### 2. API 测试工具

#### Postman

**环境配置**：

```json
{
  "name": "Qing Development",
  "values": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080",
      "enabled": true
    },
    {
      "key": "apiVersion",
      "value": "v1",
      "enabled": true
    },
    {
      "key": "token",
      "value": "",
      "enabled": true
    }
  ]
}
```

**预请求脚本**：

```javascript
// 自动获取认证令牌
if (!pm.environment.get("token")) {
    pm.sendRequest({
        url: pm.environment.get("baseUrl") + "/api/auth/login",
        method: 'POST',
        header: {
            'Content-Type': 'application/json'
        },
        body: {
            mode: 'raw',
            raw: JSON.stringify({
                username: "admin",
                password: "admin123"
            })
        }
    }, function (err, response) {
        if (response.json().success) {
            pm.environment.set("token", response.json().data.token);
        }
    });
}
```

#### Insomnia

**环境配置**：

```json
{
  "base_url": "http://localhost:8080",
  "api_version": "v1",
  "auth_token": "Bearer {{ _.token }}"
}
```

### 3. 版本控制工具

#### Git 配置

**全局配置**：

```bash
# 用户信息
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# 编辑器
git config --global core.editor "code --wait"

# 换行符处理
git config --global core.autocrlf input  # Linux/macOS
git config --global core.autocrlf true   # Windows

# 中文文件名支持
git config --global core.quotepath false

# 默认分支名
git config --global init.defaultBranch main

# 推送策略
git config --global push.default simple

# 合并策略
git config --global pull.rebase false
```

**项目级配置** (`.gitconfig`)：

```ini
[core]
    ignorecase = false
    filemode = false
[branch "main"]
    remote = origin
    merge = refs/heads/main
[alias]
    st = status
    co = checkout
    br = branch
    ci = commit
    lg = log --oneline --graph --decorate --all
    unstage = reset HEAD --
    last = log -1 HEAD
```

#### SourceTree

**推荐设置**：

```
# 通用设置
- 默认用户信息：配置全局用户名和邮箱
- 项目文件夹：设置项目根目录
- Git 版本：使用系统 Git

# 高级设置
- 推送行为：推送当前分支到同名远程分支
- 合并行为：创建合并提交
- 自动获取：每 5 分钟
```

### 4. 性能分析工具

#### JProfiler

**连接配置**：

```bash
# JVM 启动参数
-agentpath:/path/to/jprofiler/bin/linux-x64/libjprofilerti.so=port=8849
-Xmx2g -Xms1g
```

#### VisualVM

**监控配置**：

```bash
# 启用 JMX
-Dcom.sun.management.jmxremote
-Dcom.sun.management.jmxremote.port=9999
-Dcom.sun.management.jmxremote.authenticate=false
-Dcom.sun.management.jmxremote.ssl=false
```

## 🚀 构建和部署工具

### 1. Maven Wrapper

项目使用 Maven Wrapper，无需全局安装 Maven：

```bash
# Linux/macOS
./mvnw clean install
./mvnw spring-boot:run

# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

### 2. Docker 开发环境

**快速启动脚本** (`scripts/dev-start.sh`)：

```bash
#!/bin/bash

# 启动基础设施
echo "启动基础设施服务..."
docker-compose -f docker-compose.dev.yml up -d postgres redis

# 等待服务就绪
echo "等待数据库启动..."
sleep 10

# 运行数据库迁移
echo "执行数据库迁移..."
./mvnw flyway:migrate -Pflyway-dev

# 启动应用
echo "启动应用服务..."
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. 热重载配置

**Spring Boot DevTools**：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

**application-dev.yml**：

```yaml
spring:
  devtools:
    restart:
      enabled: true
      additional-paths: src/main/java
    livereload:
      enabled: true
      port: 35729
```

## 📊 代码质量工具

### 1. SonarQube

**本地运行**：

```bash
# 启动 SonarQube
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest

# 分析项目
./mvnw sonar:sonar \
  -Dsonar.projectKey=qing \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin
```

### 2. SpotBugs

**Maven 配置**：

```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.7.3.0</version>
    <configuration>
        <effort>Max</effort>
        <threshold>Low</threshold>
        <xmlOutput>true</xmlOutput>
    </configuration>
</plugin>
```

## 🔍 调试技巧

### 1. 远程调试

**JVM 参数**：

```bash
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

**IDEA 远程调试配置**：

```
Run → Edit Configurations → Remote JVM Debug
- Host: localhost
- Port: 5005
- Command line arguments for remote JVM: 
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

### 2. 日志调试

**Logback 配置** (`logback-spring.xml`)：

```xml
<configuration>
    <springProfile name="dev">
        <logger name="xyz.stanic.qing" level="DEBUG"/>
        <logger name="org.springframework.web" level="DEBUG"/>
        <logger name="org.hibernate.SQL" level="DEBUG"/>
        <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>
    </springProfile>
    
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

## 📚 学习资源

### 1. 官方文档

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Cloud 官方文档](https://spring.io/projects/spring-cloud)
- [Maven 官方文档](https://maven.apache.org/guides/)
- [Docker 官方文档](https://docs.docker.com/)

### 2. 推荐书籍

- 《Spring Boot 实战》
- 《微服务架构设计模式》
- 《Java 性能调优实战》
- 《代码整洁之道》

### 3. 在线资源

- [Spring Boot 官方指南](https://spring.io/guides)
- [Baeldung Spring 教程](https://www.baeldung.com/spring-tutorial)
- [Java 代码规范](https://google.github.io/styleguide/javaguide.html)

---

通过合理配置这些开发工具，可以显著提高开发效率和代码质量。建议根据个人习惯和项目需求选择合适的工具组合。