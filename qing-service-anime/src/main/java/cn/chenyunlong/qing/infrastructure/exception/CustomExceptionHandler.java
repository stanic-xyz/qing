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

package cn.chenyunlong.qing.infrastructure.exception;

import cn.chenyunlong.common.model.JsonObject;
import cn.chenyunlong.qing.infrastructure.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stan
 */
@SuppressWarnings("rawtypes")
@ControllerAdvice
public class CustomExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = AbstractException.class)
    public JsonObject errorHandler(Exception exception) {
        return JsonObject.res(ResponseCode.PARAM_FAIL, exception);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public JsonObject messageConversionException(Exception exception) {
        return JsonObject.fail("参数异常，请检查参数格式").setDevMessage(exception.getMessage());
    }


    /**
     * 运行时异常处理程序
     * 捕获  RuntimeException 异常
     *
     * @param request   request
     * @param exception 异常
     * @param response  response
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonObject<Long> runtimeExceptionHandler(HttpServletRequest request, final Exception exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return JsonObject.fail("消息错误了" + exception.getMessage());
    }
}
