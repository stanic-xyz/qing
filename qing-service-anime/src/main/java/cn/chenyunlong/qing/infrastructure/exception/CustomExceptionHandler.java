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

package cn.chenyunlong.qing.infrastructure.exception;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.infrastructure.enums.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Stan
 */
@SuppressWarnings("rawtypes")
@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(value = AbstractException.class)
    public ResponseEntity<JsonResult> errorHandler(Exception exception) {
        JsonResult<Exception> jsonResult = JsonResult.res(ResponseCode.PARAM_FAIL, exception);
        return ResponseEntity.badRequest().body(jsonResult);
    }

    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<JsonResult> messageConversionException(Exception exception) {
        JsonResult<Object> jsonResult = JsonResult.fail("参数异常，请检查参数格式").setDevMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(jsonResult);
    }

    /**
     * 参数校验异常
     *
     * @param exception 异常信息
     * @return 公共返回值
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<JsonResult> illegalArgumentException(IllegalArgumentException exception) {
        JsonResult jsonResult;
        jsonResult = JsonResult.fail("参数校验错误：" + exception.getMessage()).setDevMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(jsonResult);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<JsonResult> messageConversionException(BusinessException exception) {
        JsonResult jsonResult = JsonResult.fail(exception.getMessage()).setDevMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(jsonResult);
    }


    /**
     * 运行时异常处理程序
     * 捕获  RuntimeException 异常
     *
     * @param exception 异常
     * @param response  response
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<JsonResult> runtimeExceptionHandler(final Exception exception, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        JsonResult<Long> longJsonResult = JsonResult.fail("参数校验错误：" + exception.getMessage());
        return ResponseEntity.internalServerError().body(longJsonResult);
    }
}
