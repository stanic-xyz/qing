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

package cn.chenyunlong.common.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 校验状态。
 *
 * @author gim
 **/
@Getter
public enum ValidStatus implements BaseEnum<Integer> {

    /**
     * 有效。
     */
    VALID(1, "valid"),

    /**
     * 无效。
     */
    INVALID(0, "invalid");

    @JsonValue
    private final Integer value;
    private final String name;

    ValidStatus(Integer value, String msg) {
        this.value = value;
        this.name = msg;
    }

    public boolean boolValue() {
        return this.value == 1;
    }
}
