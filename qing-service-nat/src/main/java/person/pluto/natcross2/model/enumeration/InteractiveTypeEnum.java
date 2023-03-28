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

package person.pluto.natcross2.model.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 交互类型enum
 * </p>
 *
 * @author Pluto
 * @since 2019-07-17 09:50:33
 */
public enum InteractiveTypeEnum {
    //
    UNKNOW("未知"),
    //
    COMMON_REPLY("通用回复标签"),
    //
    HEART_TEST("发送心跳"),
    //
    HEART_REPLY("心跳检测回复"),
    //
    SERVER_WAIT_CLIENT("需求客户端建立连接"),
    //
    CLIENT_CONNECT("客户端建立通道连接"),
    //
    CLIENT_CONTROL("客户端控制端口建立连接"),
    //
    ;

    private final String describe;

    InteractiveTypeEnum(String describe) {
        this.describe = describe;
    }

    public static InteractiveTypeEnum getEnumByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (InteractiveTypeEnum e : InteractiveTypeEnum.values()) {
            if (StringUtils.equals(name, e.name())) {
                return e;
            }
        }
        return null;
    }

    public String getDescribe() {
        return describe;
    }
}
