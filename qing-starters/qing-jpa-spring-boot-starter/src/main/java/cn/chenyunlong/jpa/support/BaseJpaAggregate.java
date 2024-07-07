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
import cn.chenyunlong.jpa.support.converter.InstantLongConverter;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

/**
 * 基础jpa类型。
 *
 * @author 陈云龙
 * @since 2022/11/05
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Embeddable
@MappedSuperclass
public abstract class BaseJpaAggregate extends AbstractAggregateRoot<BaseJpaAggregate> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Convert(converter = InstantLongConverter.class)
    @Setter(AccessLevel.PROTECTED)
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    @Convert(converter = InstantLongConverter.class)
    @Setter(AccessLevel.PROTECTED)
    private Instant updatedAt;

    /**
     * 数据状态。
     */
    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    /**
     * 乐观锁字段。
     */
    @Version
    @Column(name = "version")
    private Integer version;

    /**
     * 创建人。
     */
    private String createBy;

    /**
     * 更新人。
     */
    private String updateBy;

    /**
     * 备注。
     */
    @Column
    private String remark;


    /**
     * 持久化之前。
     */
    @PrePersist
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

    @PreUpdate
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
        setValidStatus(ValidStatus.INVALID);
    }

}
