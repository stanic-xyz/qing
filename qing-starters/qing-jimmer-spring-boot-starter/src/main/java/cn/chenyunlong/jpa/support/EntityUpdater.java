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

package cn.chenyunlong.jpa.support;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.validator.UpdateGroup;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 实体类更新器。
 *
 * @author gim 2022/3/5 9:36 下午
 */
@Slf4j
public class EntityUpdater<T, ID> extends BaseEntityOperation
    implements Loader<T, ID>, UpdateHandler<T>, Executor<T> {

    private final BaseRepository<T, ID> repository;
    private T entity;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = Throwable::printStackTrace;

    public EntityUpdater(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * 创建服务。
     *
     * @param supplier 提供者
     * @return 创造者
     */
    public EntityUpdater<T, ID> create(Supplier<T> supplier) {
        this.entity = supplier.get();
        return this;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.entity, UpdateGroup.class);
        try {
            T save = repository.save(entity);
            successHook.accept(save);
            return Optional.of(save);
        } catch (Exception exception) {
            errorHook.accept(exception);
        }
        return Optional.empty();
    }

    /**
     * 根据主键标识查询实体类。
     *
     * @param id id 主键标识
     * @return 更新操作处理器。
     */
    @Override
    public UpdateHandler<T> loadById(ID id) {
        Preconditions.checkArgument(Objects.nonNull(id), "id is null");
        Optional<T> loadEntity = repository.findById(id);
        this.entity = loadEntity.orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
        return this;
    }

    @Override
    public UpdateHandler<T> load(Supplier<T> t) {
        this.entity = t.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Preconditions.checkArgument(Objects.nonNull(entity), "entity is null");
        consumer.accept(this.entity);
        return this;
    }

    @Override
    public Executor<T> successHook(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

}
