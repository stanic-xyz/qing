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

package cn.chenyunlong.qing.domain.sku;


import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum GrantTimeType implements BaseEnum<GrantTimeType> {

    DAY(1, "天"),
    MONTH(2, "月"),
    YEAR(3, "年");

    private final Integer code;
    private final String name;
    GrantTimeType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<GrantTimeType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(GrantTimeType.class, code));
    }

    /**
     * @return
     */
    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
