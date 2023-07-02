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

package cn.chenyunlong.common.enums;

import cn.chenyunlong.common.constants.BaseEnum;
import lombok.Getter;

/**
 * 视频的状态
 */
public enum VideoStatus implements BaseEnum<Integer> {
    /**
     * 正常
     */
    NORMAL(0, "正常"),
    /**
     * 被禁用
     */
    DISABLED(1, "被禁用");

    @Getter
    private final Integer option;

    @Getter
    private final String optionName;

    VideoStatus(Integer value, String optionName) {
        this.option = value;
        this.optionName = optionName;
    }

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return option;
    }

    /**
     * 获取编码名称，便于维护
     *
     * @return 获取编码名称
     */
    public String getName() {
        return optionName;
    }
}
