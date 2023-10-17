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

/**
 * 消息类型。
 *
 * @author Stan
 */
@SuppressWarnings("unused")
public enum MessageType implements BaseEnum<Integer> {

    /**
     * 普通消息。
     */
    SIMPLE_MESSAGE(0, "普通消息"),
    /**
     * 带图片的消息。
     */
    MESSAGE_WITH_IMG(1, "带图片的消息");


    private final String typeName;

    private final Integer value;

    MessageType(int typeId, String typeName) {
        this.typeName = typeName;
        this.value = typeId;
    }


    /**
     * 获取code码存入数据库。
     *
     * @return 获取编码
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取编码名称，便于维护。
     *
     * @return 获取编码名称
     */
    @Override
    public String getName() {
        return typeName;
    }
}
