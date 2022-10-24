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

package cn.chenyunlong.common.constants;

import java.util.Optional;

/**
 * @author gim
 **/
public enum ValidStatus implements BaseEnum<ValidStatus> {
    /**
     * 有效
     */
    VALID(1, "valid"),
    /**
     * 无效
     */
    INVALID(0, "invalid");
    private Integer code;
    private String name;

    ValidStatus(Integer code, String msg) {
        this.code = code;
        this.name = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Optional<ValidStatus> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(ValidStatus.class, code));
    }
}
