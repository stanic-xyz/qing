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
public final class JsonObject<E> {

    @Setter(AccessLevel.PRIVATE)
    private Integer code;
    @Setter(AccessLevel.PRIVATE)
    private String msg;
    @Setter(AccessLevel.PRIVATE)
    private E result;

    private String devMessage;

    private JsonObject() {
    }

    public JsonObject setDevMessage(String devMessage) {
        this.devMessage = devMessage;
        return this;
    }

    public static <E> JsonObject<E> success(E e) {
        JsonObject<E> jsonObject = new JsonObject<>();
        jsonObject.setCode(CodeEnum.Success.getValue());
        jsonObject.setMsg(CodeEnum.Success.getName());
        jsonObject.setResult(e);
        return jsonObject;
    }

    public static <E> JsonObject<E> success(E t, String msg) {
        JsonObject<E> obj = success(t);
        obj.setMsg(msg);
        return obj;
    }

    public static <T> JsonObject<T> fail(BaseEnum codeEnum) {
        JsonObject<T> object = new JsonObject<T>();
        object.setMsg(codeEnum.getName());
        object.setCode(codeEnum.getValue());
        return object;
    }

    public static <T> JsonObject<T> fail(String msg) {
        JsonObject<T> jsonObject = new JsonObject<>();
        jsonObject.setMsg(msg);
        jsonObject.setCode(CodeEnum.Fail.getValue());
        return jsonObject;
    }

    public static <E> JsonObject<E> fail(E result, String msg) {
        JsonObject<E> object = new JsonObject<>();
        object.setCode(CodeEnum.Fail.getValue());
        object.setMsg(msg);
        object.setResult(result);
        return object;
    }

    public static <E> JsonObject<E> res(BaseEnum codeEnum, E e) {
        JsonObject<E> object = new JsonObject<>();
        object.setMsg(codeEnum.getName());
        object.setCode(codeEnum.getValue());
        object.setResult(e);
        return object;
    }


    public boolean isSuccess() {
        return Objects.equals(CodeEnum.Success.getValue(), this.getCode());
    }

}
