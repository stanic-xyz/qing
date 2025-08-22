---
sidebar_position: 4
---

# 常见问题

本文档收集了青（Qing）项目使用过程中的常见问题和解决方案。

## 🚀 安装和启动问题

### Q1: 项目启动失败，提示端口被占用

**问题描述**：启动服务时出现 `Port 8080 was already in use` 错误。

**解决方案**：

1. **查看端口占用情况**：
   ```bash
   # Windows
   netstat -ano | findstr :8080
   
   # Linux/macOS
   lsof -i :8080
   ```

2. **终止占用端口的进程**：
   ```bash
   # Windows
   taskkill /PID <进程ID> /F
   
   # Linux/macOS
   kill -9 <进程ID>
   ```

3. **修改服务端口**：
   ```yaml
   # application.yml
   server:
     port: 8081  # 修改为其他端口
   ```

### Q2: 数据库连接失败

**问题描述**：启动时出现 `Cannot create PoolableConnectionFactory` 错误。

**解决方案**：

1. **检查数据库服务状态**：
   ```bash
   # 检查 MySQL 是否运行
   systemctl status mysql
   # 或
   brew services list | grep mysql
   ```

2. **验证数据库连接信息**：
   ```yaml
   # application.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/qing?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
       username: root
       password: your_password
   ```

3. **创建数据库**：
   ```sql
   CREATE DATABASE qing CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. **检查防火墙设置**：
   ```bash
   # 确保 3306 端口可访问
   telnet localhost 3306
   ```

### Q3: Redis 连接失败

**问题描述**：启动时出现 `Unable to connect to Redis` 错误。

**解决方案**：

1. **启动 Redis 服务**：
   ```bash
   # Linux
   systemctl start redis
   
   # macOS
   brew services start redis
   
   # Windows
   redis-server.exe
   ```

2. **检查 Redis 配置**：
   ```yaml
   # application.yml
   spring:
     redis:
       host: localhost
       port: 6379
       password: # 如果设置了密码
       database: 0
   ```

3. **测试 Redis 连接**：
   ```bash
   redis-cli ping
   # 应该返回 PONG
   ```

### Q4: Maven 依赖下载失败

**问题描述**：构建项目时依赖下载缓慢或失败。

**解决方案**：

1. **配置国内镜像源**：
   ```xml
   <!-- settings.xml -->
   <mirrors>
     <mirror>
       <id>aliyun</id>
       <mirrorOf>central</mirrorOf>
       <name>Aliyun Central</name>
       <url>https://maven.aliyun.com/repository/central</url>
     </mirror>
   </mirrors>
   ```

2. **清理本地仓库**：
   ```bash
   mvn clean
   mvn dependency:purge-local-repository
   ```

3. **强制更新依赖**：
   ```bash
   mvn clean install -U
   ```

## 🔧 配置问题

### Q5: Eureka 服务注册失败

**问题描述**：微服务无法注册到 Eureka Server。

**解决方案**：

1. **检查 Eureka Server 状态**：

- 访问 `http://localhost:8761` 确认 Eureka 控制台可用

2. **验证服务配置**：
   ```yaml
   # application.yml
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka/
       register-with-eureka: true
       fetch-registry: true
     instance:
       prefer-ip-address: true
       lease-renewal-interval-in-seconds: 30
   ```

3. **检查网络连通性**：
   ```bash
   curl http://localhost:8761/eureka/apps
   ```

4. **查看服务日志**：
   ```bash
   # 查找 Eureka 相关错误日志
   grep -i eureka logs/application.log
   ```

### Q6: 配置中心连接失败

**问题描述**：服务无法从 Config Server 获取配置。

**解决方案**：

1. **确认 Config Server 运行状态**：
   ```bash
   curl http://localhost:8888/actuator/health
   ```

2. **检查配置文件路径**：
   ```yaml
   # bootstrap.yml
   spring:
     cloud:
       config:
         uri: http://localhost:8888
         name: qing-service-anime
         profile: dev
         label: main
   ```

3. **验证 Git 仓库配置**：
   ```yaml
   # Config Server 配置
   spring:
     cloud:
       config:
         server:
           git:
             uri: https://github.com/stanic-xyz/qing-config
             clone-on-start: true
   ```

### Q7: JWT Token 验证失败

**问题描述**：API 请求返回 401 Unauthorized 错误。

**解决方案**：

1. **检查 Token 格式**：
   ```bash
   # 正确的 Header 格式
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

2. **验证 Token 有效期**：
   ```javascript
   // 解码 JWT Token 查看过期时间
   const payload = JSON.parse(atob(token.split('.')[1]));
   console.log('Token expires at:', new Date(payload.exp * 1000));
   ```

3. **检查密钥配置**：
   ```yaml
   # application.yml
   jwt:
     secret: your-secret-key
     expiration: 3600  # 1小时
   ```

4. **刷新 Token**：
   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/refresh \
        -H "Content-Type: application/json" \
        -d '{"refreshToken": "your-refresh-token"}'
   ```

## 🗄️ 数据库问题

### Q8: 数据库表不存在

**问题描述**：启动时出现 `Table 'qing.anime' doesn't exist` 错误。

**解决方案**：

1. **启用自动建表**：
   ```yaml
   # application.yml
   spring:
     jpa:
       hibernate:
         ddl-auto: update  # 开发环境使用
   ```

2. **手动执行 SQL 脚本**：
   ```bash
   # 执行初始化脚本
   mysql -u root -p qing < src/main/resources/sql/schema.sql
   mysql -u root -p qing < src/main/resources/sql/data.sql
   ```

3. **使用 Flyway 数据库迁移**：
   ```yaml
   # application.yml
   spring:
     flyway:
       enabled: true
       locations: classpath:db/migration
   ```

### Q9: 数据库连接池耗尽

**问题描述**：高并发时出现 `Connection pool exhausted` 错误。

**解决方案**：

1. **调整连接池配置**：
   ```yaml
   # application.yml
   spring:
     datasource:
       hikari:
         maximum-pool-size: 20
         minimum-idle: 5
         connection-timeout: 30000
         idle-timeout: 600000
         max-lifetime: 1800000
   ```

2. **检查连接泄漏**：
   ```java
   // 确保正确关闭连接
   try (Connection conn = dataSource.getConnection()) {
       // 数据库操作
   } catch (SQLException e) {
       // 异常处理
   }
   ```

3. **监控连接池状态**：
   ```yaml
   # 启用连接池监控
   management:
     endpoints:
       web:
         exposure:
           include: health,metrics
   ```

### Q10: 慢查询问题

**问题描述**：某些 API 响应时间过长。

**解决方案**：

1. **启用慢查询日志**：
   ```sql
   -- MySQL 配置
   SET GLOBAL slow_query_log = 'ON';
   SET GLOBAL long_query_time = 2;
   SET GLOBAL slow_query_log_file = '/var/log/mysql/slow.log';
   ```

2. **添加数据库索引**：
   ```sql
   -- 为常用查询字段添加索引
   CREATE INDEX idx_anime_category_status ON anime(category_id, status);
   CREATE INDEX idx_anime_create_time ON anime(create_time);
   ```

3. **优化查询语句**：
   ```java
   // 使用分页查询
   @Query("SELECT a FROM Anime a WHERE a.category.id = :categoryId")
   Page<Anime> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
   
   // 使用投影减少数据传输
   @Query("SELECT new com.stanic.qing.dto.AnimeListVO(a.id, a.name, a.coverImage) FROM Anime a")
   List<AnimeListVO> findAnimeList();
   ```

## 🔄 缓存问题

### Q11: Redis 缓存穿透

**问题描述**：大量请求查询不存在的数据，导致数据库压力过大。

**解决方案**：

1. **缓存空值**：
   ```java
   @Cacheable(value = "anime", key = "#id", unless = "#result == null")
   public AnimeVO getAnime(Long id) {
       AnimeVO anime = animeRepository.findById(id);
       if (anime == null) {
           // 缓存空值，设置较短过期时间
           redisTemplate.opsForValue().set("anime:" + id, "NULL", 5, TimeUnit.MINUTES);
       }
       return anime;
   }
   ```

2. **布隆过滤器**：
   ```java
   @Component
   public class BloomFilterService {
       private BloomFilter<Long> animeBloomFilter;
       
       @PostConstruct
       public void init() {
           animeBloomFilter = BloomFilter.create(Funnels.longFunnel(), 1000000, 0.01);
           // 初始化时加载所有存在的ID
           List<Long> existingIds = animeRepository.findAllIds();
           existingIds.forEach(animeBloomFilter::put);
       }
       
       public boolean mightExist(Long id) {
           return animeBloomFilter.mightContain(id);
       }
   }
   ```

### Q12: 缓存雪崩

**问题描述**：大量缓存同时失效，导致数据库瞬间压力过大。

**解决方案**：

1. **设置随机过期时间**：
   ```java
   @Cacheable(value = "anime", key = "#id")
   public AnimeVO getAnime(Long id) {
       // 设置随机过期时间，避免同时失效
       int randomExpire = 3600 + new Random().nextInt(600); // 1-1.1小时
       redisTemplate.expire("anime:" + id, randomExpire, TimeUnit.SECONDS);
       return animeRepository.findById(id);
   }
   ```

2. **多级缓存**：
   ```java
   @Service
   public class AnimeService {
       
       @Autowired
       private CacheManager localCacheManager;
       
       @Autowired
       private RedisTemplate redisTemplate;
       
       public AnimeVO getAnime(Long id) {
           // 1. 本地缓存
           AnimeVO anime = localCacheManager.getCache("anime").get(id, AnimeVO.class);
           if (anime != null) return anime;
           
           // 2. Redis 缓存
           anime = (AnimeVO) redisTemplate.opsForValue().get("anime:" + id);
           if (anime != null) {
               localCacheManager.getCache("anime").put(id, anime);
               return anime;
           }
           
           // 3. 数据库
           anime = animeRepository.findById(id);
           if (anime != null) {
               redisTemplate.opsForValue().set("anime:" + id, anime, 3600, TimeUnit.SECONDS);
               localCacheManager.getCache("anime").put(id, anime);
           }
           return anime;
       }
   }
   ```

### Q13: 缓存击穿

**问题描述**：热点数据缓存失效时，大量并发请求直接访问数据库。

**解决方案**：

1. **分布式锁**：
   ```java
   @Service
   public class AnimeService {
       
       @Autowired
       private RedisTemplate redisTemplate;
       
       public AnimeVO getAnime(Long id) {
           String cacheKey = "anime:" + id;
           String lockKey = "lock:anime:" + id;
           
           // 尝试从缓存获取
           AnimeVO anime = (AnimeVO) redisTemplate.opsForValue().get(cacheKey);
           if (anime != null) return anime;
           
           // 获取分布式锁
           Boolean lockAcquired = redisTemplate.opsForValue()
               .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
           
           if (lockAcquired) {
               try {
                   // 双重检查
                   anime = (AnimeVO) redisTemplate.opsForValue().get(cacheKey);
                   if (anime != null) return anime;
                   
                   // 从数据库加载
                   anime = animeRepository.findById(id);
                   if (anime != null) {
                       redisTemplate.opsForValue().set(cacheKey, anime, 3600, TimeUnit.SECONDS);
                   }
                   return anime;
               } finally {
                   redisTemplate.delete(lockKey);
               }
           } else {
               // 等待其他线程加载完成
               Thread.sleep(100);
               return getAnime(id);
           }
       }
   }
   ```

## 🌐 网络和性能问题

### Q14: API 响应时间过长

**问题描述**：某些 API 接口响应时间超过 5 秒。

**解决方案**：

1. **启用性能监控**：
   ```java
   @RestController
   public class AnimeController {
       
       @GetMapping("/animes")
       @Timed(name = "anime.list", description = "Time taken to get anime list")
       public ResponseEntity<Page<AnimeVO>> getAnimeList(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "20") int size) {
           // 实现逻辑
       }
   }
   ```

2. **数据库查询优化**：
   ```java
   // 使用 JOIN FETCH 避免 N+1 查询
   @Query("SELECT a FROM Anime a JOIN FETCH a.category WHERE a.status = :status")
   List<Anime> findByStatusWithCategory(@Param("status") AnimeStatus status);
   
   // 使用分页查询
   @Query("SELECT a FROM Anime a WHERE a.name LIKE %:keyword%")
   Page<Anime> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
   ```

3. **异步处理**：
   ```java
   @Service
   public class AnimeService {
       
       @Async
       public CompletableFuture<List<AnimeVO>> getRecommendations(Long userId) {
           // 异步计算推荐列表
           List<AnimeVO> recommendations = calculateRecommendations(userId);
           return CompletableFuture.completedFuture(recommendations);
       }
   }
   ```

### Q15: 内存溢出 (OutOfMemoryError)

**问题描述**：应用运行一段时间后出现 OOM 错误。

**解决方案**：

1. **调整 JVM 参数**：
   ```bash
   # 启动参数
   java -Xms512m -Xmx2g -XX:+UseG1GC \
        -XX:+HeapDumpOnOutOfMemoryError \
        -XX:HeapDumpPath=/tmp/heapdump.hprof \
        -jar qing-service-anime.jar
   ```

2. **分析内存泄漏**：
   ```bash
   # 使用 MAT 分析 heap dump
   # 或使用 jstat 监控内存使用
   jstat -gc -t <pid> 5s
   ```

3. **优化代码**：
   ```java
   // 避免大对象创建
   @GetMapping("/animes/export")
   public void exportAnimes(HttpServletResponse response) {
       response.setContentType("application/vnd.ms-excel");
       
       try (OutputStream out = response.getOutputStream()) {
           // 流式处理，避免一次性加载大量数据
           animeRepository.findAll().forEach(anime -> {
               // 逐条写入，及时释放内存
               writeToExcel(anime, out);
           });
       }
   }
   ```

### Q16: 服务间调用超时

**问题描述**：微服务之间调用经常出现超时错误。

**解决方案**：

1. **调整超时配置**：
   ```yaml
   # application.yml
   feign:
     client:
       config:
         default:
           connectTimeout: 5000
           readTimeout: 10000
   
   ribbon:
     ConnectTimeout: 5000
     ReadTimeout: 10000
   ```

2. **启用重试机制**：
   ```java
   @Component
   public class FeignRetryConfig {
       
       @Bean
       public Retryer feignRetryer() {
           return new Retryer.Default(100, 1000, 3);
       }
   }
   ```

3. **熔断降级**：
   ```java
   @FeignClient(name = "user-service", fallback = UserServiceFallback.class)
   public interface UserServiceClient {
       
       @GetMapping("/users/{id}")
       UserVO getUser(@PathVariable Long id);
   }
   
   @Component
   public class UserServiceFallback implements UserServiceClient {
       
       @Override
       public UserVO getUser(Long id) {
           // 降级处理
           return UserVO.builder()
               .id(id)
               .name("Unknown User")
               .build();
       }
   }
   ```

## 🔍 调试和排错

### Q17: 如何查看详细的错误日志

**解决方案**：

1. **调整日志级别**：
   ```yaml
   # application.yml
   logging:
     level:
       com.stanic.qing: DEBUG
       org.springframework.web: DEBUG
       org.hibernate.SQL: DEBUG
       org.hibernate.type.descriptor.sql.BasicBinder: TRACE
   ```

2. **配置日志输出**：
   ```xml
   <!-- logback-spring.xml -->
   <configuration>
       <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
           <encoder>
               <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
           </encoder>
       </appender>
       
       <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
           <file>logs/application.log</file>
           <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
               <fileNamePattern>logs/application.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
               <maxFileSize>100MB</maxFileSize>
               <maxHistory>30</maxHistory>
           </rollingPolicy>
           <encoder>
               <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
           </encoder>
       </appender>
       
       <root level="INFO">
           <appender-ref ref="STDOUT"/>
           <appender-ref ref="FILE"/>
       </root>
   </configuration>
   ```

### Q18: 如何进行远程调试

**解决方案**：

1. **启用远程调试**：
   ```bash
   # 启动时添加调试参数
   java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 \
        -jar qing-service-anime.jar
   ```

2. **IDE 配置**：
   ```
   # IntelliJ IDEA
   Run -> Edit Configurations -> Add New -> Remote JVM Debug
   Host: localhost
   Port: 5005
   ```

3. **Docker 环境调试**：
   ```yaml
   # docker-compose.yml
   services:
     anime-service:
       image: qing/anime-service
       ports:
         - "8080:8080"
         - "5005:5005"  # 调试端口
       environment:
         - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
   ```

## 📊 监控和运维

### Q19: 如何监控应用健康状态

**解决方案**：

1. **启用 Actuator**：
   ```yaml
   # application.yml
   management:
     endpoints:
       web:
         exposure:
           include: health,info,metrics,prometheus
     endpoint:
       health:
         show-details: always
   ```

2. **自定义健康检查**：
   ```java
   @Component
   public class DatabaseHealthIndicator implements HealthIndicator {
       
       @Autowired
       private DataSource dataSource;
       
       @Override
       public Health health() {
           try (Connection connection = dataSource.getConnection()) {
               if (connection.isValid(1)) {
                   return Health.up()
                       .withDetail("database", "Available")
                       .withDetail("validationQuery", "SELECT 1")
                       .build();
               }
           } catch (SQLException e) {
               return Health.down()
                   .withDetail("database", "Unavailable")
                   .withException(e)
                   .build();
           }
           return Health.down().withDetail("database", "Unknown").build();
       }
   }
   ```

3. **配置告警**：
   ```yaml
   # Prometheus 告警规则
   groups:
     - name: qing-alerts
       rules:
         - alert: HighErrorRate
           expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
           for: 5m
           labels:
             severity: warning
           annotations:
             summary: "High error rate detected"
   ```

### Q20: 如何进行性能调优

**解决方案**：

1. **JVM 调优**：
   ```bash
   # G1GC 配置
   -XX:+UseG1GC
   -XX:MaxGCPauseMillis=200
   -XX:G1HeapRegionSize=16m
   -XX:+G1UseAdaptiveIHOP
   -XX:G1MixedGCCountTarget=8
   ```

2. **数据库连接池调优**：
   ```yaml
   spring:
     datasource:
       hikari:
         maximum-pool-size: 20
         minimum-idle: 5
         idle-timeout: 300000
         max-lifetime: 1800000
         connection-timeout: 20000
         validation-timeout: 5000
   ```

3. **缓存调优**：
   ```yaml
   spring:
     cache:
       caffeine:
         spec: maximumSize=1000,expireAfterWrite=10m
     redis:
       lettuce:
         pool:
           max-active: 20
           max-idle: 10
           min-idle: 5
   ```

## 🆘 获取更多帮助

如果以上解决方案无法解决您的问题，可以通过以下方式获取帮助：

### 📖 文档资源

- [快速开始](../tutorial-basics/getting-started) - 项目安装和启动指南
- [用户指南](../tutorial-basics/user-guide) - 详细的功能使用说明
- [API 文档](./api-docs) - 完整的 API 接口文档
- [架构设计](./architecture) - 技术架构和设计理念
- [开发指南](./development) - 开发规范和最佳实践

### 🤝 社区支持

- 🐛 [提交 Issue](https://github.com/stanic-xyz/qing/issues) - 报告 Bug 或请求新功能
- 💬 [参与讨论](https://github.com/stanic-xyz/qing/discussions) - 技术交流和问题讨论
- 📧 发送邮件：support@example.com

### 🔍 问题反馈模板

提交问题时，请提供以下信息：

```markdown
## 问题描述
简要描述遇到的问题

## 环境信息
- 操作系统：
- Java 版本：
- Spring Boot 版本：
- 数据库版本：

## 重现步骤
1. 第一步
2. 第二步
3. 第三步

## 期望结果
描述期望的正确行为

## 实际结果
描述实际发生的情况

## 错误日志
```

粘贴相关的错误日志

```

## 其他信息
其他可能有用的信息
```

---

> 💡 **提示**：常见问题文档会持续更新，建议收藏此页面以便随时查阅。如果您解决了文档中未涵盖的问题，欢迎贡献解决方案！
