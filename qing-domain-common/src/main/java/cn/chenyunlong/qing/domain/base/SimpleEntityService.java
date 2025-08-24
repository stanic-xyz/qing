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

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.domain.common.EntityId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 简化的实体服务类，提供更简单易用的API
 * 相比于流式API，这个类提供了更直接的方法调用方式
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
@Slf4j
@RequiredArgsConstructor
public class SimpleEntityService<T extends BaseAggregate<ID>, ID extends EntityId<?>> {

    private final BaseRepository<T, ID> repository;

    /**
     * 创建实体
     *
     * @param supplier 实体供应商
     * @return 创建的实体，如果失败返回空
     */
    public Optional<T> create(Supplier<T> supplier) {
        return create(supplier, null, null, null);
    }

    /**
     * 创建实体并进行更新操作
     *
     * @param supplier 实体供应商
     * @param updater  更新操作
     * @return 创建的实体，如果失败返回空
     */
    public Optional<T> create(Supplier<T> supplier, Consumer<T> updater) {
        return create(supplier, updater, null, null);
    }

    /**
     * 创建实体（完整版本）
     *
     * @param supplier    实体供应商
     * @param updater     更新操作
     * @param successHook 成功回调
     * @param errorHook   错误回调
     * @return 创建的实体，如果失败返回空
     */
    public Optional<T> create(Supplier<T> supplier,
            Consumer<T> updater,
            Consumer<T> successHook,
            Consumer<Throwable> errorHook) {
        try {
            T entity = supplier.get();
            if (updater != null) {
                updater.accept(entity);
            }

            T saved = repository.save(entity);

            if (successHook != null) {
                successHook.accept(saved);
            } else {
                log.info("Entity created successfully: {}", saved.getId());
            }

            return Optional.of(saved);
        } catch (Exception e) {
            if (errorHook != null) {
                errorHook.accept(e);
            } else {
                log.error("Failed to create entity", e);
            }
            return Optional.empty();
        }
    }

    /**
     * 通过ID更新实体
     *
     * @param id      实体ID
     * @param updater 更新操作
     * @return 更新后的实体，如果失败返回空
     */
    public Optional<T> updateById(ID id, Consumer<T> updater) {
        return updateById(id, updater, null, null);
    }

    /**
     * 通过ID更新实体（完整版本）
     *
     * @param id          实体ID
     * @param updater     更新操作
     * @param successHook 成功回调
     * @param errorHook   错误回调
     * @return 更新后的实体，如果失败返回空
     */
    public Optional<T> updateById(ID id,
            Consumer<T> updater,
            Consumer<T> successHook,
            Consumer<Throwable> errorHook) {
        try {
            Optional<T> entityOpt = repository.findById(id);
            if (entityOpt.isEmpty()) {
                log.warn("Entity not found with id: {}", id);
                return Optional.empty();
            }

            T entity = entityOpt.get();
            updater.accept(entity);

            T saved = repository.save(entity);

            if (successHook != null) {
                successHook.accept(saved);
            } else {
                log.info("Entity updated successfully: {}", saved.getId());
            }

            return Optional.of(saved);
        } catch (Exception e) {
            if (errorHook != null) {
                errorHook.accept(e);
            } else {
                log.error("Failed to update entity with id: {}", id, e);
            }
            return Optional.empty();
        }
    }

    /**
     * 更新已存在的实体
     *
     * @param entity  实体
     * @param updater 更新操作
     * @return 更新后的实体，如果失败返回空
     */
    public Optional<T> update(T entity, Consumer<T> updater) {
        return update(entity, updater, null, null);
    }

    /**
     * 更新已存在的实体（完整版本）
     *
     * @param entity      实体
     * @param updater     更新操作
     * @param successHook 成功回调
     * @param errorHook   错误回调
     * @return 更新后的实体，如果失败返回空
     */
    public Optional<T> update(T entity,
            Consumer<T> updater,
            Consumer<T> successHook,
            Consumer<Throwable> errorHook) {
        try {
            updater.accept(entity);

            T saved = repository.save(entity);

            if (successHook != null) {
                successHook.accept(saved);
            } else {
                log.info("Entity updated successfully: {}", saved.getId());
            }

            return Optional.of(saved);
        } catch (Exception e) {
            if (errorHook != null) {
                errorHook.accept(e);
            } else {
                log.error("Failed to update entity: {}", entity.getId(), e);
            }
            return Optional.empty();
        }
    }

    /**
     * 查找实体
     *
     * @param id 实体ID
     * @return 实体，如果不存在返回空
     */
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * 保存实体
     *
     * @param entity 实体
     * @return 保存后的实体
     */
    public T save(T entity) {
        return repository.save(entity);
    }
}