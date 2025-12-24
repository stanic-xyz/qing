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

package cn.chenyunlong.qing.infrastructure.entity;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.hutool.core.lang.Assert;
import lombok.experimental.Accessors;


/**
 * 基础实体类。
 *
 * @author 陈云龙
 * @since 2021/05/22
 */
@Accessors(chain = true)
public class BaseEntity {

    /**
     * 数据状态。
     */
    private ValidStatus validStatus;

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        Assert.equals(validStatus, ValidStatus.VALID, "数据已失效");
        setValidStatus(ValidStatus.INVALID);
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }
}
