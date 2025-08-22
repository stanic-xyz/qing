# 🎯 Qing 代码生成器示例项目

> 🚀 **快速体验**: 一个完整可运行的示例项目，展示代码生成器的所有功能  
> ⏱️ **运行时间**: 5分钟即可启动并体验完整功能  
> 🎓 **学习目标**: 通过实际运行理解代码生成器的工作原理

这是一个展示 Qing 代码生成器功能的示例项目，演示了如何使用各种注解来自动生成完整的 CRUD 代码。

## ⚡ 快速运行

### 方法一：直接运行（推荐新手）

```bash
# 1. 进入示例项目目录
cd example

# 2. 编译并生成代码
mvn clean compile

# 3. 启动应用
mvn spring-boot:run
```

### 方法二：完整编译

```bash
# 1. 在项目根目录编译代码生成器
cd ..
mvn clean install -pl qing-codegen-plugin -am

# 2. 进入示例项目
cd example

# 3. 编译并启动
mvn clean compile spring-boot:run
```

### 访问应用

启动成功后，访问以下地址：
- 🌐 **API文档**: http://localhost:8080/doc.html
- 🗄️ **H2数据库**: http://localhost:8080/h2-console
- 📊 **健康检查**: http://localhost:8080/actuator/health

## 项目结构

```
example/
├── src/main/java/cn/chenyunlong/codegen/example/
│   ├── ExampleApplication.java          # Spring Boot 启动类
│   └── domain/                          # 领域模型
│       ├── User.java                    # 用户实体（基础示例）
│       ├── Product.java                 # 产品实体（复杂示例）
│       └── Category.java                # 分类实体（简单示例）
├── src/main/resources/
│   └── application.yml                  # 应用配置文件
└── pom.xml                             # Maven 配置文件
```

## 代码生成器注解说明

### 核心注解

| 注解 | 功能 | 生成文件 |
|------|------|----------|
| `@GenVo` | 生成 VO 类 | `{Entity}VO.java` |
| `@GenCreator` | 生成 Creator 类 | `{Entity}Creator.java` |
| `@GenUpdater` | 生成 Updater 类 | `{Entity}Updater.java` |
| `@GenQuery` | 生成 Query 类 | `{Entity}Query.java` |
| `@GenCreateRequest` | 生成创建请求类 | `{Entity}CreateRequest.java` |
| `@GenUpdateRequest` | 生成更新请求类 | `{Entity}UpdateRequest.java` |
| `@GenQueryRequest` | 生成查询请求类 | `{Entity}QueryRequest.java` |
| `@GenResponse` | 生成响应类 | `{Entity}Response.java` |
| `@GenRepository` | 生成 Repository 接口 | `{Entity}Repository.java` |
| `@GenService` | 生成 Service 接口 | `{Entity}Service.java` |
| `@GenServiceImpl` | 生成 Service 实现类 | `{Entity}ServiceImpl.java` |
| `@GenController` | 生成 Controller 类 | `{Entity}Controller.java` |
| `@GenMapper` | 生成 Mapper 接口 | `{Entity}Mapper.java` |
| `@GenFeign` | 生成 Feign 客户端 | `{Entity}FeignClient.java` |

### 字段忽略注解

| 注解 | 功能 |
|------|------|
| `@IgnoreVo` | 在生成 VO 时忽略该字段 |
| `@IgnoreCreator` | 在生成 Creator 时忽略该字段 |
| `@IgnoreUpdater` | 在生成 Updater 时忽略该字段 |

## 示例实体说明

### 1. User.java - 基础示例

展示了用户实体的基本用法：
- 基础字段类型（String、Integer、LocalDateTime）
- 枚举类型（UserStatus）
- 字段验证注解
- 字段忽略注解的使用
- 静态工厂方法

### 2. Product.java - 复杂示例

展示了产品实体的高级用法：
- 复杂字段类型（BigDecimal、List）
- 多种验证注解
- 枚举状态管理
- 业务方法实现
- 集合字段的处理

### 3. Category.java - 简单示例

展示了分类实体的简单用法：
- 基础字段定义
- 父子关系处理
- 简单的业务逻辑

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- IDE（推荐 IntelliJ IDEA）

### 2. 编译项目

```bash
# 进入项目目录
cd qing-codegen-plugin/example

# 编译项目（会触发代码生成）
mvn clean compile
```

### 3. 查看生成的代码

## 🎉 生成代码展示

编译完成后，你会看到自动生成了大量文件！以User实体为例：

```
src/main/java/cn/chenyunlong/codegen/example/
├── domain/
│   └── User.java                     # ✍️ 你编写的实体类
├── vo/
│   └── UserVO.java                   # 🤖 自动生成的VO类
├── creator/
│   └── UserCreator.java              # 🤖 自动生成的创建对象
├── updater/
│   └── UserUpdater.java              # 🤖 自动生成的更新对象
├── query/
│   └── UserQuery.java                # 🤖 自动生成的查询对象
├── request/
│   ├── UserCreateRequest.java        # 🤖 自动生成的创建请求
│   ├── UserUpdateRequest.java        # 🤖 自动生成的更新请求
│   └── UserQueryRequest.java         # 🤖 自动生成的查询请求
├── response/
│   └── UserResponse.java             # 🤖 自动生成的响应对象
├── repository/
│   └── UserRepository.java           # 🤖 自动生成的Repository
├── service/
│   ├── IUserService.java             # 🤖 自动生成的Service接口
│   └── UserServiceImpl.java          # 🤖 自动生成的Service实现
├── controller/
│   └── UserController.java           # 🤖 自动生成的Controller
└── mapper/
    └── UserMapper.java               # 🤖 自动生成的对象映射器
```

> 💡 **统计**: 一个简单的User实体类，自动生成了**10个文件**，包含完整的CRUD功能！

### 🔍 生成代码预览

**UserController.java** (部分代码)
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserCreateRequest request) {
        // 自动生成的创建用户API
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        // 自动生成的查询用户API
    }
    
    // ... 更多CRUD方法
}
```

**UserServiceImpl.java** (部分代码)
```java
@Service
public class UserServiceImpl implements IUserService {
    
    @Autowired
    private UserRepository repository;
    
    @Override
    public UserResponse create(UserCreateRequest request) {
        // 自动生成的业务逻辑
    }
    
    // ... 更多业务方法
}
```

### 4. 运行应用

```bash
# 启动应用
mvn spring-boot:run
```

应用启动后，可以访问：
- H2 数据库控制台：http://localhost:8080/api/h2-console
- API 文档：http://localhost:8080/api/doc.html（如果配置了 Swagger）

## 配置说明

### 注解参数

每个代码生成注解都支持以下参数：

- `pkgName`：生成代码的包名（相对于实体类包）
- `sourcePath`：生成代码的源码路径
- `overwrite`：是否覆盖已存在的文件（默认 false）

示例：
```java
@GenVo(pkgName = "vo", sourcePath = "src/main/java", overwrite = true)
```

### Maven 配置

在 `pom.xml` 中配置注解处理器：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>cn.chenyunlong</groupId>
                <artifactId>qing-codegen-apt</artifactId>
                <version>${project.version}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

## 🎓 学习建议

### 第一步：运行并体验
1. 按照上面的快速运行指南启动项目
2. 访问API文档，尝试调用各种接口
3. 查看H2数据库中的数据变化

### 第二步：查看生成的代码
1. 在IDE中打开生成的各种文件
2. 对比实体类和生成的代码，理解生成规则
3. 注意观察不同注解产生的不同效果

### 第三步：修改和实验
1. 尝试修改实体类的字段
2. 添加或删除注解
3. 重新编译，观察生成代码的变化

### 第四步：创建自己的实体
1. 在domain包下创建新的实体类
2. 添加适当的注解
3. 编译并测试生成的功能

## 🚀 下一步学习

完成示例项目体验后，建议继续学习：

1. **📚 [新手使用指南](../新手使用指南.md)** - 详细的使用教程和最佳实践
2. **📖 [详细示例](../EXAMPLES.md)** - 更多复杂场景的使用示例
3. **🛠️ [主文档](../readme.md)** - 完整的功能说明和配置选项

## ❓ 常见问题

**Q: 为什么我的代码没有生成？**
A: 确保实体类添加了正确的注解，并且执行了`mvn clean compile`命令。

**Q: 生成的代码可以修改吗？**
A: 不建议直接修改生成的代码，因为重新编译时会被覆盖。建议通过继承或组合的方式扩展功能。

**Q: 如何自定义生成的代码？**
A: 可以通过注解参数配置包名、路径等，详见[新手使用指南](../新手使用指南.md#q3-如何自定义生成的代码)。

---

🎯 **目标达成**: 通过这个示例项目，你应该已经对代码生成器有了直观的认识！现在可以在自己的项目中尝试使用了。

## 最佳实践

### 1. 实体类设计

- 继承 `AbstractJpaAggregate` 获得基础字段
- 使用 Lombok 注解减少样板代码
- 添加适当的 JPA 注解
- 使用 Bean Validation 注解进行字段验证
- 添加 Swagger 注解用于 API 文档

### 2. 字段忽略策略

- 密码等敏感字段使用 `@IgnoreVo`
- 系统字段（如创建时间）使用 `@IgnoreCreator` 和 `@IgnoreUpdater`
- 复杂关联字段根据需要选择性忽略

### 3. 包结构规划

建议按功能模块组织生成的代码：

```
com.example.module/
├── domain/          # 实体类
├── vo/              # VO 类
├── dto/             # DTO 类
├── request/         # 请求类
├── response/        # 响应类
├── repository/      # 数据访问层
├── service/         # 业务逻辑层
└── controller/      # 控制器层
```

## 常见问题

### Q: 为什么生成的代码没有出现？

A: 请检查：
1. Maven 编译插件是否正确配置了注解处理器路径
2. 实体类是否添加了代码生成注解
3. 编译是否成功完成

### Q: 如何自定义生成的代码模板？

A: 目前代码生成器使用内置模板，如需自定义，请参考源码中的处理器实现。

### Q: 生成的代码可以手动修改吗？

A: 建议不要手动修改生成的代码，因为重新编译时会被覆盖。如需自定义逻辑，可以通过继承或组合的方式扩展。

## 更多信息

- [项目主页](https://github.com/stanic-xyz/qing)
- [文档站点](https://qing.chenyunlong.cn)
- [问题反馈](https://github.com/stanic-xyz/qing/issues)

## 许可证

本项目采用 MIT 许可证，详见 [LICENSE](../LICENSE.TXT) 文件。