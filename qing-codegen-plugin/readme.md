# Qing 代码生成器插件

一个基于注解处理器的Java代码生成工具，能够自动生成常用的CRUD代码，包括DTO、Service、Repository、Controller等。

## 📚 文档导航

> 📋 **[完整文档导航](文档导航.md)** - 根据你的需求快速找到合适的文档

- 🔰 **[新手使用指南](新手使用指南.md)** - 零基础入门教程，15分钟快速上手
- 📖 **[详细示例](EXAMPLES.md)** - 丰富的使用示例和最佳实践
- 🎯 **[示例项目](example/)** - 可运行的完整示例，快速体验所有功能
- 🛠️ **[API文档](#注解详解)** - 完整的注解说明和配置选项

## 特性

- 🚀 **高性能**: 基于注解处理器，编译时生成代码，零运行时开销
- 📦 **完整生态**: 自动生成VO、Creator、Updater、Query、Service、Repository、Controller等
- 🎯 **智能缓存**: 内置缓存机制，只在源码变更时重新生成
- 🔧 **高度可配置**: 支持自定义模板和生成策略
- 📊 **性能监控**: 提供详细的生成性能报告

## ⚡ 快速开始

> 🎯 **新手用户**: 建议先阅读 **[新手使用指南](新手使用指南.md)** 获得完整的入门体验

### 三步快速体验

**1. 添加依赖**
```xml
<dependency>
    <groupId>cn.chenyunlong</groupId>
    <artifactId>qing-codegen-apt</artifactId>
    <version>0.0.2-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

**2. 创建实体类**
```java
@Entity
@Data
@GenVo @GenCreator @GenUpdater @GenQuery
@GenRepository @GenService @GenServiceImpl @GenController
public class User extends BaseEntity {
    private String username;
    private String email;
}
```

**3. 编译项目**
```bash
mvn clean compile
```

✨ **完成！** 自动生成8个文件：VO、Creator、Updater、Query、Repository、Service、ServiceImpl、Controller

**生成的文件结构：**
```
src/main/java/your/package/
├── vo/UserVO.java                    # 视图对象
├── creator/UserCreator.java          # 创建对象  
├── updater/UserUpdater.java          # 更新对象
├── query/UserQuery.java              # 查询对象
├── repository/UserRepository.java    # 数据访问层
├── service/IUserService.java         # 服务接口
├── service/UserServiceImpl.java      # 服务实现
└── controller/UserController.java    # REST控制器
```

## 注解详解

### 核心注解

| 注解 | 描述 | 生成内容 |
|------|------|----------|
| `@GenVo` | 生成视图对象 | VO类，用于数据展示 |
| `@GenCreator` | 生成创建对象 | Creator类，用于创建实体 |
| `@GenUpdater` | 生成更新对象 | Updater类，用于更新实体 |
| `@GenQuery` | 生成查询对象 | Query类，用于查询条件 |
| `@GenRepository` | 生成Repository | 数据访问层接口 |
| `@GenService` | 生成Service接口 | 服务层接口 |
| `@GenServiceImpl` | 生成Service实现 | 服务层实现类 |
| `@GenController` | 生成Controller | REST API控制器 |

### 扩展注解

| 注解 | 描述 | 生成内容 |
|------|------|----------|
| `@GenCreateRequest` | 生成创建请求 | 用于API的创建请求对象 |
| `@GenUpdateRequest` | 生成更新请求 | 用于API的更新请求对象 |
| `@GenQueryRequest` | 生成查询请求 | 用于API的查询请求对象 |
| `@GenResponse` | 生成响应对象 | 用于API的响应对象 |
| `@GenFeign` | 生成Feign客户端 | 微服务调用客户端 |
| `@GenMapper` | 生成Mapper | 对象映射器 |

## 配置说明

### 缓存配置

代码生成器支持智能缓存，可以通过以下方式配置：

```properties
# 启用缓存（默认：true）
qing.codegen.cache.enabled=true

# 缓存目录（默认：target/codegen-cache）
qing.codegen.cache.directory=target/codegen-cache

# 强制重新生成（默认：false）
qing.codegen.force.regenerate=false
```

### 生成策略配置

```properties
# 输出目录（默认：src/main/java）
qing.codegen.output.directory=src/main/java

# 包名前缀
qing.codegen.package.prefix=cn.chenyunlong

# 是否生成注释（默认：true）
qing.codegen.generate.comments=true
```

## 性能优化

### 缓存策略

1. **文件级缓存**: 基于源文件的MD5哈希值判断是否需要重新生成
2. **增量编译**: 只处理变更的文件
3. **并行处理**: 支持多线程并行生成代码

### 性能监控

编译时会输出详细的性能报告：

```
[INFO] ================================================================================
[INFO] [性能监控] 代码生成器性能报告
[INFO] ================================================================================
[INFO] 总编译时间: 133ms | 编译轮次: 1 | 处理文件总数: 8
[INFO] 生成文件: 1 | 跳过文件: 7 | 生成用时: 17ms
[INFO] 缓存命中: 7 | 缓存未命中: 1 | 命中率: 87.5% | 缓存检查用时: 87.5ms
[INFO] 平均生成时间: 17.0ms/文件 | 生成效率: 58.8文件/秒
```

## 最佳实践

### 1. 实体类设计

```java
@Entity
@Table(name = "t_product")
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
public class Product extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "产品名称", required = true, maxLength = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    @Schema(description = "产品描述", maxLength = 500)
    private String description;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Schema(description = "价格", required = true, minimum = "0")
    private BigDecimal price;
    
    @Column(name = "stock", nullable = false)
    @Schema(description = "库存数量", required = true, minimum = "0")
    private Integer stock;
}
```

### 2. 分层架构

```
src/main/java/
├── domain/
│   ├── Product.java              # 实体类
│   ├── dto/
│   │   ├── vo/
│   │   │   └── ProductVO.java    # 自动生成
│   │   ├── creator/
│   │   │   └── ProductCreator.java # 自动生成
│   │   ├── updater/
│   │   │   └── ProductUpdater.java # 自动生成
│   │   └── query/
│   │       └── ProductQuery.java   # 自动生成
│   ├── repository/
│   │   └── ProductRepository.java  # 自动生成
│   ├── service/
│   │   ├── IProductService.java    # 自动生成
│   │   └── impl/
│   │       └── ProductServiceImpl.java # 自动生成
│   └── controller/
│       └── ProductController.java  # 自动生成
```

### 3. 自定义业务逻辑

生成的代码提供了基础的CRUD操作，你可以在此基础上添加自定义业务逻辑：

```java
@Service
public class ProductServiceImpl implements IProductService {
    
    // 自动生成的基础方法...
    
    /**
     * 自定义业务方法：批量更新库存
     */
    @Transactional
    public void batchUpdateStock(List<StockUpdateRequest> requests) {
        // 自定义业务逻辑
    }
    
    /**
     * 自定义业务方法：根据分类查询产品
     */
    public List<ProductVO> findByCategory(String category) {
        // 自定义查询逻辑
    }
}
```

## 故障排除

### 常见问题

1. **编译时没有生成代码**
   - 检查注解是否正确添加
   - 确认依赖scope为provided
   - 清理并重新编译：`mvn clean compile`

2. **生成的代码编译错误**
   - 检查实体类是否继承了BaseEntity
   - 确认所有必要的依赖都已添加
   - 检查包名是否正确

3. **缓存问题**
   - 删除缓存目录：`rm -rf target/codegen-cache`
   - 使用强制重新生成：`-Dqing.codegen.force.regenerate=true`

### 调试模式

启用详细日志输出：

```bash
mvn clean compile -X
```

## 示例项目

查看 `example` 目录下的完整示例项目，了解如何使用代码生成器。

```bash
cd example
mvn clean compile
mvn spring-boot:run
```

访问 http://localhost:8080/swagger-ui.html 查看生成的API文档。

## 📖 学习资源

- 🔰 **[新手使用指南](新手使用指南.md)** - 完整的入门教程，包含详细步骤和常见问题解答
- 📚 **[详细示例](EXAMPLES.md)** - 丰富的使用示例，涵盖各种复杂场景
- 🎯 **[示例项目](example/)** - 可运行的完整示例，快速体验所有功能
- 💡 **[最佳实践](#最佳实践)** - 项目结构设计和性能优化建议

## 贡献指南

欢迎提交Issue和Pull Request！

1. Fork 项目
2. 创建特性分支：`git checkout -b feature/new-feature`
3. 提交更改：`git commit -am 'Add new feature'`
4. 推送分支：`git push origin feature/new-feature`
5. 提交Pull Request

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE.TXT) 文件。
