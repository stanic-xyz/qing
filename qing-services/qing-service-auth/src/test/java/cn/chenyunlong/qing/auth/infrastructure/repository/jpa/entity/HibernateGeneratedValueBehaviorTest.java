package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity;

import cn.chenyunlong.qing.auth.infrastructure.repository.TestEntityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Hibernate 6.6+ @GeneratedValue 行为测试")
class HibernateGeneratedValueBehaviorTest {

    @Autowired
    private TestEntityRepository repository;

    @PersistenceContext
    private EntityManager entityManager;


    @BeforeEach
    void setUp() {
        // 清空测试数据
        repository.deleteAll();
    }

    @Test
    @DisplayName("测试正常插入 - 不设置ID")
    @Transactional
    void testNormalInsertWithoutId() {
        // 正常情况：不设置ID
        TestEntity entity = new TestEntity();
        entity.setName("正常实体");

        TestEntity saved = repository.save(entity);

        assertNotNull(saved.getId(), "ID应该被自动生成");
        assertEquals("正常实体", saved.getName());
        assertTrue(saved.getId() > 0, "ID应该大于0");
    }

    @Test
    @DisplayName("测试问题场景 - 手动设置ID会导致异常")
    @Transactional
    void testInsertWithManualIdShouldFail() {
        // 问题场景：手动设置ID
        TestEntity entity = new TestEntity();
        entity.setId(999L);  // 手动设置ID
        entity.setName("手动设置ID的实体");

        // 验证是否会抛出预期的异常
        Exception exception = assertThrows(
                ObjectOptimisticLockingFailureException.class,
                () -> repository.save(entity)
        );

        // 验证异常信息
        assertTrue(
                exception.getMessage().contains("unsaved-value mapping") ||
                        exception.getMessage().contains("Row was updated or deleted"),
                "应该出现预期的错误信息"
        );
    }

    @Test
    @DisplayName("测试合并操作 - 设置ID后使用merge")
    @Transactional
    void testMergeWithManualId() {
        // 先创建一个实体
        TestEntity entity = new TestEntity();
        entity.setName("原始实体");
        TestEntity saved = repository.save(entity);
        Long originalId = saved.getId();

        // 清空持久化上下文
        entityManager.clear();

        // 创建一个新实例并设置相同的ID
        TestEntity detachedEntity = new TestEntity();
        detachedEntity.setId(originalId);
        detachedEntity.setName("更新后的实体");

        // 使用merge而不是save
        TestEntity merged = entityManager.merge(detachedEntity);

        assertNotNull(merged);
        assertEquals(originalId, merged.getId());
        assertEquals("更新后的实体", merged.getName());
    }

    @Test
    @DisplayName("测试批量插入 - 确保ID为null")
    @Transactional
    void testBatchInsertWithoutIds() {
        // 创建多个实体，都不设置ID
        List<TestEntity> entities = Arrays.asList(
                createEntityWithoutId("实体1"),
                createEntityWithoutId("实体2"),
                createEntityWithoutId("实体3")
        );

        List<TestEntity> saved = repository.saveAll(entities);

        // 验证所有实体都有自动生成的ID
        assertTrue(saved.stream().allMatch(e -> e.getId() != null));
        assertTrue(saved.stream().allMatch(e -> e.getId() > 0));

        // 验证ID是唯一的
        Set<Long> ids = saved.stream()
                .map(TestEntity::getId)
                .collect(Collectors.toSet());
        assertEquals(3, ids.size(), "ID应该唯一");
    }

    @Test
    @DisplayName("测试反射设置ID的情况")
    @Transactional
    void testReflectionIdSetting() throws Exception {
        TestEntity entity = new TestEntity();
        entity.setName("反射设置ID");

        // 使用反射设置ID（模拟框架或序列化场景）
        Field idField = TestEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, 123L);

        // 验证这种情况下会失败
        assertThrows(
                ObjectOptimisticLockingFailureException.class,
                () -> repository.save(entity)
        );
    }

    @Test
    @DisplayName("测试更新现有实体")
    @Transactional
    void testUpdateExistingEntity() {
        // 先创建实体
        TestEntity entity = new TestEntity();
        entity.setName("初始名称");
        TestEntity saved = repository.save(entity);
        Long id = saved.getId();

        // 更新实体
        saved.setName("更新后的名称");
        TestEntity updated = repository.save(saved);

        assertEquals(id, updated.getId());
        assertEquals("更新后的名称", updated.getName());
    }

    private TestEntity createEntityWithoutId(String name) {
        TestEntity entity = new TestEntity();
        entity.setName(name);
        return entity;
    }

    @Test
    @DisplayName("验证Hibernate版本")
    void testHibernateVersion() {
        // 获取Hibernate版本并验证
        String hibernateVersion = org.hibernate.Version.getVersionString();
        assertNotNull(hibernateVersion);

        // 记录版本信息用于调试
        System.out.println("Hibernate版本: " + hibernateVersion);
    }
}
