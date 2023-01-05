package cn.chenyunlong.qing.infrastructure.exception;/*
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


import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.SystemException;
import cn.chenyunlong.common.model.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author gim
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    /**
     * 处理业务异常
     *
     * @param exception 业务异常
     * @return 返回空的信息
     */
    @ExceptionHandler(cn.chenyunlong.common.exception.BusinessException.class)
    public JsonObject<Void> handleBusinessException(cn.chenyunlong.common.exception.BusinessException exception) {
        return JsonObject.res(CodeEnum.NotFindError, null);
    }

    @ExceptionHandler(SystemException.class)
    public JsonObject<Void> handleSystemException(SystemException exception) {
        log.error("系统异常", exception);
        return JsonObject.fail(CodeEnum.SystemError);
    }

    @ExceptionHandler(Exception.class)
    public JsonObject<Void> handleException(Exception exception) {
        log.error("系统异常", exception);
        return JsonObject.fail(CodeEnum.SystemError);
    }


}
