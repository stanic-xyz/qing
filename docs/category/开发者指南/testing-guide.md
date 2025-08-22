# 测试指南

本文档介绍青（Qing）项目的测试策略、测试框架和最佳实践，帮助开发者编写高质量的测试代码。

## 📋 测试策略

### 测试金字塔

```
    /\     E2E Tests (端到端测试)
   /  \    - 少量，覆盖关键业务流程
  /____\   - 测试整个系统集成
 
  /______\  Integration Tests (集成测试)
 /        \ - 适量，测试模块间交互
/__________\ - 测试数据库、外部服务

/____________\ Unit Tests (单元测试)
              - 大量，快速执行
              - 测试单个方法/类
```

### 测试原则

- **快速**：单元测试应该快速执行
- **独立**：测试之间不应相互依赖
- **可重复**：测试结果应该一致
- **自验证**：测试应该有明确的通过/失败结果
- **及时**：测试应该与代码同步编写

## 🧪 单元测试

### 测试框架

项目使用以下测试框架：

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>

<!-- AssertJ -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>

<!-- Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Service 层测试

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("动漫服务测试")
class AnimeServiceTest {
    
    @Mock
    private AnimeRepository animeRepository;
    
    @Mock
    private CategoryService categoryService;
    
    @Mock
    private CacheManager cacheManager;
    
    @InjectMocks
    private AnimeService animeService;
    
    @Nested
    @DisplayName("创建动漫")
    class CreateAnimeTest {
        
        @Test
        @DisplayName("成功创建动漫")
        void createAnime_Success() {
            // Given
            AnimeCreateRequest request = AnimeCreateRequest.builder()
                .title("测试动漫")
                .categoryId(1L)
                .description("测试描述")
                .build();
            
            Category category = new Category(1L, "动作");
            Anime savedAnime = Anime.builder()
                .id(1L)
                .title("测试动漫")
                .categoryId(1L)
                .description("测试描述")
                .status(AnimeStatus.ACTIVE)
                .createTime(LocalDateTime.now())
                .build();
            
            when(categoryService.findById(1L)).thenReturn(category);
            when(animeRepository.save(any(Anime.class))).thenReturn(savedAnime);
            
            // When
            AnimeVO result = animeService.createAnime(request);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTitle()).isEqualTo("测试动漫");
            assertThat(result.getCategoryName()).isEqualTo("动作");
            
            verify(categoryService).findById(1L);
            verify(animeRepository).save(any(Anime.class));
        }
        
        @Test
        @DisplayName("分类不存在时抛出异常")
        void createAnime_CategoryNotFound_ThrowsException() {
            // Given
            AnimeCreateRequest request = AnimeCreateRequest.builder()
                .title("测试动漫")
                .categoryId(999L)
                .build();
            
            when(categoryService.findById(999L))
                .thenThrow(new CategoryNotFoundException(999L));
            
            // When & Then
            assertThatThrownBy(() -> animeService.createAnime(request))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("分类不存在: 999");
            
            verify(categoryService).findById(999L);
            verify(animeRepository, never()).save(any());
        }
        
        @Test
        @DisplayName("标题重复时抛出异常")
        void createAnime_DuplicateTitle_ThrowsException() {
            // Given
            AnimeCreateRequest request = AnimeCreateRequest.builder()
                .title("重复标题")
                .categoryId(1L)
                .build();
            
            Category category = new Category(1L, "动作");
            
            when(categoryService.findById(1L)).thenReturn(category);
            when(animeRepository.existsByTitle("重复标题")).thenReturn(true);
            
            // When & Then
            assertThatThrownBy(() -> animeService.createAnime(request))
                .isInstanceOf(DuplicateAnimeException.class)
                .hasMessage("动漫标题已存在: 重复标题");
        }
    }
    
    @Nested
    @DisplayName("查询动漫")
    class FindAnimeTest {
        
        @Test
        @DisplayName("根据ID查询动漫成功")
        void findById_Success() {
            // Given
            Long animeId = 1L;
            Anime anime = Anime.builder()
                .id(animeId)
                .title("测试动漫")
                .categoryId(1L)
                .status(AnimeStatus.ACTIVE)
                .build();
            
            when(animeRepository.findById(animeId)).thenReturn(Optional.of(anime));
            
            // When
            AnimeVO result = animeService.findById(animeId);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(animeId);
            assertThat(result.getTitle()).isEqualTo("测试动漫");
        }
        
        @Test
        @DisplayName("动漫不存在时抛出异常")
        void findById_NotFound_ThrowsException() {
            // Given
            Long animeId = 999L;
            when(animeRepository.findById(animeId)).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> animeService.findById(animeId))
                .isInstanceOf(AnimeNotFoundException.class)
                .hasMessage("动漫不存在: 999");
        }
    }
    
    @ParameterizedTest
    @DisplayName("参数化测试 - 验证不同状态")
    @EnumSource(AnimeStatus.class)
    void findByStatus_AllStatuses(AnimeStatus status) {
        // Given
        List<Anime> animes = Arrays.asList(
            Anime.builder().id(1L).status(status).build(),
            Anime.builder().id(2L).status(status).build()
        );
        
        when(animeRepository.findByStatus(status)).thenReturn(animes);
        
        // When
        List<AnimeVO> result = animeService.findByStatus(status);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(anime -> anime.getStatus() == status);
    }
}
```

### Repository 层测试

```java
@DataJpaTest
@DisplayName("动漫仓储测试")
class AnimeRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private AnimeRepository animeRepository;
    
    @Test
    @DisplayName("根据分类ID查询动漫")
    void findByCategoryId_Success() {
        // Given
        Category category = new Category("动作");
        entityManager.persistAndFlush(category);
        
        Anime anime1 = Anime.builder()
            .title("动漫1")
            .categoryId(category.getId())
            .status(AnimeStatus.ACTIVE)
            .build();
        Anime anime2 = Anime.builder()
            .title("动漫2")
            .categoryId(category.getId())
            .status(AnimeStatus.ACTIVE)
            .build();
        
        entityManager.persist(anime1);
        entityManager.persist(anime2);
        entityManager.flush();
        
        // When
        List<Anime> result = animeRepository.findByCategoryId(category.getId());
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Anime::getTitle)
            .containsExactlyInAnyOrder("动漫1", "动漫2");
    }
    
    @Test
    @DisplayName("分页查询动漫")
    void findAll_Pageable_Success() {
        // Given
        for (int i = 1; i <= 25; i++) {
            Anime anime = Anime.builder()
                .title("动漫" + i)
                .categoryId(1L)
                .status(AnimeStatus.ACTIVE)
                .build();
            entityManager.persist(anime);
        }
        entityManager.flush();
        
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        
        // When
        Page<Anime> result = animeRepository.findAll(pageable);
        
        // Then
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(25);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("动漫1");
    }
    
    @Test
    @DisplayName("自定义查询方法")
    void searchByKeyword_Success() {
        // Given
        Anime anime1 = Anime.builder()
            .title("火影忍者")
            .description("忍者的故事")
            .categoryId(1L)
            .status(AnimeStatus.ACTIVE)
            .build();
        Anime anime2 = Anime.builder()
            .title("海贼王")
            .description("海盗的冒险")
            .categoryId(1L)
            .status(AnimeStatus.ACTIVE)
            .build();
        
        entityManager.persist(anime1);
        entityManager.persist(anime2);
        entityManager.flush();
        
        // When
        List<Anime> result = animeRepository.searchByKeyword("忍者");
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("火影忍者");
    }
}
```

## 🔗 集成测试

### Web 层集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@DisplayName("动漫控制器集成测试")
class AnimeControllerIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
        .withDatabaseName("qing_test")
        .withUsername("test")
        .withPassword("test");
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private AnimeRepository animeRepository;
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @BeforeEach
    void setUp() {
        animeRepository.deleteAll();
    }
    
    @Test
    @DisplayName("创建动漫 - 完整流程")
    void createAnime_FullFlow_Success() {
        // Given
        AnimeCreateRequest request = AnimeCreateRequest.builder()
            .title("集成测试动漫")
            .categoryId(1L)
            .description("集成测试描述")
            .build();
        
        // When
        ResponseEntity<Result<AnimeVO>> response = restTemplate.postForEntity(
            "/api/v1/animes",
            request,
            new ParameterizedTypeReference<Result<AnimeVO>>() {}
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        
        AnimeVO anime = response.getBody().getData();
        assertThat(anime.getTitle()).isEqualTo("集成测试动漫");
        assertThat(anime.getId()).isNotNull();
        
        // 验证数据库中的数据
        Optional<Anime> savedAnime = animeRepository.findById(anime.getId());
        assertThat(savedAnime).isPresent();
        assertThat(savedAnime.get().getTitle()).isEqualTo("集成测试动漫");
    }
    
    @Test
    @DisplayName("查询动漫列表 - 分页")
    void getAnimes_Pagination_Success() {
        // Given
        for (int i = 1; i <= 15; i++) {
            Anime anime = Anime.builder()
                .title("动漫" + i)
                .categoryId(1L)
                .status(AnimeStatus.ACTIVE)
                .build();
            animeRepository.save(anime);
        }
        
        // When
        ResponseEntity<Result<PageResult<AnimeVO>>> response = restTemplate.exchange(
            "/api/v1/animes?page=1&size=10",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Result<PageResult<AnimeVO>>>() {}
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        PageResult<AnimeVO> pageResult = response.getBody().getData();
        assertThat(pageResult.getContent()).hasSize(10);
        assertThat(pageResult.getTotalElements()).isEqualTo(15);
        assertThat(pageResult.getTotalPages()).isEqualTo(2);
    }
}
```

### 数据库集成测试

```java
@SpringBootTest
@Transactional
@DisplayName("数据库集成测试")
class DatabaseIntegrationTest {
    
    @Autowired
    private AnimeService animeService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Test
    @DisplayName("事务回滚测试")
    void transactionRollback_Test() {
        // Given
        Category category = categoryService.create("测试分类");
        
        AnimeCreateRequest request = AnimeCreateRequest.builder()
            .title("事务测试动漫")
            .categoryId(category.getId())
            .build();
        
        // When & Then
        assertThatThrownBy(() -> {
            animeService.createAnime(request);
            // 模拟异常
            throw new RuntimeException("模拟异常");
        }).isInstanceOf(RuntimeException.class);
        
        // 验证事务回滚
        assertThat(animeService.findByTitle("事务测试动漫")).isEmpty();
    }
}
```

## 🌐 端到端测试

### API 端到端测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("动漫管理端到端测试")
class AnimeE2ETest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private static Long createdAnimeId;
    
    @Test
    @Order(1)
    @DisplayName("E2E - 创建动漫")
    void e2e_CreateAnime() {
        AnimeCreateRequest request = AnimeCreateRequest.builder()
            .title("E2E测试动漫")
            .categoryId(1L)
            .description("端到端测试")
            .build();
        
        ResponseEntity<Result<AnimeVO>> response = restTemplate.postForEntity(
            "/api/v1/animes", request, 
            new ParameterizedTypeReference<Result<AnimeVO>>() {}
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        createdAnimeId = response.getBody().getData().getId();
    }
    
    @Test
    @Order(2)
    @DisplayName("E2E - 查询动漫")
    void e2e_GetAnime() {
        ResponseEntity<Result<AnimeVO>> response = restTemplate.exchange(
            "/api/v1/animes/" + createdAnimeId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Result<AnimeVO>>() {}
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().getTitle()).isEqualTo("E2E测试动漫");
    }
    
    @Test
    @Order(3)
    @DisplayName("E2E - 更新动漫")
    void e2e_UpdateAnime() {
        AnimeUpdateRequest request = AnimeUpdateRequest.builder()
            .title("E2E测试动漫-已更新")
            .description("端到端测试-已更新")
            .build();
        
        ResponseEntity<Result<AnimeVO>> response = restTemplate.exchange(
            "/api/v1/animes/" + createdAnimeId,
            HttpMethod.PUT,
            new HttpEntity<>(request),
            new ParameterizedTypeReference<Result<AnimeVO>>() {}
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().getTitle()).isEqualTo("E2E测试动漫-已更新");
    }
    
    @Test
    @Order(4)
    @DisplayName("E2E - 删除动漫")
    void e2e_DeleteAnime() {
        ResponseEntity<Result<Void>> response = restTemplate.exchange(
            "/api/v1/animes/" + createdAnimeId,
            HttpMethod.DELETE,
            null,
            new ParameterizedTypeReference<Result<Void>>() {}
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // 验证删除成功
        ResponseEntity<Result<AnimeVO>> getResponse = restTemplate.exchange(
            "/api/v1/animes/" + createdAnimeId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Result<AnimeVO>>() {}
        );
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
```

## 🧪 测试工具

### 测试数据构建器

```java
public class AnimeTestDataBuilder {
    
    private String title = "默认动漫";
    private Long categoryId = 1L;
    private String description = "默认描述";
    private AnimeStatus status = AnimeStatus.ACTIVE;
    
    public static AnimeTestDataBuilder anAnime() {
        return new AnimeTestDataBuilder();
    }
    
    public AnimeTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }
    
    public AnimeTestDataBuilder withCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }
    
    public AnimeTestDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public AnimeTestDataBuilder withStatus(AnimeStatus status) {
        this.status = status;
        return this;
    }
    
    public Anime build() {
        return Anime.builder()
            .title(title)
            .categoryId(categoryId)
            .description(description)
            .status(status)
            .createTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();
    }
    
    public AnimeCreateRequest buildCreateRequest() {
        return AnimeCreateRequest.builder()
            .title(title)
            .categoryId(categoryId)
            .description(description)
            .build();
    }
}

// 使用示例
Anime anime = anAnime()
    .withTitle("测试动漫")
    .withCategoryId(2L)
    .withStatus(AnimeStatus.INACTIVE)
    .build();
```

### 测试配置

```java
@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public Clock testClock() {
        return Clock.fixed(
            LocalDateTime.of(2023, 1, 1, 12, 0, 0)
                .toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        );
    }
    
    @Bean
    @Primary
    public CacheManager testCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
```

## 📊 测试覆盖率

### JaCoCo 配置

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>CLASS</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 覆盖率目标

- **单元测试覆盖率**：≥ 80%
- **集成测试覆盖率**：≥ 60%
- **关键业务逻辑**：≥ 90%

## 🚀 运行测试

### Maven 命令

```bash
# 运行所有测试
mvn test

# 运行单元测试
mvn test -Dtest="*Test"

# 运行集成测试
mvn test -Dtest="*IntegrationTest"

# 运行特定测试类
mvn test -Dtest="AnimeServiceTest"

# 运行特定测试方法
mvn test -Dtest="AnimeServiceTest#createAnime_Success"

# 生成测试报告
mvn test jacoco:report

# 跳过测试
mvn install -DskipTests
```

### IDE 配置

1. **IntelliJ IDEA**

- 右键点击测试类/方法 → Run
- 使用快捷键 `Ctrl+Shift+F10`
- 配置运行模板

2. **测试覆盖率**

- Run with Coverage
- 查看覆盖率报告
- 设置覆盖率阈值

## 📝 测试最佳实践

### 1. 测试命名

```java
// ✅ 好的测试名称
@Test
@DisplayName("当用户ID为空时，创建动漫应该抛出参数异常")
void createAnime_WhenUserIdIsNull_ShouldThrowIllegalArgumentException() {
    // 测试实现
}

// ❌ 不好的测试名称
@Test
void test1() {
    // 测试实现
}
```

### 2. AAA 模式

```java
@Test
void shouldReturnAnimeWhenValidIdProvided() {
    // Arrange (Given)
    Long animeId = 1L;
    Anime expectedAnime = anAnime().withId(animeId).build();
    when(animeRepository.findById(animeId)).thenReturn(Optional.of(expectedAnime));
    
    // Act (When)
    AnimeVO result = animeService.findById(animeId);
    
    // Assert (Then)
    assertThat(result.getId()).isEqualTo(animeId);
    verify(animeRepository).findById(animeId);
}
```

### 3. 测试隔离

```java
@BeforeEach
void setUp() {
    // 每个测试前的准备工作
    animeRepository.deleteAll();
}

@AfterEach
void tearDown() {
    // 每个测试后的清理工作
    cacheManager.clear();
}
```

### 4. 异常测试

```java
@Test
void shouldThrowExceptionWhenAnimeNotFound() {
    // Given
    Long nonExistentId = 999L;
    when(animeRepository.findById(nonExistentId))
        .thenReturn(Optional.empty());
    
    // When & Then
    assertThatThrownBy(() -> animeService.findById(nonExistentId))
        .isInstanceOf(AnimeNotFoundException.class)
        .hasMessage("动漫不存在: 999");
}
```

---

通过遵循这些测试指南，我们可以构建可靠、可维护的测试套件，确保代码质量和系统稳定性。
