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

import cn.chenyunlong.common.validator.CreateGroup;
import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

/**
 * 实体类控制器。
 *
 * @author gim 2022/3/5 9:54 下午
 */
@Slf4j
public class EntityCreator<T, ID> extends BaseEntityOperation
    implements Create<T>, UpdateHandler<T>, Executor<T> {

    private final CrudRepository<T, ID> repository;
    private T data;
    private Consumer<T> successHook = t -> log.info("save success");
    private Consumer<? super Throwable> errorHook =
        throwable -> log.error("插入数据发生了异常", throwable);

    public EntityCreator(CrudRepository<T, ID> repository) {
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
        doValidate(this.data, CreateGroup.class);
        try {
            T save = repository.save(data);
            successHook.accept(save);
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

}

