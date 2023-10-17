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

package cn.chenyunlong.security.exception;

import lombok.Getter;

/**
 * Token 解析异常。
 */
@Getter
public class ParseTokenException extends RuntimeException {

    private final Integer code;
    private final String msg;

    /**
     * token解析异常构造函数。
     *
     * @param code 错误代码
     * @param msg  错误消息
     */
    public ParseTokenException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
