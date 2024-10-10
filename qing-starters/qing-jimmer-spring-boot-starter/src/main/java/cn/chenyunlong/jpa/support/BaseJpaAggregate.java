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

package cn.chenyunlong.jpa.support;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.domain.AggregateId;
import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

/**
 * 基础jpa类型。
 *
 * @author 陈云龙
 * @since 2022/11/05
 */

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseJpaAggregate extends AbstractAggregateRoot<BaseJpaAggregate> implements Serializable {

    private AggregateId id;


    @Setter(AccessLevel.PROTECTED)
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Setter(AccessLevel.PROTECTED)
    private Instant updatedAt;

    /**
     * 数据状态。
     */
    private ValidStatus validStatus;

    /**
     * 乐观锁字段。
     */
    private Integer version;

    /**
     * 创建人。
     */
    // @Column(name = "createBy")
    private String createBy;

    /**
     * 更新人。
     */
    // @Column(name = "updateBy")
    private String updateBy;

    /**
     * 备注。
     */
    // @Column(name = "remark")
    private String remark;


    /**
     * 持久化之前。
     */
    // @PrePersist
    public void prePersist() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
        if (validStatus == null) {
            this.validStatus = ValidStatus.VALID;
        }
        if (version == null) {
            this.version = 1;
        }
    }

    // @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(Instant.now());
        this.setVersion(this.version + 1);
    }

    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        Assert.equals(validStatus, ValidStatus.VALID, "数据已失效");
        setValidStatus(ValidStatus.INVALID);
    }

    @Override
    public Collection<Object> domainEvents() {
        return super.domainEvents();
    }
}
