# Changelog

本文档记录了项目的重要变更历史。

## 2025-01-15

### qing-domain-common 模块重构

#### 🔧 重构 (Refactor)

**泛型类型系统优化**
- 修复了 `EntityCreator`、`EntityUpdater`、`BaseEntityService`、`EntityOperations`、`BaseRepository` 等类的泛型类型声明问题
- 统一使用 `EntityId<?>` 接口替代 `AggregateId`，确保类型系统一致性
- 解决了泛型边界声明不匹配导致的编译错误

**API 设计优化**
- 创建了 `SimpleEntityService` 类，提供简化的实体服务API
  - 支持实体的创建、更新、查找和保存操作
  - 提供多种方法重载以支持不同的使用场景
  - 包含成功和错误回调处理机制
- 新增 `EntityUtils` 工具类，提供静态方法简化实体操作
  - `createAndSave()` - 创建并保存实体
  - `createUpdateAndSave()` - 创建、更新并保存实体
  - `findUpdateAndSave()` - 查找、更新并保存实体
  - `updateAndSave()` - 更新并保存实体
  - `findSafely()` - 安全查找实体
  - `saveSafely()` - 安全保存实体
  - `exists()` - 检查实体是否存在
- 创建了 `package-info.java` 文件，提供详细的API使用指南

#### 🧪 测试 (Test)

**测试基础设施**
- 创建了完整的测试目录结构 `src/test/java` 和 `src/test/resources`
- 添加了测试依赖：JUnit 5、Mockito Core、Mockito JUnit Jupiter
- 创建了测试实体类 `TestEntity` 和 `TestEntityId`
- 创建了测试仓储接口 `TestEntityRepository`

**单元测试**
- 为 `SimpleEntityService` 创建了完整的单元测试 `SimpleEntityServiceTest`
  - 测试所有公共方法的成功场景
  - 测试异常处理场景
  - 使用 Mockito 进行依赖模拟
- 为 `EntityUtils` 创建了完整的单元测试 `EntityUtilsTest`
  - 测试所有静态工具方法
  - 覆盖成功和异常场景
  - 验证方法参数和返回值

#### 🐛 修复 (Fix)

**代码质量**
- 修复了 `EntityId.java` 文件中 Javadoc 格式问题
  - 在 `@return` 标签前添加了必要的空行
  - 解决了 Checkstyle 警告

**依赖管理**
- 在 `pom.xml` 中添加了测试依赖
  - JUnit Jupiter 用于单元测试框架
  - Mockito 用于模拟对象
  - Mockito JUnit Jupiter 用于集成支持

#### 📝 文档 (Documentation)

- 创建了包级别的API使用指南，详细说明了三种API使用方式：
  1. 流式API（适合复杂业务逻辑）
  2. 简化服务API（适合常见操作）
  3. 工具类API（适合简单快捷操作）
- 提供了每种API的使用示例和适用场景说明

#### ✅ 验证 (Verification)

- 所有代码编译通过，无编译错误
- 所有单元测试运行成功
- Checkstyle 检查通过，无代码质量警告
- 泛型类型系统完全兼容，类型安全得到保障

---

### 影响范围

本次重构主要影响 `qing-domain-common` 模块，为整个项目的领域层提供了更加稳定和易用的基础设施。重构后的API更加简洁明了，同时保持了原有的灵活性和扩展性。

### 后续计划

- 在其他模块中应用新的简化API
- 根据实际使用情况进一步优化API设计
- 添加更多的集成测试用例

---

## 2025-01-15 (下午) - DDD架构重构

### 🏗️ 架构重构 (Architecture Refactoring)

#### anime模块DDD重构

**领域实体ID类统一**
- 新增了完整的实体ID类体系：`CategoryId`、`TagId`、`AttachmentId`、`EpisodeId`、`FavoriteId`、`PlaybackProgressId`、`ProductId`、`RatingId`、`TypeId`、`WatchHistoryId`
- 统一实体标识符定义，符合DDD设计原则
- 所有ID类继承自`EntityId`接口，确保类型安全

**领域事件系统**
- 新增领域事件：`AnimeCreatedEvent`、`AnimeDeletedEvent`、`AnimeStatusChangedEvent`、`AnimeUpdatedEvent`
- 完善事件驱动架构支持
- 为后续的事件溯源和CQRS模式奠定基础

**领域层重构**
- 重构核心实体：`Anime`、`Attachment`、`Episode`、`Favorite`、`PlaybackProgress`、`Product`、`Rating`、`Type`、`WatchHistory`
- 统一仓储接口规范，所有Repository接口继承自基础仓储
- 完善领域对象的封装和业务逻辑
- 统一使用新的`EntityId`类型系统

**基础设施层重构**
- 新增`adapter`、`converter`包，完善适配器模式
- 重构`config`和`repository`包
- 移除`AnimeRepositoryJpaImpl`的`@Repository`注解，解决Bean重复定义问题
- 完善基础设施层的分层架构和统一数据访问层实现

**接口层重构**
- 优化`AnimeController`的API设计
- 更新应用服务层实现
- 完善查询服务和命令处理
- 统一接口层架构设计

**应用层重构**
- 重构`AnimeApplicationService`，优化应用服务
- 新增`AnimeCommandHandler`，实现命令处理
- 重构`AnimeQueryServiceImpl`，优化查询服务
- 新增`AnimeQueryPort`接口，定义查询端口
- 完善应用层的CQRS架构
- 统一应用服务的职责划分

#### auth模块DDD重构

**实体ID类统一**
- 新增`AuthenticationId`、`TokenId`、`SysMenuId`、`PlatformId`、`RoleId`、`QingUserId`等ID类
- 统一认证模块的实体标识符

**领域层重构**
- 重构`Authentication`、`Token`等领域实体
- 完善认证领域服务

**基础设施层优化**
- 新增`AuthInfrastructureConfig`配置类
- 重构仓储实现，移除重复的转换器
- 完善基础设施层架构

#### 核心配置优化

**项目配置完善**
- 更新checkstyle配置，优化代码检查规则
- 更新根目录和services模块的pom.xml配置
- 完善`BaseEntityOperation`和`DefaultCustomValidator`
- 优化`AggregateMapper`和`ValidationException`
- 更新`BaseJpaEntity`基础实体类
- 统一项目构建和依赖管理配置

**示例代码更新**
- 更新`OrderCreator`、`OrderQuery`、`OrderCreateRequest`等示例DTO
- 更新`OrderResponse`、`OrderUpdater`和`OrderMapper`
- 更新`ProductCreator`、`ProductQuery`、`ProductUpdater`等产品DTO
- 更新`ProductVO`视图对象
- 统一示例代码的结构和命名规范

### 📊 重构统计

- **提交次数**: 12次
- **修改文件**: 100+个文件
- **新增文件**: 50+个文件
- **删除文件**: 10+个文件
- **代码行数变更**: +2000/-800行

### 🎯 重构成果

1. **架构清晰**: 完全符合DDD分层架构，职责分离明确
2. **类型安全**: 统一的EntityId类型系统，编译时类型检查
3. **代码质量**: 通过Checkstyle检查，代码规范统一
4. **可维护性**: 模块化设计，易于扩展和维护
5. **测试覆盖**: 完善的单元测试，保证代码质量

### 🔄 后续优化方向

- 完善集成测试用例
- 添加性能测试
- 优化查询性能
- 完善事件驱动架构
- 添加API文档