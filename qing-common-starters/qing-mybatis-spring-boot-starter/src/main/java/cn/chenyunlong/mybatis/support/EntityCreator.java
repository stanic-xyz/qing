/*
 * Copyright (c) 2019-2022 YunLong Chen
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

import cn.chenyunlong.common.validator.CreateGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 实体产生器
 *
 * @author gim 2022/3/5 9:54 下午
 * @date 2022/11/14
 */
@Slf4j
public class EntityCreator<T> extends BaseEntityOperation implements Create<T>, UpdateHandler<T>, Executor<T> {

    private final BaseMapper<T> baseMapper;
    private T t;
    private Consumer<T> successHook = object -> log.info("save success");
    private Consumer<? super Throwable> errorHook = Throwable::printStackTrace;

    public EntityCreator(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }


    /**
     * 执行失败回调钩子
     *
     * @param consumer 消费者
     * @return {@link Executor}<{@link T}>
     */
    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

    /**
     * 创建
     *
     * @param supplier 供应商
     * @return {@link UpdateHandler}<{@link T}>
     */
    @Override
    public UpdateHandler<T> create(Supplier<T> supplier) {
        this.t = supplier.get();
        return this;
    }

    /**
     * 更新操作
     *
     * @param consumer 消费者
     * @return {@link Executor}<{@link T}>
     */
    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Preconditions.checkArgument(Objects.nonNull(t), "entity must supply");
        consumer.accept(this.t);
        return this;
    }

    /**
     * 执行
     *
     * @return {@link Optional}<{@link T}>
     */
    @Override
    public Optional<T> execute() {
        doValidate(this.t, CreateGroup.class);
        T save = Try.of(() -> {
                    baseMapper.insert(t);
                    return this.t;
                })
                .onSuccess(successHook)
                .onFailure(errorHook).getOrNull();
        return Optional.ofNullable(save);
    }

    /**
     * 执行成功回调钩子
     *
     * @param consumer 消费者
     * @return {@link Executor}<{@link T}>
     */
    @Override
    public Executor<T> successHook(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

}

