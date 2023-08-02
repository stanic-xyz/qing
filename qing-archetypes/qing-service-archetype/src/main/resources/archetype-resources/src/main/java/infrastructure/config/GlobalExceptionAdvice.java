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

import com.only4play.common.constants.CodeEnum;
import com.only4play.common.exception.BusinessException;
import com.only4play.common.exception.SystemException;
import com.only4play.common.model.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author gim
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public JsonObject handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return JsonObject.res(e.getMsg(), e.getData());
    }

    @ExceptionHandler(SystemException.class)
    public JsonObject handleSystemException(SystemException e) {
        log.error("系统异常", e);
        return JsonObject.fail(CodeEnum.SystemError);
    }

    @ExceptionHandler(Exception.class)
    public JsonObject handleException(Exception e) {
        log.error("系统异常", e);
        return JsonObject.fail(CodeEnum.SystemError);
    }


}
