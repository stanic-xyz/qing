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

package cn.chenyunlong.mybatis.support;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.validator.UpdateGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

/**
 * 实体类更新器。
 *
 * @author gim
 */
@Slf4j
public class EntityUpdater<T> extends BaseEntityOperation implements Loader<T>,
                                                                         UpdateHandler<T>, Executor<T> {

    private final BaseMapper<T> baseMapper;
    private T entity;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = Throwable::printStackTrace;

    public EntityUpdater(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.entity, UpdateGroup.class);
        try {
            baseMapper.updateById(entity);
            successHook.accept(entity);
            return Optional.of(entity);
        } catch (Exception exception) {
            errorHook.accept(exception);
            return Optional.empty();
        }
    }

    @Override
    public UpdateHandler<T> loadById(Serializable id) {
        Preconditions.checkArgument(Objects.nonNull(id), "id is null");
        T t = baseMapper.selectById(id);
        if (Objects.isNull(t)) {
            throw new BusinessException(CodeEnum.NotFindError);
        } else {
            this.entity = t;
        }
        return this;
    }

    @Override
    public UpdateHandler<T> load(Supplier<T> supplier) {
        this.entity = supplier.get();
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
