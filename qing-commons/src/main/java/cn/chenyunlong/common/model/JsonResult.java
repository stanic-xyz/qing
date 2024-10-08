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

import cn.chenyunlong.common.constants.BaseEnum;
import cn.chenyunlong.common.constants.CodeEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * 公共返回结构
 *
 * @author gim
 * @since 2022/11/11
 */
@Data
public final class JsonResult<T> {

    @Setter(AccessLevel.PRIVATE)
    private Integer code;

    @Setter(AccessLevel.PRIVATE)
    private String message;

    @Setter(AccessLevel.PRIVATE)
    private T result;

    private String devMessage;

    private JsonResult() {
    }

    /**
     * 成功
     */
    public static <E> JsonResult<E> success() {
        return success(null);
    }

    /**
     * 成功
     */
    public static <E> JsonResult<E> success(E result, String msg) {
        JsonResult<E> jsonResult = success(result);
        jsonResult.setMessage(msg);
        return jsonResult;
    }

    /**
     * 成功
     */
    public static <E> JsonResult<E> success(E result) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.Success.getValue());
        jsonResult.setMessage(CodeEnum.Success.getName());
        jsonResult.setResult(result);
        return jsonResult;
    }

    /**
     * 成功
     */
    public static <T, E> JsonResult<T> fail(BaseEnum<Integer> codeEnum) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setMessage(codeEnum.getName());
        jsonResult.setCode(codeEnum.getValue());
        return jsonResult;
    }

    /**
     * 成功
     */
    public static <T> JsonResult<T> fail(String msg) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setMessage(msg);
        jsonResult.setCode(CodeEnum.Fail.getValue());
        return jsonResult;
    }

    /**
     * 成功
     */
    public static <E> JsonResult<E> fail(E result, String msg) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.Fail.getValue());
        jsonResult.setMessage(msg);
        jsonResult.setResult(result);
        return jsonResult;
    }


    /**
     * 成功
     */
    public static <E> JsonResult<E> forbidden(String msg) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.Fail.getValue());
        jsonResult.setMessage(msg);
        jsonResult.setResult(null);
        jsonResult.setCode(HttpStatus.FORBIDDEN.value());
        return jsonResult;
    }

    /**
     * 成功
     */
    public static <E> JsonResult<E> notFound() {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.NotFoundError.getValue());
        jsonResult.setMessage("资源不存在");
        jsonResult.setResult(null);
        jsonResult.setCode(HttpStatus.NOT_FOUND.value());
        return jsonResult;
    }

    public static <E> JsonResult<E> unAuthorized(String message) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.Fail.getValue());
        jsonResult.setMessage(message);
        jsonResult.setResult(null);
        jsonResult.setCode(HttpStatus.UNAUTHORIZED.value());
        return jsonResult;
    }

    /**
     * 成功
     */
    public static <E> JsonResult<E> res(BaseEnum<Integer> codeEnum, E result) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setMessage(codeEnum.getName());
        jsonResult.setCode(codeEnum.getValue());
        jsonResult.setResult(result);
        return jsonResult;
    }

    public JsonResult<T> setDevMessage(String devMessage) {
        this.devMessage = devMessage;
        return this;
    }


    public boolean isSuccess() {
        return Objects.equals(CodeEnum.Success.getValue(), this.getCode());
    }

}
