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

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.converter.InstantLongConverter;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Instant;

/**
 * 基础jpa类型
 *
 * @author Stan
 * @since 2022/11/05
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Embeddable
public abstract class BaseJpaAggregate extends AbstractAggregateRoot<BaseJpaAggregate> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Convert(converter = InstantLongConverter.class)
    @Setter(AccessLevel.PROTECTED)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @Convert(converter = InstantLongConverter.class)
    @Setter(AccessLevel.PROTECTED)
    private Instant updatedAt;

    /**
     * 数据状态
     */
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    /**
     * 乐观锁字段
     */
    @Version
    @Column(name = "version")
    @Setter(AccessLevel.PRIVATE)
    private Integer version;


    @PrePersist
    public void prePersist() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(Instant.now());
    }


    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }

}
