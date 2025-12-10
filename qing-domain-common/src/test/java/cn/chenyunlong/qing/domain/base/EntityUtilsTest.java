/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.qing.domain.TestEntity;
import cn.chenyunlong.qing.domain.TestEntityRepository;
import cn.chenyunlong.qing.domain.TestIdentifiable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * EntityUtils工具类单元测试
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
@ExtendWith(MockitoExtension.class)
class EntityUtilsTest {

    @Mock
    private TestEntityRepository repository;

    private TestIdentifiable testId;
    private TestEntity testEntity;

    /**
     * 测试前的初始化
     */
    @BeforeEach
    void setUp() {
        testId = TestIdentifiable.newId();
        testEntity = new TestEntity(testId, "Test Entity");
    }

    /**
     * 测试创建并保存实体 - 成功场景
     */
    @Test
    void testCreateAndSave_Success() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = EntityUtils.createAndSave(
                repository,
                () -> new TestEntity("New Entity", "Description"));

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试创建并保存实体 - 异常场景
     */
    @Test
    void testCreateAndSave_Exception() {
        // Given
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Database error"));

        // When
        Optional<TestEntity> result = EntityUtils.createAndSave(
                repository,
                () -> new TestEntity("New Entity", "Description"));

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试创建、更新并保存实体 - 成功场景
     */
    @Test
    void testCreateUpdateAndSave_Success() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = EntityUtils.createUpdateAndSave(
                repository,
                () -> new TestEntity("New Entity", "Description"),
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试创建、更新并保存实体 - 异常场景
     */
    @Test
    void testCreateUpdateAndSave_Exception() {
        // Given
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Update failed"));

        // When
        Optional<TestEntity> result = EntityUtils.createUpdateAndSave(
                repository,
                () -> new TestEntity("New Entity", "Description"),
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试查找并更新实体 - 成功场景
     */
    @Test
    void testFindUpdateAndSave_Success() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = EntityUtils.findUpdateAndSave(
                repository,
                testId,
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).findById(testId);
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试查找并更新实体 - 实体不存在
     */
    @Test
    void testFindUpdateAndSave_EntityNotFound() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When
        Optional<TestEntity> result = EntityUtils.findUpdateAndSave(
                repository,
                testId,
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(testId);
        verify(repository, never()).save(any(TestEntity.class));
    }

    /**
     * 测试查找并更新实体 - 异常场景
     */
    @Test
    void testFindUpdateAndSave_Exception() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Save failed"));

        // When
        Optional<TestEntity> result = EntityUtils.findUpdateAndSave(
                repository,
                testId,
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(testId);
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试更新并保存实体 - 成功场景
     */
    @Test
    void testUpdateAndSave_Success() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = EntityUtils.updateAndSave(
                repository,
                testEntity,
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试更新并保存实体 - 异常场景
     */
    @Test
    void testUpdateAndSave_Exception() {
        // Given
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Update failed"));

        // When
        Optional<TestEntity> result = EntityUtils.updateAndSave(
                repository,
                testEntity,
                entity -> entity.setDescription("Updated Description"));

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试安全查找实体 - 成功场景
     */
    @Test
    void testFindSafely_Success() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When
        Optional<TestEntity> result = EntityUtils.findSafely(repository, testId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).findById(testId);
    }

    /**
     * 测试安全查找实体 - 实体不存在
     */
    @Test
    void testFindSafely_EntityNotFound() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When
        Optional<TestEntity> result = EntityUtils.findSafely(repository, testId);

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(testId);
    }

    /**
     * 测试安全查找实体 - 异常场景
     */
    @Test
    void testFindSafely_Exception() {
        // Given
        when(repository.findById(testId)).thenThrow(new RuntimeException("Database error"));

        // When
        Optional<TestEntity> result = EntityUtils.findSafely(repository, testId);

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(testId);
    }

    /**
     * 测试安全保存实体 - 成功场景
     */
    @Test
    void testSaveSafely_Success() {
        // Given
        when(repository.save(testEntity)).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = EntityUtils.saveSafely(repository, testEntity);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试安全保存实体 - 异常场景
     */
    @Test
    void testSaveSafely_Exception() {
        // Given
        when(repository.save(testEntity)).thenThrow(new RuntimeException("Save failed"));

        // When
        Optional<TestEntity> result = EntityUtils.saveSafely(repository, testEntity);

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试检查实体是否存在 - 存在
     */
    @Test
    void testExists_EntityExists() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When
        boolean result = EntityUtils.exists(repository, testId);

        // Then
        assertTrue(result);
        verify(repository, times(1)).findById(testId);
    }

    /**
     * 测试检查实体是否存在 - 不存在
     */
    @Test
    void testExists_EntityNotExists() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When
        boolean result = EntityUtils.exists(repository, testId);

        // Then
        assertFalse(result);
        verify(repository, times(1)).findById(testId);
    }

    /**
     * 测试检查实体是否存在 - 异常场景
     */
    @Test
    void testExists_Exception() {
        // Given
        when(repository.findById(testId)).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = EntityUtils.exists(repository, testId);

        // Then
        assertFalse(result);
        verify(repository, times(1)).findById(testId);
    }
}
