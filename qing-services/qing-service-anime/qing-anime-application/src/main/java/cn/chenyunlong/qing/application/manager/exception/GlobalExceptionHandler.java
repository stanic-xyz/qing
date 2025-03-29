package cn.chenyunlong.qing.application.manager.exception;

import cn.chenyunlong.common.exception.AbstractException;
import cn.chenyunlong.common.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    JsonResult<Void> handleArgumentException(IllegalArgumentException exception) {
        String message = exception.getMessage();
        return JsonResult.fail(message);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    JsonResult<Void> handleIllegalStateException(BindException exception) {
        String message = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        return JsonResult.fail(message);
    }

    @ResponseBody
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    JsonResult<Void> handleException() {
        return JsonResult.notFound();
    }

    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    JsonResult<Void> handleException(AbstractException exception) {
        return JsonResult.fail("业务异常：%s!".formatted(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    JsonResult<Void> handleException(Exception exception) {
        log.error("未知异常", exception);
        return JsonResult.notFound();
    }
}
