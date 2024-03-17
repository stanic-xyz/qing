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

package cn.chenyunlong.common.exception;


import cn.chenyunlong.common.constants.BaseEnum;
import lombok.Getter;

/**
 * 业务错误。
 *
 * @author gim 强制业务异常必须提供code码，便于统一维护
 */
@Getter
public class BusinessException extends RuntimeException {

    private BaseEnum<Integer> code;

    @Getter private Object data;

    public BusinessException(BaseEnum<Integer> code) {
        super(code.getName());
        this.code = code;
    }

    public BusinessException(Object data) {
        this.data = data;
    }

    /**
     * 生成基础异常
     *
     * @param code 错误码
     * @param data 错误信息
     */
    public BusinessException(BaseEnum<Integer> code, Object data) {
        super(code.getName());
        this.code = code;
        this.data = data;
    }

    public BaseEnum getCode() {
        return code;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
