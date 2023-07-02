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

package cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta;


import cn.chenyunlong.common.constants.BaseEnum;

import java.util.Optional;

public enum InOutType implements BaseEnum {

    IN(1, "入库"),
    OUT(2, "出库");

    private Integer code;
    private String name;

    InOutType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<InOutType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(code, InOutType.class));
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
