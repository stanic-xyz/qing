package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.BadRequestException;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.exception.ConflictException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 统一拦截 REST 异常并输出稳定的 Result JSON 结构。
 */
@Slf4j
@RestControllerAdvice(basePackages = "cn.chenyunlong.qing.service.llm.controler")
public class GlobalRestExceptionHandler {

    private static final int BUSINESS_ERROR_CODE = 400;
    private static final int CONFLICT_ERROR_CODE = 409;
    private static final int VALIDATION_ERROR_CODE = 400;
    private static final int NOT_FOUND_ERROR_CODE = 404;
    private static final int UNKNOWN_ERROR_CODE = 500;
    private static final String UNKNOWN_ERROR_MESSAGE = "系统繁忙，请稍后重试";
    private static final String REQUEST_BODY_ERROR_MESSAGE = "请求体格式错误或缺少必要字段";

    /**
     * 处理业务异常，优先保留业务消息与显式业务码。
     *
     * @param ex 业务异常
     * @return 统一错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex) {
        log.warn("捕获业务异常: {}", ex.getMessage(), ex);
        Integer code = ex.getCode() != null ? ex.getCode().getValue() : BUSINESS_ERROR_CODE;
        return Result.error(code, defaultMessage(ex.getMessage(), "业务处理失败"));
    }

    /**
     * 处理显式抛出的参数错误异常。
     *
     * @param ex 参数错误异常
     * @return 统一错误响应
     */
    @ExceptionHandler(BadRequestException.class)
    public Result<Void> handleBadRequestException(BadRequestException ex) {
        log.warn("请求参数不合法: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, defaultMessage(ex.getMessage(), "请求参数不合法"));
    }

    /**
     * 处理显式抛出的业务冲突异常。
     *
     * @param ex 业务冲突异常
     * @return 统一错误响应
     */
    @ExceptionHandler(ConflictException.class)
    public Result<Void> handleConflictException(ConflictException ex) {
        log.warn("业务冲突: {}", ex.getMessage());
        return Result.error(CONFLICT_ERROR_CODE, defaultMessage(ex.getMessage(), "当前操作与资源状态冲突"));
    }

    /**
     * 处理请求参数不合法或业务前置校验失败。
     *
     * @param ex 非法参数异常
     * @return 统一错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("请求参数不合法: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, defaultMessage(ex.getMessage(), "请求参数不合法"));
    }

    /**
     * 处理业务状态不满足当前操作要求的场景。
     *
     * @param ex 非法状态异常
     * @return 统一错误响应
     */
    @ExceptionHandler(IllegalStateException.class)
    public Result<Void> handleIllegalStateException(IllegalStateException ex) {
        log.warn("请求状态不合法: {}", ex.getMessage());
        return Result.error(BUSINESS_ERROR_CODE, defaultMessage(ex.getMessage(), "当前状态不允许执行该操作"));
    }

    /**
     * 处理资源不存在场景。
     *
     * @param ex 资源不存在异常
     * @return 统一错误响应
     */
    @ExceptionHandler(NoSuchElementException.class)
    public Result<Void> handleNoSuchElementException(NoSuchElementException ex) {
        log.warn("请求资源不存在: {}", ex.getMessage());
        return Result.error(NOT_FOUND_ERROR_CODE, defaultMessage(ex.getMessage(), "请求资源不存在"));
    }

    /**
     * 处理显式抛出的资源不存在异常。
     *
     * @param ex 资源不存在异常
     * @return 统一错误响应
     */
    @ExceptionHandler(NotFoundException.class)
    public Result<Void> handleNotFoundException(NotFoundException ex) {
        log.warn("请求资源不存在: {}", ex.getMessage());
        return Result.error(NOT_FOUND_ERROR_CODE, defaultMessage(ex.getMessage(), "请求资源不存在"));
    }

    /**
     * 处理请求体参数校验失败。
     *
     * @param ex 请求体校验异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("请求体参数校验失败: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, extractBindingMessage(ex.getBindingResult(), "请求参数校验失败"));
    }

    /**
     * 处理参数绑定失败。
     *
     * @param ex 参数绑定异常
     * @return 统一错误响应
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        log.warn("请求参数绑定失败: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, extractBindingMessage(ex.getBindingResult(), "请求参数绑定失败"));
    }

    /**
     * 处理约束校验失败。
     *
     * @param ex 约束异常
     * @return 统一错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("请求参数约束校验失败: {}", ex.getMessage());
        String message = ex.getConstraintViolations().stream()
                .map(this::formatConstraintViolation)
                .filter(Objects::nonNull)
                .filter(text -> !text.isBlank())
                .collect(Collectors.joining("; "));
        return Result.error(VALIDATION_ERROR_CODE, defaultMessage(message, "请求参数校验失败"));
    }

    /**
     * 处理缺少必填请求参数。
     *
     * @param ex 参数缺失异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("缺少请求参数: {}", ex.getParameterName());
        return Result.error(VALIDATION_ERROR_CODE, "缺少必填参数: " + ex.getParameterName());
    }

    /**
     * 处理参数类型不匹配。
     *
     * @param ex 参数类型异常
     * @return 统一错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("请求参数类型不匹配: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, "参数类型不正确: " + ex.getName());
    }

    /**
     * 处理请求参数绑定类异常。
     *
     * @param ex 请求绑定异常
     * @return 统一错误响应
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    public Result<Void> handleServletRequestBindingException(ServletRequestBindingException ex) {
        log.warn("请求参数绑定异常: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, defaultMessage(ex.getMessage(), "请求参数绑定失败"));
    }

    /**
     * 处理请求体不可读或 JSON 结构错误。
     *
     * @param ex 请求体解析异常
     * @return 统一错误响应
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return Result.error(VALIDATION_ERROR_CODE, REQUEST_BODY_ERROR_MESSAGE);
    }

    /**
     * 处理其他未识别异常，避免把底层细节直接返回给前端。
     *
     * @param ex 未知异常
     * @return 统一错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex) {
        log.error("捕获未处理异常", ex);
        return Result.error(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
    }

    /**
     * 生成绑定错误的可读提示信息。
     *
     * @param bindingResult 绑定结果
     * @param fallbackMessage 兜底文案
     * @return 可读错误消息
     */
    private String extractBindingMessage(BindingResult bindingResult, String fallbackMessage) {
        List<String> messages = bindingResult.getFieldErrors().stream()
                .map(this::formatFieldError)
                .filter(Objects::nonNull)
                .filter(message -> !message.isBlank())
                .distinct()
                .toList();

        if (!messages.isEmpty()) {
            return String.join("; ", messages);
        }

        if (bindingResult.getGlobalError() != null && bindingResult.getGlobalError().getDefaultMessage() != null) {
            return bindingResult.getGlobalError().getDefaultMessage();
        }

        return fallbackMessage;
    }

    /**
     * 生成字段级校验信息。
     *
     * @param fieldError 字段错误
     * @return 字段错误描述
     */
    private String formatFieldError(FieldError fieldError) {
        if (fieldError == null) {
            return null;
        }
        return fieldError.getField() + ": " + defaultMessage(fieldError.getDefaultMessage(), "参数不合法");
    }

    /**
     * 生成约束校验提示信息。
     *
     * @param violation 约束违反项
     * @return 约束错误描述
     */
    private String formatConstraintViolation(ConstraintViolation<?> violation) {
        if (violation == null) {
            return null;
        }
        String path = violation.getPropertyPath() != null ? violation.getPropertyPath().toString() : "";
        if (!path.isBlank()) {
            return path + ": " + defaultMessage(violation.getMessage(), "参数不合法");
        }
        return defaultMessage(violation.getMessage(), "参数不合法");
    }

    /**
     * 为空消息补齐兜底文案。
     *
     * @param message 原始消息
     * @param fallbackMessage 兜底文案
     * @return 安全消息
     */
    private String defaultMessage(String message, String fallbackMessage) {
        if (message == null || message.isBlank()) {
            return fallbackMessage;
        }
        return message;
    }
}
