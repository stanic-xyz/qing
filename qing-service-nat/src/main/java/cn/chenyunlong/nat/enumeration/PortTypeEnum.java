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

package cn.chenyunlong.nat.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>
 * 端口类型
 * </p>
 *
 * @author Pluto
 * @since 2020-01-10 11:39:40
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PortTypeEnum {

    //
    NORMAL(0, "http"),
    //
    HTTPS(10, "HTTPs"),
    //
    ;

    private final Integer code;
    private final String comment;

    PortTypeEnum(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static PortTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return NORMAL;
        }
        for (PortTypeEnum e : PortTypeEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return NORMAL;
    }

    public Integer getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

}
