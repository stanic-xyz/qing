package cn.chenyunlong.qing.infrustructure.exception;

import cn.chenyunlong.common.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    JsonResult<Void> handleArgumentException(IllegalArgumentException exception) {
        String message = exception.getMessage();
        return JsonResult.fail(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    JsonResult<Void> handleException(Exception exception) {
        log.error("未知异常", exception);
        return JsonResult.fail("未知异常!");
    }
}
