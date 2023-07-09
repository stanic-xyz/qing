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

package cn.chenyunlong.jpa.support.domain;

import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;


/**
 * Base entity.
 *
 * @author Stan
 * @since 2021/05/22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Embeddable
@MappedSuperclass
public class BaseEntity extends BaseJpaAggregate {

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 备注
     */
    @Column
    private String remark;


    /**
     * 数据检查
     */
    @Override
    public void prePersist() {
        super.prePersist();
        if (!StringUtils.hasText(createBy)) {
            createBy = "system";
        }
        if (!StringUtils.hasText(updateBy)) {
            updateBy = "";
        }
    }

    @Override
    public void preUpdate() {
        if (!StringUtils.hasLength(updateBy)) {
            updateBy = "";
        }
    }


}
