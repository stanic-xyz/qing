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

package cn.chenyunlong.qing.infrastructure.domain;

import cn.chenyunlong.qing.infrastructure.utils.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base entity.
 *
 * @author Stan
 * @date 2021/05/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class BaseEntity<DOMAIN extends BaseEntity<DOMAIN>> extends Model<DOMAIN> implements Serializable {

    /**
     * Create time.
     */
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    private LocalDateTime updateTime;

    /**
     * 搜索值
     */
    private String searchValue;

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
    private String remark;


    /**
     * 数据检查
     */
    public void preCheck() {
        if (createBy == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
        if (StringUtils.isEmpty(searchValue)) {
            searchValue = "";
        }
        if (StringUtils.isEmpty(createBy)) {
            createBy = "system";
        }
        if (StringUtils.isEmpty(updateBy)) {
            updateBy = "";
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof BaseEntity;
    }

}
