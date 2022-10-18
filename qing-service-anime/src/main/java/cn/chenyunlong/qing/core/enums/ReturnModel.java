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

package cn.chenyunlong.qing.core.enums;


/**
 * <p>
 * boolean类型返回model
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:46:06
 */
public class ReturnModel {
    private boolean success;
    private Object data;

    private ReturnModel(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public static ReturnModel of(boolean success, Object data) {
        return new ReturnModel(success, data);
    }

    public static ReturnModel ofSuccess() {
        return new ReturnModel(true, null);
    }

    public static ReturnModel ofSuccess(Object data) {
        return new ReturnModel(true, data);
    }

    public static ReturnModel ofFail(Object data) {
        return new ReturnModel(false, data);
    }
}
