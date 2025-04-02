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

import cn.chenyunlong.common.validator.CreateGroup;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 实体类控制器。
 *
 * @author gim 2022/3/5 9:54 下午
 */
@Slf4j
public class EntityCreator<T extends BaseAggregate, ID extends AggregateId> extends BaseEntityOperation
    implements Create<T>, UpdateHandler<T>, Executor<T>, Validate<T> {

    private final BaseRepository<T, ID> repository;
    private T data;
    private Consumer<T> successHook = t -> log.info("save success");
    private CustomValidator<T> validator = DefaultCustomValidator.defaultValidator();
    private Consumer<? super Throwable> errorHook =
        throwable -> log.error("插入数据发生了异常", throwable);

    public EntityCreator(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }


    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

    /**
     * 创建对象。
     *
     * @param supplier 供应商
     * @return {@link UpdateHandler}<{@link T}>
     */
    @Override
    public UpdateHandler<T> create(Supplier<T> supplier) {
        this.data = supplier.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Preconditions.checkArgument(Objects.nonNull(data), "entity must supply");
        consumer.accept(this.data);
        return this;
    }

    @Override
    public Optional<T> execute() {
        try {
            if (validator != null) {
                validator.doValidate(this.data, CreateGroup.class);
            }
            T save = repository.save(data);
            successHook.accept(save);
            Collection<Object> objects = data.domainEvents();
            log.info("需要发布的领域事件：{}", objects);
            return Optional.of(save);
        } catch (Exception exception) {
            errorHook.accept(exception);
            return Optional.empty();
        }
    }

    @Override
    public Executor<T> successHook(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

    @Override
    public Executor<T> validate(CustomValidator<T> validator) {
        this.validator = validator;
        return this;
    }
}

