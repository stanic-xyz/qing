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

public enum ProductType implements BaseEnum<ProductType> {
    COURSE(1, "课程"),
    ENTITY(2, "实物"),
    VIRTUAL(3, "月卡"),
    PERSONAL_SERVICE(4, "私教"),
    SUBJECT_CARD(5, "学科卡");

    private final Integer code;
    private final String name;
    ProductType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<ProductType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(ProductType.class, code));
    }

    @Override
    public Integer getValue() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
