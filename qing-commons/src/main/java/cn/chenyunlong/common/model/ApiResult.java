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

package cn.chenyunlong.common.model;

import cn.chenyunlong.common.exception.SystemErrorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 全局返回数据类型
 *
 * @author Stan
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "返回数据值")
public class ApiResult<T> {
    @Schema(title = "错误代码", description = "返回值编码；0表示成功，其他全部失败")
    private int code;
    @Schema(title = "提示信息", description = "异常提示信息")
    private String msg;
    @Schema(title = "返回值", description = "具体返回值信息,如果为一般表示请求成功")
    private T data;

    /**
     * Creates an ok result. (Default status is 200)
     *
     * @return ok result with message and data
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<T>(0, "success", null);
    }

    /**
     * Creates an ok result with data only. (Default message is OK, status is 200)
     *
     * @param data data to response
     * @return base response with data
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(0, SystemErrorType.SUCCESS.getMsg(), data);
    }

    /**
     * Creates an error result. (Default message is OK, status is 400)
     *
     * @param <T> data type
     * @return base response with data
     */
    public static <T> ApiResult<T> fail(String msg) {
        return new ApiResult<T>(1, msg, null);
    }

    public ApiResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
