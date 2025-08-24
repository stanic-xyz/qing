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

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.converter.InstantLongConverter;
import cn.chenyunlong.qing.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private ValidStatus validStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Convert(converter = InstantLongConverter.class)
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    @Convert(converter = InstantLongConverter.class)
    private Instant updatedAt;

    /**
     * 乐观锁字段。
     */
    @Version
    @Column(name = "version")
    private Integer version = 0;

    /**
     * 创建人。
     */
    @Column(name = "createBy")
    private String createBy;

    /**
     * 持久化之前。
     */
    @PrePersist
    public void prePersist() {
        this.setUpdatedAt(Instant.now());
        if (this.createdAt == null) {
            this.setCreatedAt(Instant.now());
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(Instant.now());
        this.setVersion(this.version + 1);
    }
}
