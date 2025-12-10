---
sidebar_position: 1
---

# 开发指南

本指南将详细介绍青（Qing）项目的开发规范、最佳实践和贡献流程。

## 🎯 开发原则

### 核心理念

- **简洁性**：代码应该简洁明了，易于理解和维护
- **一致性**：遵循统一的编码规范和设计模式
- **可测试性**：编写可测试的代码，保证代码质量
- **可扩展性**：设计应该支持未来的功能扩展
- **性能优先**：在保证功能的前提下，优化性能

### 设计模式

- **微服务架构**：服务间松耦合，高内聚
- **领域驱动设计**：以业务领域为核心的设计方法
- **CQRS**：命令查询职责分离
- **事件驱动**：通过事件实现服务间通信
- **依赖注入**：使用 Spring 的 IoC 容器管理依赖

## 🏗️ 项目结构

### 整体架构

```
qing/
├── qing-eureka-server/          # 服务注册中心
├── qing-config-server/          # 配置中心
├── qing-gateway/                # API 网关
├── qing-services/               # 业务服务
│   ├── qing-service-anime/      # 动漫服务
│   └── qing-service-auth/       # 认证服务
├── qing-common/                 # 公共组件
│   ├── qing-common-core/        # 核心工具类
│   ├── qing-common-web/         # Web 相关组件
│   ├── qing-common-security/    # 安全组件
│   └── qing-common-data/        # 数据访问组件
├── qing-codegen-plugin/         # 代码生成插件
├── docs/                        # 项目文档
├── sql/                         # 数据库脚本
├── checkstyle/                  # 代码检查配置
├── .editorconfig               # 编辑器配置
├── pom.xml                     # Maven 主配置
└── README.md                   # 项目说明
```

### 服务模块结构

每个微服务遵循统一的目录结构：

```
qing-service-xxx/
├── src/main/java/
│   └── com/stanic/qing/xxx/
│       ├── XxxApplication.java          # 启动类
│       ├── config/                      # 配置类
│       ├── controller/                  # 控制器
│       ├── service/                     # 业务逻辑
│       │   └── impl/                    # 业务实现
│       ├── repository/                  # 数据访问
│       ├── domain/                      # 领域模型
│       │   ├── entity/                  # 实体类
│       │   ├── dto/                     # 数据传输对象
│       │   └── vo/                      # 视图对象
│       ├── exception/                   # 异常处理
│       └── util/                        # 工具类
├── src/main/resources/
│   ├── application.yml                  # 应用配置
│   ├── application-dev.yml              # 开发环境配置
│   ├── application-test.yml             # 测试环境配置
│   ├── application-prod.yml             # 生产环境配置
│   ├── mapper/                          # MyBatis 映射文件
│   └── db/migration/                    # 数据库迁移脚本
├── src/test/java/                       # 测试代码
└── pom.xml                              # Maven 配置
```

## 📝 编码规范

### Java 编码规范

#### 命名规范

```java
// 类名：使用 PascalCase
public class AnimeService {
    
    // 常量：使用 UPPER_SNAKE_CASE
    private static final String DEFAULT_CATEGORY = "UNKNOWN";
    
    // 变量和方法：使用 camelCase
    private String animeName;
    
    public List<Anime> findAnimesByCategory(String category) {
        // 方法实现
    }
}

// 包名：使用小写字母，用点分隔
package com.stanic.qing.anime.service;

// 接口名：使用 PascalCase，可以用 I 前缀或 Service/Repository 后缀
public interface AnimeService {
    // 接口方法
}
```

#### 注释规范

```java
/**
 * 动漫服务接口
 * 
 * @author stanic
 * @since 1.0.0
 */
public interface AnimeService {
    
    /**
     * 根据分类查询动漫列表
     * 
     * @param category 动漫分类
     * @param pageable 分页参数
     * @return 动漫列表
     * @throws IllegalArgumentException 当分类参数为空时抛出
     */
    Page<Anime> findByCategory(String category, Pageable pageable);
}
```

#### 异常处理

```java
// 自定义业务异常
public class AnimeNotFoundException extends RuntimeException {
    public AnimeNotFoundException(String message) {
        super(message);
    }
    
    public AnimeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AnimeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAnimeNotFound(AnimeNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .code("ANIME_NOT_FOUND")
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
```

### 数据库规范

#### 表命名规范

```sql
-- 表名：使用小写字母和下划线
CREATE TABLE anime_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    anime_name VARCHAR(255) NOT NULL COMMENT '动漫名称',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-连载中，2-已完结',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动漫信息表';
```

#### 实体类规范

```java
@Entity
@Table(name = "anime_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "anime_name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private AnimeStatus status;
    
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;
}
```

### API 设计规范

#### RESTful API 设计

```java
@RestController
@RequestMapping("/api/v1/animes")
@Validated
public class AnimeController {
    
    // GET /api/v1/animes - 获取动漫列表
    @GetMapping
    public ResponseEntity<Page<AnimeVO>> getAnimes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        // 实现逻辑
    }
    
    // GET /api/v1/animes/{id} - 获取动漫详情
    @GetMapping("/{id}")
    public ResponseEntity<AnimeVO> getAnime(@PathVariable Long id) {
        // 实现逻辑
    }
    
    // POST /api/v1/animes - 创建动漫
    @PostMapping
    public ResponseEntity<AnimeVO> createAnime(@Valid @RequestBody AnimeCreateDTO dto) {
        // 实现逻辑
    }
    
    // PUT /api/v1/animes/{id} - 更新动漫
    @PutMapping("/{id}")
    public ResponseEntity<AnimeVO> updateAnime(
            @PathVariable Long id, 
            @Valid @RequestBody AnimeUpdateDTO dto) {
        // 实现逻辑
    }
    
    // DELETE /api/v1/animes/{id} - 删除动漫
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable Long id) {
        // 实现逻辑
    }
}
```

#### 统一响应格式

```java
@Data
@Builder
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .code(200)
            .message("Success")
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
            .code(code)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }
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
    private AnimeServiceImpl animeService;
    
    @Test
    @DisplayName("根据ID查询动漫 - 成功")
    void findById_Success() {
        // Given
        Long animeId = 1L;
        Anime anime = Anime.builder()
            .id(animeId)
            .name("测试动漫")
            .build();
        when(animeRepository.findById(animeId)).thenReturn(Optional.of(anime));
        
        // When
        AnimeVO result = animeService.findById(animeId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(animeId);
        assertThat(result.getName()).isEqualTo("测试动漫");
        verify(animeRepository).findById(animeId);
    }
    
    @Test
    @DisplayName("根据ID查询动漫 - 不存在")
    void findById_NotFound() {
        // Given
        Long animeId = 999L;
        when(animeRepository.findById(animeId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> animeService.findById(animeId))
            .isInstanceOf(AnimeNotFoundException.class)
            .hasMessage("动漫不存在: " + animeId);
    }
}
```

### 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AnimeControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private AnimeRepository animeRepository;
    
    @Test
    @DisplayName("创建动漫 - 集成测试")
    void createAnime_IntegrationTest() {
        // Given
        AnimeCreateDTO createDTO = AnimeCreateDTO.builder()
            .name("新动漫")
            .categoryId(1L)
            .description("测试描述")
            .build();
        
        // When
        ResponseEntity<AnimeVO> response = restTemplate.postForEntity(
            "/api/v1/animes", createDTO, AnimeVO.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("新动漫");
        
        // 验证数据库
        Optional<Anime> savedAnime = animeRepository.findById(response.getBody().getId());
        assertThat(savedAnime).isPresent();
        assertThat(savedAnime.get().getName()).isEqualTo("新动漫");
    }
}
```

### 测试数据管理

```java
@TestConfiguration
public class TestDataConfig {
    
    @Bean
    @Primary
    public AnimeTestDataFactory animeTestDataFactory() {
        return new AnimeTestDataFactory();
    }
}

public class AnimeTestDataFactory {
    
    public Anime createAnime(String name) {
        return Anime.builder()
            .name(name)
            .categoryId(1L)
            .description("测试描述")
            .status(AnimeStatus.ONGOING)
            .build();
    }
    
    public List<Anime> createAnimeList(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> createAnime("动漫" + i))
            .collect(Collectors.toList());
    }
}
```

## 🔧 开发工具配置

### IDE 配置

#### IntelliJ IDEA

1. **代码风格配置**

- File → Settings → Editor → Code Style
- 导入项目根目录的 `intellij-code-style.xml`

2. **Checkstyle 插件**

- File → Settings → Plugins → 搜索并安装 "Checkstyle-IDEA"
- File → Settings → Tools → Checkstyle
- 添加配置文件：`checkstyle/checkstyle.xml`

3. **Live Templates**
   ```java
   // 创建 Service 方法模板
   @Override
   public $RETURN_TYPE$ $METHOD_NAME$($PARAMETERS$) {
       log.debug("$METHOD_NAME$ called with parameters: {}", $PARAM_NAMES$);
       try {
           $BODY$
       } catch (Exception e) {
           log.error("Error in $METHOD_NAME$: {}", e.getMessage(), e);
           throw e;
       }
   }
   ```

#### Visual Studio Code

推荐扩展：

```json
{
  "recommendations": [
    "vscjava.vscode-java-pack",
    "redhat.java",
    "vscjava.vscode-spring-boot-dashboard",
    "gabrielbb.vscode-lombok",
    "editorconfig.editorconfig",
    "shengchen.vscode-checkstyle"
  ]
}
```

### Git 工作流

#### 分支策略

```bash
# 主分支
main          # 生产环境代码
develop       # 开发环境代码

# 功能分支
feature/xxx   # 新功能开发
bugfix/xxx    # Bug 修复
hotfix/xxx    # 紧急修复
release/xxx   # 发布准备
```

#### 提交规范

```bash
# 提交消息格式
<type>(<scope>): <subject>

<body>

<footer>

# 示例
feat(anime): add anime search functionality

Implement search functionality for anime service:
- Add search by name and category
- Support pagination and sorting
- Add search result caching

Closes #123
```

提交类型：

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

#### Git Hooks

```bash
#!/bin/sh
# .git/hooks/pre-commit

# 运行代码检查
mvn checkstyle:check
if [ $? -ne 0 ]; then
    echo "Checkstyle failed. Please fix the issues before committing."
    exit 1
fi

# 运行测试
mvn test
if [ $? -ne 0 ]; then
    echo "Tests failed. Please fix the issues before committing."
    exit 1
fi

echo "Pre-commit checks passed."
```

## 📊 性能优化

### 数据库优化

#### 查询优化

```java
// 避免 N+1 查询
@Query("SELECT a FROM Anime a JOIN FETCH a.category WHERE a.status = :status")
List<Anime> findByStatusWithCategory(@Param("status") AnimeStatus status);

// 使用分页查询
@Query(value = "SELECT * FROM anime_info WHERE category_id = :categoryId",
       countQuery = "SELECT count(*) FROM anime_info WHERE category_id = :categoryId",
       nativeQuery = true)
Page<Anime> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

// 使用索引优化
@Query("SELECT a FROM Anime a WHERE a.name LIKE :keyword AND a.status = :status")
List<Anime> searchByNameAndStatus(
    @Param("keyword") String keyword, 
    @Param("status") AnimeStatus status);
```

#### 缓存策略

```java
@Service
public class AnimeService {
    
    // 方法级缓存
    @Cacheable(value = "animes", key = "#id")
    public AnimeVO findById(Long id) {
        // 查询逻辑
    }
    
    // 条件缓存
    @Cacheable(value = "animeList", 
               key = "#category + '_' + #pageable.pageNumber + '_' + #pageable.pageSize",
               condition = "#category != null")
    public Page<AnimeVO> findByCategory(String category, Pageable pageable) {
        // 查询逻辑
    }
    
    // 缓存清除
    @CacheEvict(value = "animes", key = "#id")
    public void deleteById(Long id) {
        // 删除逻辑
    }
}
```

### JVM 优化

#### 内存配置

```bash
# 开发环境
JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 生产环境
JAVA_OPTS="
  -Xms2g -Xmx4g
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
  -XX:+UseStringDeduplication
  -XX:+PrintGCDetails
  -XX:+PrintGCTimeStamps
  -Xloggc:gc.log
"
```

#### 监控配置

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true
```

## 🔒 安全最佳实践

### 输入验证

```java
public class AnimeCreateDTO {
    
    @NotBlank(message = "动漫名称不能为空")
    @Size(max = 255, message = "动漫名称长度不能超过255个字符")
    private String name;
    
    @NotNull(message = "分类ID不能为空")
    @Positive(message = "分类ID必须为正数")
    private Long categoryId;
    
    @Size(max = 5000, message = "描述长度不能超过5000个字符")
    private String description;
    
    @Pattern(regexp = "^(ONGOING|COMPLETED|SUSPENDED)$", 
             message = "状态值无效")
    private String status;
}
```

### SQL 注入防护

```java
// 使用参数化查询
@Query("SELECT a FROM Anime a WHERE a.name LIKE %:keyword%")
List<Anime> searchByKeyword(@Param("keyword") String keyword);

// 避免动态 SQL 拼接
// 错误示例
// String sql = "SELECT * FROM anime WHERE name = '" + name + "'";

// 正确示例
@Query(value = "SELECT * FROM anime WHERE name = :name", nativeQuery = true)
List<Anime> findByName(@Param("name") String name);
```

### 敏感信息处理

```java
// 密码加密
@Service
public class PasswordService {
    
    private final PasswordEncoder passwordEncoder;
    
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

// 日志脱敏
@Data
public class UserVO {
    private String username;
    
    @JsonIgnore
    private String rawPassword;
    
    @Override
    public String toString() {
        return "UserVO{username='" + username + "', rawPassword='***'}";
    }
}
```

## 📚 文档规范

### API 文档

```java
@RestController
@RequestMapping("/api/v1/animes")
@Tag(name = "动漫管理", description = "动漫信息的增删改查接口")
public class AnimeController {
    
    @GetMapping("/{id}")
    @Operation(summary = "获取动漫详情", description = "根据动漫ID获取详细信息")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(schema = @Schema(implementation = AnimeVO.class))),
        @ApiResponse(responseCode = "404", description = "动漫不存在",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AnimeVO> getAnime(
            @Parameter(description = "动漫ID", required = true, example = "1")
            @PathVariable Long id) {
        // 实现逻辑
    }
}
```

### 代码文档

```java
/**
 * 动漫服务实现类
 * 
 * <p>提供动漫信息的增删改查功能，包括：
 * <ul>
 *   <li>动漫信息的创建和更新</li>
 *   <li>动漫信息的查询和搜索</li>
 *   <li>动漫分类管理</li>
 * </ul>
 * 
 * <p>使用示例：
 * <pre>{@code
 * AnimeService animeService = new AnimeServiceImpl(animeRepository);
 * AnimeVO anime = animeService.findById(1L);
 * }</pre>
 * 
 * @author stanic
 * @version 1.0.0
 * @since 2024-01-01
 * @see AnimeRepository
 * @see AnimeVO
 */
@Service
@Slf4j
public class AnimeServiceImpl implements AnimeService {
    // 实现代码
}
```

## 🚀 部署和发布

### CI/CD 流水线

```yaml
# .github/workflows/ci.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Cache Maven dependencies
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
    - name: Run tests
      run: mvn clean test
    
    - name: Run Checkstyle
      run: mvn checkstyle:check
    
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Maven Tests
        path: target/surefire-reports/*.xml
        reporter: java-junit

  build:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
    - uses: actions/checkout@v3
    
    - name: Build Docker images
      run: |
        docker build -t qing/eureka-server:${{ github.sha }} qing-eureka-server/
        docker build -t qing/gateway:${{ github.sha }} qing-gateway/
    
    - name: Push to registry
      run: |
        echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --rawPassword-stdin
        docker push qing/eureka-server:${{ github.sha }}
        docker push qing/gateway:${{ github.sha }}
```

### 版本管理

```bash
# 语义化版本控制
# MAJOR.MINOR.PATCH
# 1.0.0 -> 1.0.1 (补丁版本)
# 1.0.1 -> 1.1.0 (次版本)
# 1.1.0 -> 2.0.0 (主版本)

# 发布流程
git checkout main
git pull origin main
mvn versions:set -DnewVersion=1.1.0
git add .
git commit -m "chore: bump version to 1.1.0"
git tag v1.1.0
git push origin main --tags
```

## 🆘 故障排查

### 日志规范

```java
@Slf4j
@Service
public class AnimeServiceImpl implements AnimeService {
    
    @Override
    public AnimeVO findById(Long id) {
        log.debug("Finding anime by id: {}", id);
        
        try {
            Optional<Anime> anime = animeRepository.findById(id);
            if (anime.isEmpty()) {
                log.warn("Anime not found with id: {}", id);
                throw new AnimeNotFoundException("动漫不存在: " + id);
            }
            
            log.info("Successfully found anime: {} (id: {})", anime.get().getName(), id);
            return animeMapper.toVO(anime.get());
            
        } catch (Exception e) {
            log.error("Error finding anime by id: {}", id, e);
            throw e;
        }
    }
}
```

### 监控指标

```java
@Component
public class AnimeMetrics {
    
    private final Counter animeCreatedCounter;
    private final Timer animeQueryTimer;
    private final Gauge activeAnimeGauge;
    
    public AnimeMetrics(MeterRegistry meterRegistry) {
        this.animeCreatedCounter = Counter.builder("anime.created")
            .description("Number of animes created")
            .register(meterRegistry);
            
        this.animeQueryTimer = Timer.builder("anime.query")
            .description("Time taken to query anime")
            .register(meterRegistry);
            
        this.activeAnimeGauge = Gauge.builder("anime.active")
            .description("Number of active animes")
            .register(meterRegistry, this, AnimeMetrics::getActiveAnimeCount);
    }
    
    public void incrementCreatedCounter() {
        animeCreatedCounter.increment();
    }
    
    public Timer.Sample startQueryTimer() {
        return Timer.start();
    }
    
    private double getActiveAnimeCount() {
        // 返回活跃动漫数量
        return 0;
    }
}
```

## 📚 学习资源

### 推荐阅读

- **Spring Boot 官方文档**: https://spring.io/projects/spring-boot
- **Spring Cloud 官方文档**: https://spring.io/projects/spring-cloud
- **Clean Code**: Robert C. Martin
- **Effective Java**: Joshua Bloch
- **微服务架构设计模式**: Chris Richardson

### 在线资源

- **Baeldung**: https://www.baeldung.com/
- **Spring 官方指南**: https://spring.io/guides
- **Java 设计模式**: https://java-design-patterns.com/

## 🤝 贡献指南

### 贡献流程

1. **Fork 项目**到你的 GitHub 账户
2. **创建功能分支**：`git checkout -b feature/amazing-feature`
3. **提交更改**：`git commit -m 'feat: add amazing feature'`
4. **推送分支**：`git push origin feature/amazing-feature`
5. **创建 Pull Request**

### Pull Request 规范

- 标题简洁明了，描述主要变更
- 详细描述变更内容和原因
- 关联相关的 Issue
- 确保所有测试通过
- 添加必要的文档更新

### 代码审查

- 代码风格符合项目规范
- 功能实现正确且高效
- 测试覆盖率充足
- 文档完整准确
- 安全性考虑充分

---

> 💡 **提示**
>
：本开发指南会随着项目发展持续更新，建议定期查看最新版本。如有疑问或建议，欢迎通过 [Issues](https://github.com/stanic-xyz/qing/issues)
> 或 [Discussions](https://github.com/stanic-xyz/qing/discussions) 与我们交流。
