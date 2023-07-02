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


/**
 * @author Stan
 * @date 2022/11/27
 */
public class ReturnModel {

    private ReturnModel(boolean success, Object data) {
    }

    /**
     * 放弃吧
     *
     * @param success 成功的结果
     * @param data
     * @return {@link ReturnModel}
     */
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
