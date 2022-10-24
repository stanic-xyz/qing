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

package cn.chenyunlong.common.exception;


import cn.chenyunlong.common.constants.BaseEnum;

/**
 * @author gim 强制业务异常必须提供code码，便于统一维护
 */
public class BusinessException extends RuntimeException {

    private final BaseEnum msg;
    private Object data;

    public BusinessException(BaseEnum msg) {
        super(msg.getName());
        this.msg = msg;
    }

    public BusinessException(BaseEnum msg, Object data) {
        super(msg.getName());
        this.msg = msg;
        this.data = data;
    }

    public BaseEnum getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
