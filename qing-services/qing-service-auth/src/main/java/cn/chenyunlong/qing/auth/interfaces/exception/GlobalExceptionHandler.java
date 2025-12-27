/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.interfaces.exception;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * <p>统一处理系统中的各种异常，提供标准化的错误响应格式</p>
 *
 * <p>处理的异常类型包括：</p>
 * <ul>
 *   <li>业务异常：BusinessException、NotFoundException等</li>
 *   <li>认证异常：AuthenticationException、BadCredentialsException等</li>
 *   <li>参数校验异常：MethodArgumentNotValidException、ConstraintViolationException等</li>
 *   <li>系统异常：RuntimeException、Exception等</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param ex      业务异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("业务异常: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Business Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .code(ex.getCode() != null ? ex.getCode().toString() : "BUSINESS_ERROR")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理资源未找到异常
     *
     * @param ex      未找到异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        log.warn("资源未找到: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .code("NOT_FOUND")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 处理认证异常
     *
     * @param ex      认证异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        log.warn("认证异常: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .code("AUTHENTICATION_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    //    /**
    //     * 处理Spring Security认证异常
    //     *
    //     * @param ex Spring认证异常
    //     * @param request HTTP请求
    //     * @return 错误响应
    //     */
    //    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    //    public ResponseEntity<ErrorResponse> handleSpringAuthenticationException(org.springframework.security.core.AuthenticationException ex, HttpServletRequest request) {
    //        log.warn("Spring认证异常: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI());
    //
    //        String message = "认证失败";
    //        String code = "AUTHENTICATION_FAILED";
    //
    //        if (ex instanceof BadCredentialsException) {
    //            message = "用户名或密码错误";
    //            code = "BAD_CREDENTIALS";
    //        }
    //
    //        ErrorResponse errorResponse = ErrorResponse.builder()
    //                .timestamp(LocalDateTime.now())
    //                .status(HttpStatus.UNAUTHORIZED.value())
    //                .error("Authentication Failed")
    //                .message(message)
    //                .path(request.getRequestURI())
    //                .code(code)
    //                .build();
    //
    //        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    //    }
    //
    //    /**
    //     * 处理访问拒绝异常
    //     *
    //     * @param ex 访问拒绝异常
    //     * @param request HTTP请求
    //     * @return 错误响应
    //     */
    //    @ExceptionHandler(AccessDeniedException.class)
    //    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
    //        log.warn("访问拒绝: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI());
    //
    //        ErrorResponse errorResponse = ErrorResponse.builder()
    //                .timestamp(LocalDateTime.now())
    //                .status(HttpStatus.FORBIDDEN.value())
    //                .error("Access Denied")
    //                .message("您没有权限访问此资源")
    //                .path(request.getRequestURI())
    //                .code("ACCESS_DENIED")
    //                .build();
    //
    //        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    //    }

    /**
     * 处理参数校验异常
     *
     * @param ex      参数校验异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("参数校验失败, 请求路径: {}", request.getRequestURI());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        String message = "参数校验失败: " + String.join(", ", errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(message)
                .path(request.getRequestURI())
                .code("VALIDATION_FAILED")
                .details(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理绑定异常
     *
     * @param ex      绑定异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex, HttpServletRequest request) {
        log.warn("参数绑定失败, 请求路径: {}", request.getRequestURI());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        String message = "参数绑定失败: " + String.join(", ", errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bind Failed")
                .message(message)
                .path(request.getRequestURI())
                .code("BIND_FAILED")
                .details(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理约束违反异常
     *
     * @param ex      约束违反异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("约束违反, 请求路径: {}", request.getRequestURI());

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<String> errors = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        String message = "约束违反: " + String.join(", ", errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Constraint Violation")
                .message(message)
                .path(request.getRequestURI())
                .code("CONSTRAINT_VIOLATION")
                .details(errors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理缺少请求参数异常
     *
     * @param ex      缺少请求参数异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("缺少请求参数: {}, 请求路径: {}", ex.getParameterName(), request.getRequestURI());

        String message = String.format("缺少必需的请求参数: %s", ex.getParameterName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Missing Parameter")
                .message(message)
                .path(request.getRequestURI())
                .code("MISSING_PARAMETER")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理方法参数类型不匹配异常
     *
     * @param ex      方法参数类型不匹配异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("参数类型不匹配: {}, 请求路径: {}", ex.getName(), request.getRequestURI());

        String message = String.format("参数 %s 的值 %s 类型不正确，期望类型: %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Type Mismatch")
                .message(message)
                .path(request.getRequestURI())
                .code("TYPE_MISMATCH")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理HTTP请求方法不支持异常
     *
     * @param ex      HTTP请求方法不支持异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("HTTP方法不支持: {}, 请求路径: {}", ex.getMethod(), request.getRequestURI());

        String message = String.format("请求方法 %s 不被支持，支持的方法: %s",
                ex.getMethod(), String.join(", ", ex.getSupportedMethods()));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error("Method Not Allowed")
                .message(message)
                .path(request.getRequestURI())
                .code("METHOD_NOT_ALLOWED")
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * 处理404异常
     *
     * @param ex      404异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        log.warn("请求路径未找到: {}", request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message("请求的资源不存在")
                .path(request.getRequestURI())
                .code("RESOURCE_NOT_FOUND")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 处理非法参数异常
     *
     * @param ex      非法参数异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("非法参数: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Illegal Argument")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .code("ILLEGAL_ARGUMENT")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 处理非法状态异常
     *
     * @param ex      非法状态异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        log.warn("非法状态: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Illegal State")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .code("ILLEGAL_STATE")
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * 处理运行时异常
     *
     * @param ex      运行时异常
     * @param request HTTP请求
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        log.error("运行时异常: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Runtime Error")
                .message("系统运行时错误，请联系管理员")
                .path(request.getRequestURI())
                .code("RUNTIME_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 处理通用异常
     *
     * @param ex      通用异常
     * @param request HTTP 请求
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("系统异常: {}, 请求路径: {}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("系统内部错误，请联系管理员")
                .path(request.getRequestURI())
                .code("INTERNAL_SERVER_ERROR")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 格式化字段错误信息
     *
     * @param fieldError 字段错误
     * @return 格式化后的错误信息
     */
    private String formatFieldError(FieldError fieldError) {
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        Object rejectedValue = fieldError.getRejectedValue();

        if (rejectedValue != null && StrUtil.isNotBlank(rejectedValue.toString())) {
            return String.format("%s='%s' %s", field, rejectedValue, message);
        } else {
            return String.format("%s %s", field, message);
        }
    }
}
