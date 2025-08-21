# Qing 代码生成器示例

本文档提供了 Qing 代码生成器的详细使用示例，展示不同场景下的注解使用方法。

## 目录

- [基础示例](#基础示例)
- [复杂示例](#复杂示例)
- [注解组合](#注解组合)
- [配置示例](#配置示例)
- [最佳实践](#最佳实践)

## 基础示例

### 1. 简单实体类

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
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "stock", nullable = false)
    private Integer stock;
}
```

**生成的文件：**
- `ProductVO.java` - 视图对象
- `ProductCreator.java` - 创建对象
- `ProductUpdater.java` - 更新对象
- `ProductQuery.java` - 查询对象
- `ProductRepository.java` - 数据访问层
- `IProductService.java` - 服务接口
- `ProductServiceImpl.java` - 服务实现
- `ProductController.java` - 控制器

### 2. 用户实体类（带验证）

```java
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
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;
    
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "用户状态不能为空")
    private UserStatus status;
    
    public enum UserStatus {
        ACTIVE("正常"),
        INACTIVE("未激活"),
        LOCKED("已锁定"),
        DELETED("已删除");
        
        private final String description;
        
        UserStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
```

## 复杂示例

### 1. 订单实体类（完整功能）

```java
@Entity
@Table(name = "t_order")
@Data
@EqualsAndHashCode(callSuper = true)
// 生成所有基础代码
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenRepository
@GenService
@GenServiceImpl
@GenController
// 生成扩展代码
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
public class Order extends BaseEntity {
    
    @Column(name = "order_number", nullable = false, unique = true, length = 32)
    private String orderNumber;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "order_time", nullable = false)
    private LocalDateTime orderTime;
    
    public enum OrderStatus {
        PENDING("待确认"),
        CONFIRMED("已确认"),
        SHIPPED("已发货"),
        DELIVERED("已收货"),
        CANCELLED("已取消");
        
        private final String description;
        
        OrderStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
```

**生成的文件：**
- 基础文件：VO、Creator、Updater、Query、Repository、Service、ServiceImpl、Controller
- 扩展文件：CreateRequest、UpdateRequest、QueryRequest、Response

### 2. 关联关系示例

```java
@Entity
@Table(name = "t_order_item")
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
public class OrderItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
```

## 注解组合

### 1. 最小配置

```java
@Entity
@GenVo
@GenRepository
public class SimpleEntity extends BaseEntity {
    private String name;
}
```

### 2. 标准配置

```java
@Entity
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenRepository
@GenService
@GenServiceImpl
@GenController
public class StandardEntity extends BaseEntity {
    // 字段定义
}
```

### 3. 完整配置

```java
@Entity
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenRepository
@GenService
@GenServiceImpl
@GenController
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
public class FullEntity extends BaseEntity {
    // 字段定义
}
```

## 配置示例

### 1. Maven 插件配置

```xml
<plugin>
    <groupId>cn.chenyunlong</groupId>
    <artifactId>qing-codegen-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <!-- 扫描包路径 -->
        <scanPackage>cn.chenyunlong.codegen.example.domain</scanPackage>
        <!-- 输出目录 -->
        <outputDirectory>src/main/java</outputDirectory>
        <!-- 基础包名 -->
        <basePackage>cn.chenyunlong.codegen.example</basePackage>
        <!-- 是否覆盖已存在的文件 -->
        <overwrite>false</overwrite>
        <!-- 是否生成 Swagger 注解 -->
        <generateSwagger>true</generateSwagger>
        <!-- 是否生成验证注解 -->
        <generateValidation>true</generateValidation>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 2. 自定义模板配置

```xml
<configuration>
    <templateConfig>
        <!-- 自定义 VO 模板 -->
        <voTemplate>templates/custom-vo.ftl</voTemplate>
        <!-- 自定义 Service 模板 -->
        <serviceTemplate>templates/custom-service.ftl</serviceTemplate>
        <!-- 自定义 Controller 模板 -->
        <controllerTemplate>templates/custom-controller.ftl</controllerTemplate>
    </templateConfig>
</configuration>
```

### 3. 包结构配置

```xml
<configuration>
    <packageConfig>
        <!-- DTO 包路径 -->
        <dtoPackage>dto</dtoPackage>
        <!-- Repository 包路径 -->
        <repositoryPackage>repository</repositoryPackage>
        <!-- Service 包路径 -->
        <servicePackage>service</servicePackage>
        <!-- Controller 包路径 -->
        <controllerPackage>controller</controllerPackage>
    </packageConfig>
</configuration>
```

## 最佳实践

### 1. 实体类设计

```java
@Entity
@Table(name = "t_example", indexes = {
    @Index(name = "idx_name", columnList = "name"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "Example", description = "示例实体")
public class Example extends BaseEntity {
    
    // 使用合适的数据类型
    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "名称不能为空")
    @Size(max = 100, message = "名称长度不能超过100个字符")
    @Schema(description = "名称", required = true, maxLength = 100)
    private String name;
    
    // 枚举类型使用 STRING 存储
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", required = true)
    private Status status;
    
    // 金额使用 BigDecimal
    @Column(name = "amount", precision = 10, scale = 2)
    @DecimalMin(value = "0.00", message = "金额不能小于0")
    @Schema(description = "金额", example = "100.00")
    private BigDecimal amount;
    
    // 布尔类型提供默认值
    @Column(name = "enabled", nullable = false)
    @Schema(description = "是否启用", required = true, example = "true")
    private Boolean enabled = true;
    
    public enum Status {
        ACTIVE("激活"),
        INACTIVE("未激活");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
```

### 2. 注解使用建议

- **基础项目**：使用 `@GenVo`、`@GenRepository`、`@GenService`、`@GenController`
- **标准项目**：添加 `@GenCreator`、`@GenUpdater`、`@GenQuery`、`@GenServiceImpl`
- **完整项目**：再添加 `@GenCreateRequest`、`@GenUpdateRequest`、`@GenQueryRequest`、`@GenResponse`

### 3. 性能优化

- 使用 `@Index` 注解为常用查询字段添加索引
- 关联关系使用 `FetchType.LAZY` 延迟加载
- 合理设置字段长度和精度
- 为枚举类型使用 `EnumType.STRING`

### 4. 代码组织

```
src/main/java/
├── domain/           # 实体类
│   ├── Product.java
│   ├── Order.java
│   └── User.java
├── dto/              # 数据传输对象
│   ├── creator/
│   ├── updater/
│   ├── query/
│   └── vo/
├── repository/       # 数据访问层
├── service/          # 服务层
└── controller/       # 控制器层
```

### 5. 测试建议

```java
@SpringBootTest
@Transactional
class ProductServiceTest {
    
    @Autowired
    private IProductService productService;
    
    @Test
    void testCreateProduct() {
        ProductCreator creator = new ProductCreator();
        creator.setName("测试产品");
        creator.setPrice(new BigDecimal("99.99"));
        creator.setStock(100);
        
        ProductVO result = productService.createProduct(creator);
        
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("测试产品");
    }
}
```

## 常见问题

### Q: 如何自定义生成的代码？
A: 可以通过自定义 FreeMarker 模板来修改生成的代码格式和内容。

### Q: 如何处理复杂的业务逻辑？
A: 生成的代码提供基础功能，复杂业务逻辑可以在 ServiceImpl 中添加自定义方法。

### Q: 如何处理数据库迁移？
A: 建议使用 Flyway 或 Liquibase 进行数据库版本管理。

### Q: 如何优化查询性能？
A: 合理使用索引、分页查询、缓存等技术，避免 N+1 查询问题。