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

import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 实体操作工具类，提供简化的静态方法
 * 这个类提供了最简单的API，适合快速开发和简单场景
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
@Slf4j
public final class EntityUtils {

    private EntityUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 创建并保存实体
     *
     * @param repository 仓储
     * @param supplier   实体供应商
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 创建的实体，如果失败返回空
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID> Optional<T> createAndSave(
            BaseRepository<T, ID> repository, Supplier<T> supplier) {
        try {
            T entity = supplier.get();
            T saved = repository.save(entity);
            log.debug("Entity created: {}", saved.getId());
            return Optional.of(saved);
        } catch (Exception e) {
            log.error("Failed to create entity", e);
            return Optional.empty();
        }
    }

    /**
     * 创建、更新并保存实体
     *
     * @param repository 仓储
     * @param supplier   实体供应商
     * @param updater    更新操作
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 创建的实体，如果失败返回空
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID>

    Optional<T> createUpdateAndSave(
            BaseRepository<T, ID> repository,
            Supplier<T> supplier,
            Consumer<T> updater) {
        try {
            T entity = supplier.get();
            updater.accept(entity);
            T saved = repository.save(entity);
            log.debug("Entity created and updated: {}", saved.getId());
            return Optional.of(saved);
        } catch (Exception e) {
            log.error("Failed to create and update entity", e);
            return Optional.empty();
        }
    }

    /**
     * 查找并更新实体
     *
     * @param repository 仓储
     * @param id         实体ID
     * @param updater    更新操作
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 更新后的实体，如果失败返回空
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID>

    Optional<T> findUpdateAndSave(
            BaseRepository<T, ID> repository,
            ID id,
            Consumer<T> updater) {
        try {
            Optional<T> entityOpt = repository.findById(id);
            if (entityOpt.isEmpty()) {
                log.warn("Entity not found: {}", id);
                return Optional.empty();
            }

            T entity = entityOpt.get();
            updater.accept(entity);
            T saved = repository.save(entity);
            log.debug("Entity updated: {}", saved.getId());
            return Optional.of(saved);
        } catch (Exception e) {
            log.error("Failed to update entity: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * 更新并保存实体
     *
     * @param repository 仓储
     * @param entity     实体
     * @param updater    更新操作
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 更新后的实体，如果失败返回空
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID>

    Optional<T> updateAndSave(
            BaseRepository<T, ID> repository,
            T entity,
            Consumer<T> updater) {
        try {
            updater.accept(entity);
            T saved = repository.save(entity);
            log.debug("Entity updated: {}", saved.getId());
            return Optional.of(saved);
        } catch (Exception e) {
            log.error("Failed to update entity: {}", entity.getId(), e);
            return Optional.empty();
        }
    }

    /**
     * 安全地查找实体
     *
     * @param repository 仓储
     * @param id         实体ID
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 实体，如果不存在返回空
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID>

    Optional<T> findSafely(
            BaseRepository<T, ID> repository, ID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Failed to find entity: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * 安全地保存实体
     *
     * @param repository 仓储
     * @param entity     实体
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 保存后的实体，如果失败返回空
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID>

    Optional<T> saveSafely(
            BaseRepository<T, ID> repository, T entity) {
        try {
            T saved = repository.save(entity);
            log.debug("Entity saved: {}", saved.getId());
            return Optional.of(saved);
        } catch (Exception e) {
            log.error("Failed to save entity: {}", entity.getId(), e);
            return Optional.empty();
        }
    }

    /**
     * 检查实体是否存在
     *
     * @param repository 仓储
     * @param id         实体ID
     * @param <T>        实体类型
     * @param <ID>       ID类型
     * @return 如果存在返回true，否则返回false
     */
    public static <T extends BaseSimpleBusinessEntity<ID>, ID>

    boolean exists(BaseRepository<T, ID> repository,
                   ID id) {
        try {
            return repository.findById(id).isPresent();
        } catch (Exception e) {
            log.error("Failed to check entity existence: {}", id, e);
            return false;
        }
    }
}
