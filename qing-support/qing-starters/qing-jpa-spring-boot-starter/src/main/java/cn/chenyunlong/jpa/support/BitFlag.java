/*
 * Copyright (c) 2023  YunLong Chen
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

import cn.chenyunlong.common.constants.BaseEnum;
import lombok.Getter;

import java.util.Optional;

@Getter
public enum BitFlag implements BaseEnum<Integer> {
    Y(1, "是"),
    N(0, "否");

    private final Integer code;
    private final String name;

    BitFlag(Integer code, String msg) {
        this.code = code;
        this.name = msg;
    }

    public static Optional<BitFlag> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, BitFlag.class));
    }

    /**
     * 获取枚举的实际类型。
     *
     * @return 枚举值
     */
    @Override
    public Integer getValue() {
        return code;
    }

}
