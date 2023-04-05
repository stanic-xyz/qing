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

import java.util.Objects;

/**
 * 公共返回结构
 *
 * @author gim
 * @date 2022/11/11
 */
@Data
public final class JsonResult<T> {

    @Setter(AccessLevel.PRIVATE)
    private Integer code;

    @Setter(AccessLevel.PRIVATE)
    private String msg;

    @Setter(AccessLevel.PRIVATE)
    private T result;

    private String devMessage;

    private JsonResult() {
    }

    public static <E> JsonResult<E> success(E result) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.Success.getValue());
        jsonResult.setMsg(CodeEnum.Success.getName());
        jsonResult.setResult(result);
        return jsonResult;
    }

    public static <E> JsonResult<E> success(E result, String msg) {
        JsonResult<E> jsonResult = success(result);
        jsonResult.setMsg(msg);
        return jsonResult;
    }

    public static <T> JsonResult<T> fail(BaseEnum codeEnum) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setMsg(codeEnum.getName());
        jsonResult.setCode(codeEnum.getValue());
        return jsonResult;
    }

    public static <T> JsonResult<T> fail(String msg) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.setMsg(msg);
        jsonResult.setCode(CodeEnum.Fail.getValue());
        return jsonResult;
    }

    public static <E> JsonResult<E> fail(E result, String msg) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setCode(CodeEnum.Fail.getValue());
        jsonResult.setMsg(msg);
        jsonResult.setResult(result);
        return jsonResult;
    }

    public static <E> JsonResult<E> res(BaseEnum codeEnum, E result) {
        JsonResult<E> jsonResult = new JsonResult<>();
        jsonResult.setMsg(codeEnum.getName());
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