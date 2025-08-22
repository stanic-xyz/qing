# 快速开始指南

本指南将帮助你在5分钟内开始使用Qing代码生成器。

## 前置条件

- Java 8+
- Maven 3.6+
- Spring Boot 2.x/3.x

## 步骤1：安装插件

```bash
# 克隆项目
git clone https://github.com/stanic-xyz/qing.git
cd qing

# 编译安装插件
mvn clean install -pl qing-codegen-plugin -am
```

## 步骤2：创建新项目

```bash
# 使用Spring Boot脚手架创建项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=demo-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

cd demo-app
```

## 步骤3：添加依赖

在 `pom.xml` 中添加：

```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>3.2.0</version>
    </dependency>
    
    <!-- Spring Boot JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>3.2.0</version>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Qing 代码生成器 -->
    <dependency>
        <groupId>cn.chenyunlong</groupId>
        <artifactId>qing-codegen-apt</artifactId>
        <version>0.0.2-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Qing 通用模块 -->
    <dependency>
        <groupId>cn.chenyunlong</groupId>
        <artifactId>qing-domain-common</artifactId>
        <version>0.0.2-SNAPSHOT</version>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Swagger -->
    <dependency>
        <groupId>io.swagger.core.v3</groupId>
        <artifactId>swagger-annotations</artifactId>
        <version>2.2.8</version>
    </dependency>
</dependencies>
```

## 步骤4：创建基础实体

创建 `src/main/java/com/example/domain/User.java`：

```java
package com.example.domain;

import cn.chenyunlong.qing.domain.common.BaseEntity;
import cn.chenyunlong.codegen.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity
@Table(name = "t_user")
@Data
@EqualsAndHashCode(callSuper = true)
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenRepository
@GenService
@GenServiceImpl
@GenController
public class User extends BaseEntity {
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    @Schema(description = "用户名", required = true, maxLength = 50)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Schema(description = "邮箱", required = true, maxLength = 100)
    private String email;
    
    @Column(name = "full_name", length = 100)
    @Schema(description = "全名", maxLength = 100)
    private String fullName;
    
    @Column(name = "age")
    @Schema(description = "年龄", minimum = "0", maximum = "150")
    private Integer age;
}
```

## 步骤5：创建启动类

创建 `src/main/java/com/example/Application.java`：

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## 步骤6：配置数据库

创建 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080
  servlet:
    context-path: /api

logging:
  level:
    cn.chenyunlong: DEBUG
```

## 步骤7：编译生成代码

```bash
mvn clean compile
```

编译成功后，你会看到以下生成的文件：

```
src/main/java/com/example/domain/
├── User.java                    # 你创建的实体
├── dto/
│   ├── vo/UserVO.java          # 自动生成的视图对象
│   ├── creator/UserCreator.java # 自动生成的创建对象
│   ├── updater/UserUpdater.java # 自动生成的更新对象
│   └── query/UserQuery.java     # 自动生成的查询对象
├── repository/UserRepository.java # 自动生成的Repository
├── service/
│   ├── IUserService.java        # 自动生成的Service接口
│   └── impl/UserServiceImpl.java # 自动生成的Service实现
└── controller/UserController.java # 自动生成的Controller
```

## 步骤8：启动应用

```bash
mvn spring-boot:run
```

## 步骤9：测试API

应用启动后，你可以访问以下端点：

- **H2 控制台**: http://localhost:8080/api/h2-console
- **API 端点**: http://localhost:8080/api/api/v1/user/

### 测试创建用户（需要实现创建端点）

```bash
curl -X POST http://localhost:8080/api/api/v1/user \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "age": 30
  }'
```

## 下一步

恭喜！你已经成功创建了一个使用Qing代码生成器的项目。现在你可以：

1. **添加更多实体**: 创建更多带有生成注解的实体类
2. **自定义业务逻辑**: 在生成的Service实现中添加自定义方法
3. **配置数据库**: 切换到MySQL、PostgreSQL等生产数据库
4. **添加安全性**: 集成Spring Security
5. **API文档**: 集成Swagger/OpenAPI

## 常见问题

**Q: 编译时没有生成代码？**
A: 确保依赖的scope是`provided`，并且实体类正确添加了生成注解。

**Q: 生成的代码有编译错误？**
A: 检查实体类是否继承了`BaseEntity`，并确保所有必要的依赖都已添加。

**Q: 如何自定义生成的代码？**
A: 查看完整的[README.md](README.md)了解高级配置选项。

## 获取帮助

- 📖 [完整文档](README.md)
- 🐛 [报告问题](https://github.com/stanic-xyz/qing/issues)
- 💬 [讨论区](https://github.com/stanic-xyz/qing/discussions)