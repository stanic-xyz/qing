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

package cn.chenyunlong.qing.domain.anime.domainservice.model.meta;


import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum InOutBizType implements BaseEnum {

    IN_FIRST(1, "初始入库"),
    OUT_TRANSFER(2, "调拨出库"),
    IN_TRANSFER(3, "调拨入库"),
    IN_BUY(4, "购买入库"),
    OUT_SALE(5, "销售出库");

    private final Integer code;
    private final String name;

    InOutBizType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<InOutBizType> of(Integer code) {
        return Arrays
                .stream(InOutBizType.values())
                .filter(inOutBizType -> Objects.equals(inOutBizType.getValue(), code))
                .findAny();
    }

    /**
     * 获取code码存入数据库
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
