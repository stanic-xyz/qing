# 编码规范

本文档定义了青（Qing）项目的编码规范和最佳实践，旨在提高代码质量、可读性和可维护性。

## 📋 总体原则

### 核心理念

- **一致性**：整个项目保持统一的编码风格
- **可读性**：代码应该易于理解和维护
- **简洁性**：避免过度设计，保持代码简洁
- **安全性**：遵循安全编码实践
- **性能**：在保证可读性的前提下优化性能

## ☕ Java 编码规范

### 命名规范

#### 1. 包命名

```java
// ✅ 正确：全小写，使用点分隔
package xyz.stanic.qing.anime.controller;
package xyz.stanic.qing.common.util;

// ❌ 错误：包含大写字母
package xyz.stanic.qing.Anime.Controller;
```

#### 2. 类命名

```java
// ✅ 正确：PascalCase（大驼峰）
public class AnimeController {
public class UserService {
public class DatabaseConfig {

// ❌ 错误：其他命名方式
public class animeController {
public class user_service {
```

#### 3. 方法命名

```java
// ✅ 正确：camelCase（小驼峰），动词开头
public void createAnime() {
public List<Anime> findAnimesByCategory(String category) {
public boolean isValidUser(User user) {

// ❌ 错误：其他命名方式
public void CreateAnime() {
public List<Anime> anime_list() {
```

#### 4. 变量命名

```java
// ✅ 正确：camelCase，有意义的名称
String userName;
List<Anime> animeList;
int maxRetryCount = 3;

// ❌ 错误：无意义或不规范的名称
String s;
List<Anime> list1;
int a = 3;
```

#### 5. 常量命名

```java
// ✅ 正确：全大写，下划线分隔
public static final String DEFAULT_ENCODING = "UTF-8";
public static final int MAX_RETRY_COUNT = 3;
public static final long CACHE_EXPIRE_TIME = 3600L;

// ❌ 错误：其他命名方式
public static final String defaultEncoding = "UTF-8";
public static final int maxRetryCount = 3;
```

### 代码格式

#### 1. 缩进和空格

```java
// ✅ 正确：使用 4 个空格缩进
public class AnimeService {
    
    public void createAnime(AnimeCreateRequest request) {
        if (request != null) {
            // 业务逻辑
        }
    }
}

// 运算符前后加空格
int result = a + b * c;
boolean isValid = (count > 0) && (status == ACTIVE);
```

#### 2. 大括号规范

```java
// ✅ 正确：K&R 风格
if (condition) {
    doSomething();
} else {
    doSomethingElse();
}

// ✅ 正确：方法大括号
public void method() {
    // 方法体
}

// ❌ 错误：Allman 风格（不推荐）
if (condition)
{
    doSomething();
}
```

#### 3. 行长度限制

```java
// ✅ 正确：每行不超过 120 个字符
public AnimeResponse createAnime(AnimeCreateRequest request, 
                                String userId, 
                                boolean validateOnly) {
    // 方法实现
}

// 长字符串换行
String message = "这是一个很长的错误消息，" +
                "需要分行显示以保持代码的可读性";
```

### 注释规范

#### 1. 类注释

```java
/**
 * 动漫管理服务
 * 
 * 提供动漫的增删改查功能，包括：
 * - 动漫信息管理
 * - 分类管理
 * - 搜索功能
 * 
 * @author stanic
 * @since 1.0.0
 */
@Service
public class AnimeService {
    // 类实现
}
```

#### 2. 方法注释

```java
/**
 * 根据分类查询动漫列表
 * 
 * @param categoryId 分类ID，不能为空
 * @param page 页码，从1开始
 * @param size 每页大小，范围1-100
 * @return 动漫列表，包含分页信息
 * @throws IllegalArgumentException 当参数无效时抛出
 */
public PageResult<AnimeVO> findAnimesByCategory(Long categoryId, 
                                               int page, 
                                               int size) {
    // 方法实现
}
```

#### 3. 行内注释

```java
// ✅ 正确：解释复杂逻辑
// 计算缓存过期时间：基础时间 + 随机偏移（避免缓存雪崩）
long expireTime = baseExpireTime + random.nextInt(300);

// ✅ 正确：TODO 注释
// TODO: 优化查询性能，考虑添加索引
List<Anime> animes = animeRepository.findByComplexCondition(condition);

// ❌ 错误：无意义的注释
// 设置用户名
user.setUserName(userName);
```

### 异常处理

#### 1. 异常捕获

```java
// ✅ 正确：具体的异常处理
try {
    animeService.createAnime(request);
} catch (ValidationException e) {
    log.warn("参数验证失败: {}", e.getMessage());
    return Result.error("参数验证失败");
} catch (DuplicateKeyException e) {
    log.warn("动漫已存在: {}", request.getTitle());
    return Result.error("动漫已存在");
} catch (Exception e) {
    log.error("创建动漫失败", e);
    return Result.error("系统异常");
}

// ❌ 错误：捕获所有异常但不处理
try {
    animeService.createAnime(request);
} catch (Exception e) {
    // 空的异常处理
}
```

#### 2. 自定义异常

```java
// ✅ 正确：有意义的异常类
public class AnimeNotFoundException extends BusinessException {
    
    public AnimeNotFoundException(Long animeId) {
        super(String.format("动漫不存在: %d", animeId));
    }
}

// 使用自定义异常
if (anime == null) {
    throw new AnimeNotFoundException(animeId);
}
```

### 日志规范

#### 1. 日志级别

```java
// ✅ 正确：合适的日志级别
log.debug("查询参数: {}", queryParam);           // 调试信息
log.info("用户登录成功: {}", userId);             // 重要业务信息
log.warn("缓存未命中，降级到数据库查询");          // 警告信息
log.error("数据库连接失败", exception);           // 错误信息
```

#### 2. 日志格式

```java
// ✅ 正确：使用占位符，避免字符串拼接
log.info("用户 {} 创建动漫 {} 成功", userId, animeTitle);

// ✅ 正确：记录关键参数
log.info("开始处理动漫创建请求: userId={}, title={}", 
         request.getUserId(), request.getTitle());

// ❌ 错误：字符串拼接（性能差）
log.info("用户 " + userId + " 创建动漫 " + animeTitle + " 成功");
```

## 🏗️ 架构规范

### 分层架构

#### 1. Controller 层

```java
@RestController
@RequestMapping("/api/v1/animes")
@Validated
public class AnimeController {
    
    private final AnimeService animeService;
    
    /**
     * 只处理HTTP请求，参数验证，不包含业务逻辑
     */
    @PostMapping
    public Result<AnimeVO> createAnime(@Valid @RequestBody AnimeCreateRequest request) {
        AnimeVO anime = animeService.createAnime(request);
        return Result.success(anime);
    }
}
```

#### 2. Service 层

```java
@Service
@Transactional(rollbackFor = Exception.class)
public class AnimeService {
    
    private final AnimeRepository animeRepository;
    
    /**
     * 包含业务逻辑，事务管理
     */
    public AnimeVO createAnime(AnimeCreateRequest request) {
        // 业务验证
        validateAnimeRequest(request);
        
        // 业务逻辑
        Anime anime = convertToEntity(request);
        anime = animeRepository.save(anime);
        
        return convertToVO(anime);
    }
}
```

#### 3. Repository 层

```java
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    
    /**
     * 只处理数据访问，不包含业务逻辑
     */
    List<Anime> findByCategoryIdAndStatus(Long categoryId, AnimeStatus status);
    
    @Query("SELECT a FROM Anime a WHERE a.title LIKE %:keyword%")
    Page<Anime> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
```

### DTO 和 VO 规范

```java
// ✅ 请求 DTO
@Data
@Valid
public class AnimeCreateRequest {
    
    @NotBlank(message = "动漫标题不能为空")
    @Length(max = 100, message = "动漫标题长度不能超过100个字符")
    private String title;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @Length(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}

// ✅ 响应 VO
@Data
public class AnimeVO {
    
    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

## 🧪 测试规范

### 单元测试

```java
@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {
    
    @Mock
    private AnimeRepository animeRepository;
    
    @InjectMocks
    private AnimeService animeService;
    
    @Test
    @DisplayName("创建动漫 - 成功")
    void createAnime_Success() {
        // Given
        AnimeCreateRequest request = new AnimeCreateRequest();
        request.setTitle("测试动漫");
        request.setCategoryId(1L);
        
        Anime savedAnime = new Anime();
        savedAnime.setId(1L);
        savedAnime.setTitle("测试动漫");
        
        when(animeRepository.save(any(Anime.class))).thenReturn(savedAnime);
        
        // When
        AnimeVO result = animeService.createAnime(request);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("测试动漫");
    }
}
```

## 🔧 工具配置

### Checkstyle 配置

项目使用 Checkstyle 进行代码风格检查：

```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <configLocation>checkstyle/checkstyle.xml</configLocation>
        <encoding>UTF-8</encoding>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
    </configuration>
</plugin>
```

### IDEA 配置

1. **导入代码格式化配置**

- File → Settings → Editor → Code Style → Java
- Import Scheme → IntelliJ IDEA code style XML
- 选择 `checkstyle/idea-java-style.xml`

2. **配置 Checkstyle 插件**

- 安装 CheckStyle-IDEA 插件
- 配置 Checkstyle 文件路径

## 📚 最佳实践

### 1. 代码复用

```java
// ✅ 正确：提取公共方法
public class ValidationUtils {
    
    public static void validateId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + "不能为空或小于等于0");
        }
    }
}

// 使用公共方法
ValidationUtils.validateId(animeId, "动漫ID");
ValidationUtils.validateId(categoryId, "分类ID");
```

### 2. 常量管理

```java
// ✅ 正确：集中管理常量
public class AnimeConstants {
    
    public static final String CACHE_KEY_PREFIX = "anime:";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    public static final class Status {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
    }
}
```

### 3. 配置管理

```java
// ✅ 正确：使用配置类
@ConfigurationProperties(prefix = "qing.anime")
@Data
public class AnimeProperties {
    
    private final int maxTitleLength = 100;
    private final int defaultPageSize = 20;
    private final Duration cacheExpireTime = Duration.ofHours(1);
}
```

## 🚀 代码检查

### 运行检查

```bash
# 运行 Checkstyle 检查
mvn checkstyle:check

# 生成 Checkstyle 报告
mvn checkstyle:checkstyle

# 运行所有代码质量检查
mvn clean compile test checkstyle:check
```

### 持续集成

在 CI/CD 流水线中集成代码检查：

```yaml
# .github/workflows/ci.yml
- name: Run code quality checks
  run: |
    mvn clean compile
    mvn checkstyle:check
    mvn test
```

---

遵循这些编码规范将帮助我们构建高质量、可维护的代码。如有疑问，请参考 [开发工具配置](./dev-tools) 或在团队中讨论。
