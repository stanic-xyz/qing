package cn.chenyunlong.qing.anime.interfaces.rest.exception;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author 陈云龙
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理 @Valid 校验失败异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult<String>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("参数校验失败: {}", ex.getMessage());
        
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        return ResponseEntity.badRequest()
                .body(JsonResult.fail("参数校验失败: " + errorMessage));
    }

    /**
     * 处理 @Validated 校验失败异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<JsonResult<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("参数约束校验失败: {}", ex.getMessage());
        
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        return ResponseEntity.badRequest()
                .body(JsonResult.fail("参数约束校验失败: " + errorMessage));
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<JsonResult<String>> handleBindException(BindException ex) {
        log.warn("参数绑定失败: {}", ex.getMessage());
        
        String errorMessage = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        return ResponseEntity.badRequest()
                .body(JsonResult.fail("参数绑定失败: " + errorMessage));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<JsonResult<String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("参数类型不匹配: {}", ex.getMessage());
        
        String errorMessage = String.format("参数 '%s' 类型不正确，期望类型: %s", 
                ex.getName(), ex.getRequiredType().getSimpleName());
        
        return ResponseEntity.badRequest()
                .body(JsonResult.fail(errorMessage));
    }

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<JsonResult<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("业务参数异常: {}", ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(JsonResult.fail(ex.getMessage()));
    }

    /**
     * 处理业务状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<JsonResult<String>> handleIllegalStateException(IllegalStateException ex) {
        log.warn("业务状态异常: {}", ex.getMessage());
        
        return ResponseEntity.badRequest()
                .body(JsonResult.fail(ex.getMessage()));
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<JsonResult<String>> handleNullPointerException(NullPointerException ex) {
        log.error("空指针异常: ", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(JsonResult.fail("系统内部错误"));
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResult<String>> handleGenericException(Exception ex) {
        log.error("未处理的异常: ", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(JsonResult.fail("系统内部错误，请联系管理员"));
    }
}
