/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.common;

import java.time.Instant;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.data.domain.AbstractAggregateRoot;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.hutool.core.lang.Assert;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseAggregate<T extends EntityId<?>> extends AbstractAggregateRoot<BaseAggregate<T>> {

    private T id;

    /**
     * 数据状态。
     */
    private ValidStatus validStatus;

    /**
     * 乐观锁字段。
     */
    private Integer version = 0;

    private Instant createdAt;

    private Instant updatedAt;

    public void init() {
        setValidStatus(ValidStatus.VALID);
        this.setCreatedAt(Instant.now());
        if (validStatus == null) {
            this.validStatus = ValidStatus.VALID;
        }
    }

    public void valid() {
        Assert.notEquals(validStatus, ValidStatus.VALID, "状态错误");
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        Assert.notEquals(validStatus, ValidStatus.INVALID, "状态错误");
        setValidStatus(ValidStatus.INVALID);
    }

    @Nonnull
    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }

}
