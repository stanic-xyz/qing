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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * SimpleEntityService单元测试
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
@ExtendWith(MockitoExtension.class)
class SimpleEntityServiceTest {

    @Mock
    private TestEntityRepository repository;

    private SimpleEntityService<TestEntity, TestIdentifiable> service;

    private TestIdentifiable testId;
    private TestEntity testEntity;

    /**
     * 测试前的初始化
     */
    @BeforeEach
    void setUp() {
        service = new SimpleEntityService<>(repository);
        testId = TestIdentifiable.newId();
        testEntity = new TestEntity(testId, "Test Entity");
    }

    /**
     * 测试创建实体 - 成功场景
     */
    @Test
    void testCreate_Success() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = service.create(() -> new TestEntity("New Entity", "Description"));

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试创建实体并更新 - 成功场景
     */
    @Test
    void testCreateWithUpdater_Success() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = service.create(
                () -> new TestEntity("New Entity", "Description"),
                entity -> entity.setDescription("Updated Description")
        );

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试创建实体 - 异常场景
     */
    @Test
    void testCreate_Exception() {
        // Given
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Database error"));
        AtomicReference<Throwable> capturedError = new AtomicReference<>();

        // When
        Optional<TestEntity> result = service.create(
                () -> new TestEntity("New Entity", "Description"),
                null,
                null,
                capturedError::set
        );

        // Then
        assertFalse(result.isPresent());
        assertNotNull(capturedError.get());
        assertEquals("Database error", capturedError.get().getMessage());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试创建实体 - 带成功回调
     */
    @Test
    void testCreateWithSuccessHook() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);
        AtomicBoolean successCalled = new AtomicBoolean(false);
        AtomicReference<TestEntity> capturedEntity = new AtomicReference<>();

        // When
        Optional<TestEntity> result = service.create(
                () -> new TestEntity("New Entity", "Description"),
                null,
                entity -> {
                    successCalled.set(true);
                    capturedEntity.set(entity);
                },
                null
        );

        // Then
        assertTrue(result.isPresent());
        assertTrue(successCalled.get());
        assertEquals(testEntity, capturedEntity.get());
        verify(repository, times(1)).save(any(TestEntity.class));
    }

    /**
     * 测试通过ID更新实体 - 成功场景
     */
    @Test
    void testUpdateById_Success() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = service.updateById(
                testId,
                entity -> entity.setDescription("Updated Description")
        );

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).findById(testId);
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试通过ID更新实体 - 实体不存在
     */
    @Test
    void testUpdateById_EntityNotFound() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // When
        Optional<TestEntity> result = service.updateById(
                testId,
                entity -> entity.setDescription("Updated Description")
        );

        // Then
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById(testId);
        verify(repository, never()).save(any(TestEntity.class));
    }

    /**
     * 测试通过ID更新实体 - 异常场景
     */
    @Test
    void testUpdateById_Exception() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(repository.save(any(TestEntity.class))).thenThrow(new RuntimeException("Update failed"));
        AtomicReference<Throwable> capturedError = new AtomicReference<>();

        // When
        Optional<TestEntity> result = service.updateById(
                testId,
                entity -> entity.setDescription("Updated Description"),
                null,
                capturedError::set
        );

        // Then
        assertFalse(result.isPresent());
        assertNotNull(capturedError.get());
        assertEquals("Update failed", capturedError.get().getMessage());
        verify(repository, times(1)).findById(testId);
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试更新已存在的实体 - 成功场景
     */
    @Test
    void testUpdate_Success() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);

        // When
        Optional<TestEntity> result = service.update(
                testEntity,
                entity -> entity.setDescription("Updated Description")
        );

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试更新已存在的实体 - 带成功回调
     */
    @Test
    void testUpdateWithSuccessHook() {
        // Given
        when(repository.save(any(TestEntity.class))).thenReturn(testEntity);
        AtomicBoolean successCalled = new AtomicBoolean(false);

        // When
        Optional<TestEntity> result = service.update(
                testEntity,
                entity -> entity.setDescription("Updated Description"),
                entity -> successCalled.set(true),
                null
        );

        // Then
        assertTrue(result.isPresent());
        assertTrue(successCalled.get());
        verify(repository, times(1)).save(testEntity);
    }

    /**
     * 测试查找实体
     */
    @Test
    void testFindById() {
        // Given
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));

        // When
        Optional<TestEntity> result = service.findById(testId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
        verify(repository, times(1)).findById(testId);
    }

    /**
     * 测试保存实体
     */
    @Test
    void testSave() {
        // Given
        when(repository.save(testEntity)).thenReturn(testEntity);

        // When
        TestEntity result = service.save(testEntity);

        // Then
        assertEquals(testEntity, result);
        verify(repository, times(1)).save(testEntity);
    }
}
