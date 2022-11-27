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

package cn.chenyunlong.qing.infrastructure.enums;

import lombok.Data;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/**
 * <p>
 * 常规类型的前后端返回model
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:45:41
 */
@Data
public class ResultModel {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ResultModel.class);

    private Integer code;
    private String message;

    private ResultModel(Integer code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.data = object;
    }

    public static ResultModel of(Integer code, String message, Object object) {
        return new ResultModel(code, message, object);
    }

    public static ResultModel of(ResponseCode responseCode, Object data) {
        return new ResultModel(responseCode.getCode(), responseCode.getOptionName(), data);
    }

    public static ResultModel of(ResponseCode responseCode) {
        return new ResultModel(responseCode.getCode(), responseCode.getOptionName(), null);
    }

    public static ResultModel ofFail(Object data) {
        return new ResultModel(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getOptionName(), data);
    }

    public static ResultModel ofFail() {
        return new ResultModel(ResponseCode.FAIL.getCode(), ResponseCode.FAIL.getOptionName(), null);
    }

    public static ResultModel ofSuccess(Object data) {
        return new ResultModel(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getOptionName(), data);
    }

    private Object data;

    public static ResultModel ofSuccess() {
        return new ResultModel(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getOptionName(), null);
    }

    /**
     * 反射方式修改值
     *
     * @param fieldStr 字段吗
     * @param object   对象
     * @return 修改结果
     * @author wangmin1994@qq.com
     * @since 2019-05-10 14:04:48
     */
    public ResultModel set(String fieldStr, Object object) {
        Field field;
        try {
            field = this.getClass().getDeclaredField(fieldStr);
        } catch (NoSuchFieldException | SecurityException e) {
            log.warn("ResultModel get field faild!", e);
            return this;
        }

        field.setAccessible(true);
        try {
            field.set(this, object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.warn("ResultModel set field failed!", e);
            return this;
        }
        return this;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResultModel;
    }
}
