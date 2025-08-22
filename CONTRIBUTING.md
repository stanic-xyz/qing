# 贡献指南

> 🤝 欢迎参与 Project-青微服务平台的开发！

感谢您对 Project-青项目的关注和支持！我们非常欢迎社区的贡献，无论是代码、文档、测试还是反馈建议。本指南将帮助您了解如何参与项目贡献。

## 📋 目录

- [行为准则](#行为准则)
- [贡献方式](#贡献方式)
- [开发环境搭建](#开发环境搭建)
- [代码贡献流程](#代码贡献流程)
- [代码规范](#代码规范)
- [提交规范](#提交规范)
- [测试要求](#测试要求)
- [文档贡献](#文档贡献)
- [问题反馈](#问题反馈)
- [社区支持](#社区支持)

## 🤝 行为准则

### 我们的承诺

为了营造一个开放和友好的环境，我们作为贡献者和维护者承诺，无论年龄、体型、残疾、种族、性别认同和表达、经验水平、国籍、个人形象、种族、宗教或性取向如何，参与我们项目和社区的每个人都能享受无骚扰的体验。

### 我们的标准

**积极行为包括**：
- ✅ 使用友好和包容的语言
- ✅ 尊重不同的观点和经验
- ✅ 优雅地接受建设性批评
- ✅ 关注对社区最有利的事情
- ✅ 对其他社区成员表示同情

**不当行为包括**：
- ❌ 使用性化语言或图像以及不受欢迎的性关注或性骚扰
- ❌ 恶意评论、人身攻击或政治攻击
- ❌ 公开或私下骚扰
- ❌ 未经明确许可发布他人的私人信息
- ❌ 在专业环境中可能被认为不当的其他行为

## 🎯 贡献方式

我们欢迎各种形式的贡献：

### 🐛 Bug 修复
- 发现并修复项目中的错误
- 提供重现步骤和修复方案
- 编写相关测试用例

### ✨ 新功能开发
- 实现新的功能特性
- 优化现有功能性能
- 增强用户体验

### 📚 文档改进
- 完善项目文档
- 翻译文档到其他语言
- 编写教程和示例

### 🧪 测试贡献
- 编写单元测试
- 进行集成测试
- 性能测试和压力测试

### 🎨 设计优化
- UI/UX 设计改进
- 图标和视觉元素设计
- 用户体验优化建议

### 💡 想法和建议
- 提出新功能建议
- 架构设计讨论
- 技术选型建议

## 🛠️ 开发环境搭建

### 环境要求

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| **JDK** | 17+ | 推荐使用 OpenJDK 17 |
| **Maven** | 3.8+ | 项目构建工具 |
| **Git** | 2.30+ | 版本控制 |
| **Docker** | 20.0+ | 容器化部署 |
| **Node.js** | 16+ | 前端开发（可选） |
| **IDE** | - | 推荐 IntelliJ IDEA |

### 快速搭建

1. **克隆项目**
   ```bash
   # 从 Gitee 克隆（推荐，国内访问更快）
   git clone https://gitee.com/stanChen/qing.git
   
   # 或从 GitHub 克隆
   git clone https://github.com/project-qing/qing.git
   
   cd qing
   ```

2. **启动基础设施**
   ```bash
   # 启动 MySQL、Redis、Nacos
   docker-compose -f docker/docker-compose-dev.yml up -d
   ```

3. **编译项目**
   ```bash
   # 编译整个项目
   mvn clean compile
   
   # 跳过测试快速编译
   mvn clean compile -DskipTests
   ```

4. **运行服务**
   ```bash
   # 启动网关服务
   cd qing-gateway
   mvn spring-boot:run
   
   # 启动认证服务
   cd ../qing-services/qing-service-auth
   mvn spring-boot:run
   
   # 启动业务服务（以anime为例）
   cd ../qing-service-anime
   mvn spring-boot:run
   ```

5. **验证环境**
   ```bash
   # 检查服务注册情况
   curl http://localhost:8848/nacos
   
   # 测试网关
   curl http://localhost:8080/actuator/health
   
   # 查看API文档
   open http://localhost:8080/doc.html
   ```

### IDE 配置

#### IntelliJ IDEA 推荐配置

1. **代码格式化**
   - 导入项目根目录的 `code-style.xml`
   - 设置自动格式化快捷键

2. **插件推荐**
   - Lombok Plugin
   - MyBatis Log Plugin
   - Maven Helper
   - Git Commit Template
   - SonarLint

3. **运行配置**
   - 配置 Spring Boot 运行模板
   - 设置环境变量和JVM参数
   - 配置数据库连接

## 🔄 代码贡献流程

### 1. Fork 项目

1. 访问项目主页：https://gitee.com/stanChen/qing
2. 点击右上角的 "Fork" 按钮
3. 将项目 Fork 到您的账户下

### 2. 创建功能分支

```bash
# 克隆您 Fork 的仓库
git clone https://gitee.com/your-username/qing.git
cd qing

# 添加上游仓库
git remote add upstream https://gitee.com/stanChen/qing.git

# 创建功能分支
git checkout -b feature/your-feature-name

# 或者修复分支
git checkout -b fix/your-bug-fix
```

### 3. 开发和测试

```bash
# 进行开发工作
# ...

# 运行测试
mvn test

# 代码格式检查
mvn checkstyle:check

# 代码质量检查
mvn sonar:sonar
```

### 4. 提交代码

```bash
# 添加修改的文件
git add .

# 提交代码（遵循提交规范）
git commit -m "feat: 添加用户管理功能"

# 推送到您的仓库
git push origin feature/your-feature-name
```

### 5. 创建 Pull Request

1. 访问您 Fork 的仓库页面
2. 点击 "Pull Request" 按钮
3. 填写 PR 标题和描述
4. 选择目标分支（通常是 `main`）
5. 提交 Pull Request

### 6. 代码审查

- 维护者会审查您的代码
- 根据反馈进行修改
- 通过审查后会被合并

## 📝 代码规范

### Java 代码规范

#### 命名规范

```java
// ✅ 好的命名
public class UserService {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private UserRepository userRepository;
    
    public User findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}

// ❌ 不好的命名
public class userservice {
    private static final String encoding = "UTF-8";
    private UserRepository repo;
    
    public User find(Long id) {
        return repo.findById(id);
    }
}
```

#### 代码格式

```java
// ✅ 好的格式
@Service
@Slf4j
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 根据用户ID查找用户
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    public User findUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("用户不存在: " + userId));
    }
}
```

#### 注释规范

```java
/**
 * 用户服务类
 * 
 * 提供用户相关的业务操作，包括用户查询、创建、更新和删除等功能
 * 
 * @author 作者姓名
 * @since 1.0.0
 */
@Service
public class UserService {
    
    /**
     * 创建新用户
     * 
     * @param userCreateRequest 用户创建请求
     * @return 创建的用户信息
     * @throws UserAlreadyExistsException 当用户已存在时抛出
     */
    public User createUser(UserCreateRequest userCreateRequest) {
        // 实现逻辑
    }
}
```

### 包结构规范

```
com.qing.service.user/
├── controller/          # 控制器层
│   ├── UserController.java
│   └── dto/            # 数据传输对象
│       ├── UserCreateRequest.java
│       └── UserResponse.java
├── service/            # 服务层
│   ├── UserService.java
│   └── impl/
│       └── UserServiceImpl.java
├── repository/         # 数据访问层
│   ├── UserRepository.java
│   └── mapper/
│       └── UserMapper.java
├── domain/            # 领域模型
│   ├── entity/
│   │   └── User.java
│   └── vo/
│       └── UserVO.java
├── config/            # 配置类
│   └── UserConfig.java
└── exception/         # 异常类
    └── UserNotFoundException.java
```

### 数据库规范

#### 表命名

```sql
-- ✅ 好的表名
CREATE TABLE qing_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ❌ 不好的表名
CREATE TABLE User (
    ID int,
    UserName varchar(50),
    Email varchar(100)
);
```

#### 字段命名

- 使用小写字母和下划线
- 布尔字段使用 `is_` 前缀
- 时间字段使用 `_time` 后缀
- 外键使用 `表名_id` 格式

### 代码风格配置

本项目采用 **EditorConfig + Checkstyle** 的双重配置方案来确保代码质量和风格一致性：

#### 🎯 配置分工

| 工具               | 职责    | 检查内容                            |
|------------------|-------|---------------------------------|
| **EditorConfig** | 代码格式化 | 缩进、空格、换行、编码、行尾等格式化规则            |
| **Checkstyle**   | 代码质量  | 命名约定、代码结构、Javadoc、最佳实践等Java特有规则 |

> 💡 **设计理念**: EditorConfig 负责"如何格式化"，Checkstyle 负责"如何编写"，两者互补不重叠。

#### 📁 配置文件

- **EditorConfig**: 项目根目录的 `.editorconfig` 文件
- **Checkstyle**: `checkstyle/checkstyle.xml` 文件

#### 🔧 IDE 配置指南

##### IntelliJ IDEA 配置

**1. 安装和启用 EditorConfig 支持**

IDEA 默认支持 EditorConfig，确保已启用：

```
File → Settings → Editor → Code Style
✅ Enable EditorConfig support
```

**2. 配置 Checkstyle 插件**

安装 Checkstyle-IDEA 插件：

```
File → Settings → Plugins
搜索并安装: CheckStyle-IDEA
```

配置 Checkstyle：

```
File → Settings → Tools → Checkstyle

配置项:
✅ Checkstyle version: 10.12.4 (或更高版本)
✅ Scan Scope: Only Java sources (including tests)

添加配置文件:
1. 点击 "+" 按钮
2. Description: Qing Project Checkstyle
3. File: 选择项目中的 checkstyle/checkstyle.xml
4. ✅ Store relative to project file
5. 点击 "Next" → "Finish"
6. ✅ 勾选新添加的配置使其生效
```

**3. 配置代码格式化**

```
File → Settings → Editor → Code Style → Java

重要设置:
✅ Use tab character: 取消勾选 (使用空格)
✅ Tab size: 4
✅ Indent: 4
✅ Continuation indent: 8

Imports 设置:
✅ Class count to use import with '*': 999
✅ Names count to use static import with '*': 999
```

**4. 启用实时检查**

```
File → Settings → Editor → Inspections
✅ Checkstyle real-time scan

在编辑器中:
View → Tool Windows → Checkstyle
点击 "Play" 按钮启用实时扫描
```

**5. 配置快捷键**

```
File → Settings → Keymap

推荐快捷键:
- Reformat Code: Ctrl+Alt+L
- Optimize Imports: Ctrl+Alt+O
- Checkstyle Scan: 自定义 (如 Ctrl+Alt+C)
```

##### Visual Studio Code 配置

**1. 安装必要插件**

```json
// 推荐插件列表
{
  "recommendations": [
    "EditorConfig.EditorConfig",
    "shengchen.vscode-checkstyle",
    "vscjava.vscode-java-pack",
    "redhat.java",
    "vscjava.vscode-maven"
  ]
}
```

**2. 配置 settings.json**

在项目根目录创建 `.vscode/settings.json`：

```json
{
  // EditorConfig 支持
  "editorconfig.generateAuto": false,
  
  // Java 格式化设置
  "java.format.settings.url": "./checkstyle/checkstyle.xml",
  "java.format.settings.profile": "GoogleStyle",
  
  // Checkstyle 配置
  "java.checkstyle.configuration": "./checkstyle/checkstyle.xml",
  "java.checkstyle.version": "10.12.4",
  
  // 保存时自动格式化
  "editor.formatOnSave": true,
  "editor.codeActionsOnSave": {
    "source.organizeImports": true
  },
  
  // 文件编码
  "files.encoding": "utf8",
  "files.eol": "\r\n",
  
  // 缩进设置
  "editor.insertSpaces": true,
  "editor.tabSize": 4,
  "editor.detectIndentation": false,
  
  // Java 特定设置
  "[java]": {
    "editor.tabSize": 4,
    "editor.insertSpaces": true,
    "editor.formatOnSave": true
  },
  
  // XML 文件设置
  "[xml]": {
    "editor.tabSize": 4,
    "editor.insertSpaces": true
  },
  
  // YAML 文件设置
  "[yaml]": {
    "editor.tabSize": 2,
    "editor.insertSpaces": true
  }
}
```

**3. 配置任务 (tasks.json)**

创建 `.vscode/tasks.json` 用于快速运行 Checkstyle：

```json
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "checkstyle:check",
      "type": "shell",
      "command": "mvn",
      "args": ["checkstyle:check"],
      "group": "build",
      "presentation": {
        "echo": true,
        "reveal": "always",
        "focus": false,
        "panel": "shared"
      },
      "problemMatcher": []
    },
    {
      "label": "format-code",
      "type": "shell",
      "command": "mvn",
      "args": ["spotless:apply"],
      "group": "build",
      "presentation": {
        "echo": true,
        "reveal": "always",
        "focus": false,
        "panel": "shared"
      }
    }
  ]
}
```

#### 🚀 使用流程

**开发时**:

1. 编写代码时，EditorConfig 自动处理格式化
2. IDE 实时显示 Checkstyle 检查结果
3. 保存文件时自动格式化和优化导入

**提交前**:

```bash
# 运行 Checkstyle 检查
mvn checkstyle:check

# 如果有违规，修复后重新检查
# 直到所有检查通过
```

**CI/CD 流程**:

- 自动运行 `mvn checkstyle:check`
- 检查失败时阻止合并
- 确保代码质量一致性

#### 📋 常见问题

**Q: EditorConfig 和 IDE 格式化冲突怎么办？**

A: EditorConfig 优先级更高，建议：

- 禁用 IDE 的自动格式化冲突设置
- 使用 EditorConfig 的配置为准
- 必要时可以在 IDE 中手动触发格式化

**Q: Checkstyle 检查失败但代码看起来正确？**

A: 检查以下几点：

- 确认使用的是项目指定的 checkstyle.xml
- 查看具体的错误信息和行号
- 参考项目中其他类似代码的写法
- 如有疑问可以在 PR 中讨论

**Q: 如何临时禁用某个 Checkstyle 规则？**

A: 使用注释方式：

```java
// CHECKSTYLE:OFF
代码块
// CHECKSTYLE:ON

// 或者禁用特定规则
@SuppressWarnings("checkstyle:MethodName")
public void method_with_underscores() {
    // 代码
}
```

> ⚠️ **注意**: 临时禁用应该谨慎使用，并在代码审查时说明原因。

## 📋 提交规范

我们使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

### 提交格式

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### 提交类型

| 类型 | 描述 | 示例 |
|------|------|------|
| `feat` | 新功能 | `feat: 添加用户登录功能` |
| `fix` | Bug修复 | `fix: 修复用户注册时邮箱验证问题` |
| `docs` | 文档更新 | `docs: 更新API文档` |
| `style` | 代码格式 | `style: 格式化代码` |
| `refactor` | 重构 | `refactor: 重构用户服务` |
| `test` | 测试 | `test: 添加用户服务单元测试` |
| `chore` | 构建/工具 | `chore: 更新依赖版本` |
| `perf` | 性能优化 | `perf: 优化数据库查询性能` |
| `ci` | CI配置 | `ci: 添加GitHub Actions配置` |
| `build` | 构建系统 | `build: 更新Maven配置` |
| `revert` | 回滚 | `revert: 回滚用户功能` |

### 提交示例

```bash
# 新功能
git commit -m "feat(user): 添加用户头像上传功能

- 支持JPG、PNG格式
- 自动压缩和裁剪
- 添加文件大小限制

Closes #123"

# Bug修复
git commit -m "fix(auth): 修复JWT token过期时间计算错误

修复了token过期时间计算不准确的问题，现在使用UTC时间进行计算。

Fixes #456"

# 文档更新
git commit -m "docs: 更新快速开始指南

- 添加Docker部署说明
- 更新环境要求
- 修复示例代码错误"
```

## 🧪 测试要求

### 测试覆盖率

- **单元测试覆盖率**: 不低于 80%
- **集成测试覆盖率**: 不低于 60%
- **核心业务逻辑**: 必须达到 90% 以上

### 测试类型

#### 1. 单元测试

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    @DisplayName("根据ID查找用户 - 成功")
    void findUserById_Success() {
        // Given
        Long userId = 1L;
        User expectedUser = User.builder()
            .id(userId)
            .username("testuser")
            .build();
        when(userRepository.findById(userId))
            .thenReturn(Optional.of(expectedUser));
        
        // When
        User actualUser = userService.findUserById(userId);
        
        // Then
        assertThat(actualUser).isEqualTo(expectedUser);
        verify(userRepository).findById(userId);
    }
    
    @Test
    @DisplayName("根据ID查找用户 - 用户不存在")
    void findUserById_UserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> userService.findUserById(userId))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("用户不存在: " + userId);
    }
}
```

#### 2. 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("创建用户 - 集成测试")
    void createUser_IntegrationTest() {
        // Given
        UserCreateRequest request = UserCreateRequest.builder()
            .username("newuser")
            .email("newuser@example.com")
            .build();
        
        // When
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
            "/api/users", request, UserResponse.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUsername()).isEqualTo("newuser");
        
        // 验证数据库
        Optional<User> savedUser = userRepository.findByUsername("newuser");
        assertThat(savedUser).isPresent();
    }
}
```

#### 3. 性能测试

```java
@Test
@DisplayName("用户查询性能测试")
@Timeout(value = 2, unit = TimeUnit.SECONDS)
void userQuery_PerformanceTest() {
    // 性能测试逻辑
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    
    for (int i = 0; i < 1000; i++) {
        userService.findUserById(1L);
    }
    
    stopWatch.stop();
    long executionTime = stopWatch.getTotalTimeMillis();
    
    // 断言执行时间小于阈值
    assertThat(executionTime).isLessThan(1000);
}
```

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 运行测试并生成覆盖率报告
mvn test jacoco:report

# 跳过测试
mvn install -DskipTests
```

## 📚 文档贡献

### 文档类型

1. **API文档**
   - 使用 Swagger/OpenAPI 3.0
   - 详细的接口说明和示例
   - 错误码和异常处理说明

2. **用户文档**
   - 快速开始指南
   - 功能使用教程
   - 最佳实践指南

3. **开发文档**
   - 架构设计文档
   - 代码规范说明
   - 部署运维指南

4. **示例代码**
   - 完整的示例项目
   - 代码片段和用法说明
   - 常见场景解决方案

### 文档规范

#### Markdown 格式

```markdown
# 一级标题

## 二级标题

### 三级标题

**重要内容加粗**

*斜体强调*

`代码片段`

```java
// 代码块
public class Example {
    // 代码内容
}
```

> 💡 **提示**: 这是一个提示信息

> ⚠️ **警告**: 这是一个警告信息

> ❌ **错误**: 这是一个错误信息

| 列1 | 列2 | 列3 |
|-----|-----|-----|
| 值1 | 值2 | 值3 |

- 无序列表项1
- 无序列表项2

1. 有序列表项1
2. 有序列表项2

[链接文本](https://example.com)

![图片描述](image-url)
```

#### API 文档规范

```java
/**
 * 用户管理接口
 * 
 * @author 作者姓名
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关操作接口")
public class UserController {
    
    /**
     * 创建用户
     * 
     * @param request 用户创建请求
     * @return 创建的用户信息
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建新的用户账户")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "409", description = "用户已存在")
    })
    public ResponseEntity<UserResponse> createUser(
        @Valid @RequestBody 
        @Parameter(description = "用户创建请求", required = true)
        UserCreateRequest request) {
        // 实现逻辑
    }
}
```

## 🐛 问题反馈

### Bug 报告

在提交 Bug 报告时，请包含以下信息：

#### Bug 报告模板

```markdown
## Bug 描述
简要描述遇到的问题

## 复现步骤
1. 进入页面 '...'
2. 点击按钮 '...'
3. 滚动到 '...'
4. 看到错误

## 期望行为
描述您期望发生的行为

## 实际行为
描述实际发生的行为

## 截图
如果适用，添加截图来帮助解释您的问题

## 环境信息
- 操作系统: [例如 Windows 10, macOS 12.0]
- 浏览器: [例如 Chrome 95, Safari 15]
- 项目版本: [例如 v1.0.0]
- Java版本: [例如 OpenJDK 17]

## 附加信息
添加任何其他有关问题的上下文信息

## 日志信息
```
粘贴相关的错误日志
```
```

### 功能请求

#### 功能请求模板

```markdown
## 功能描述
简要描述您希望添加的功能

## 问题背景
描述这个功能要解决的问题或改进的场景

## 解决方案
描述您希望的解决方案

## 替代方案
描述您考虑过的任何替代解决方案或功能

## 附加信息
添加任何其他有关功能请求的上下文或截图

## 优先级
- [ ] 高优先级（影响核心功能）
- [ ] 中优先级（改善用户体验）
- [ ] 低优先级（锦上添花）
```

### 问题标签

我们使用以下标签来分类问题：

| 标签 | 描述 | 颜色 |
|------|------|------|
| `bug` | 确认的错误 | 🔴 红色 |
| `enhancement` | 功能增强 | 🟢 绿色 |
| `documentation` | 文档相关 | 🔵 蓝色 |
| `good first issue` | 适合新手 | 🟡 黄色 |
| `help wanted` | 需要帮助 | 🟣 紫色 |
| `question` | 问题咨询 | 🟠 橙色 |
| `wontfix` | 不会修复 | ⚫ 黑色 |
| `duplicate` | 重复问题 | 🔘 灰色 |

## 🌟 社区支持

### 获取帮助

1. **文档查阅**
   - 📖 [项目文档](docs/)
   - 🚀 [快速开始](docs/快速开始.md)
   - 🏗️ [架构设计](docs/架构设计.md)

2. **社区讨论**
   - 💬 [GitHub Discussions](https://github.com/project-qing/qing/discussions)
   - 🎯 QQ群: 123456789
   - 📱 微信群: 扫码加入

3. **问题反馈**
   - 🐛 [Bug报告](https://gitee.com/stanChen/qing/issues/new?template=bug_report.md)
   - 💡 [功能请求](https://gitee.com/stanChen/qing/issues/new?template=feature_request.md)

### 社区活动

1. **定期活动**
   - 📅 每月技术分享会
   - 🏆 季度贡献者表彰
   - 🎉 年度开发者大会

2. **贡献激励**
   - 🏅 贡献者徽章
   - 🎁 优秀贡献者奖励
   - 📜 贡献证书

### 联系方式

- 📧 **项目邮箱**: project-qing@example.com
- 🐙 **GitHub**: https://github.com/project-qing
- 🦄 **Gitee**: https://gitee.com/stanChen/qing
- 🌐 **官方网站**: https://qing.example.com
- 📱 **微博**: @Project青团队

## 🏆 贡献者认可

### 贡献者等级

| 等级 | 要求 | 权限 | 徽章 |
|------|------|------|------|
| **新手贡献者** | 1个PR被合并 | 基础权限 | 🌱 |
| **活跃贡献者** | 5个PR被合并 | 代码审查 | 🌟 |
| **核心贡献者** | 20个PR被合并 | 项目管理 | 💎 |
| **项目维护者** | 长期贡献 | 完全权限 | 👑 |

### 贡献者墙

感谢所有为项目做出贡献的开发者！

<!-- 这里会自动生成贡献者列表 -->

### 特别感谢

- 🎯 **架构设计**: [@architect](https://github.com/architect)
- 🔧 **核心开发**: [@developer](https://github.com/developer)
- 📚 **文档维护**: [@writer](https://github.com/writer)
- 🧪 **测试保障**: [@tester](https://github.com/tester)
- 🎨 **UI设计**: [@designer](https://github.com/designer)

## 📄 许可证

本项目采用 [Apache License 2.0](LICENSE) 开源许可证。

通过贡献代码，您同意您的贡献将在相同许可证下授权。

---

## 🚀 开始贡献

准备好开始贡献了吗？

1. 🍴 Fork 项目
2. 🌿 创建功能分支
3. 💻 开始编码
4. ✅ 运行测试
5. 📝 提交代码
6. 🔄 创建 Pull Request

让我们一起构建更好的微服务平台！ 🎉

---

> 💡 **提示**: 如果您是第一次贡献开源项目，建议先从标有 `good first issue` 的问题开始。
> 
> 📅 **最后更新**: 2024年2月
> 
> 🔄 **文档版本**: v1.0.0
