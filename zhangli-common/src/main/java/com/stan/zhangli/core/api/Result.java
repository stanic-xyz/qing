package com.stan.zhangli.core.api;

import com.stan.zhangli.core.exception.BaseException;
import com.stan.zhangli.core.exception.ErrorType;
import com.stan.zhangli.core.exception.SystemErrorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * @author Stan
 */
@Data
@ApiModel(description = "rest请求的返回模型，所有rest正常都返回该类的对象")
public class Result<T> {

    public static final ErrorType SUCCESS = SystemErrorType.SUCCESS;

    @ApiModelProperty(value = "处理结果code", required = true)
    private String code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String msg;
    @ApiModelProperty(value = "请求结果生成时间戳")
    private final Instant time;
    @ApiModelProperty(value = "处理结果数据信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * @param errorType 错误类型
     */
    public Result(ErrorType errorType) {
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType 错误类型
     * @param data      处理结果数据信息
     */
    public Result(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param errorType 处理结果
     * @param msg       消息
     * @param data      数据
     */
    private Result(ErrorType errorType, String msg, T data) {
        this(errorType);
        this.msg = msg;
        this.data = data;
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data 处理结果数据信息
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SystemErrorType.SUCCESS, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static Result<String> fail() {
        return new Result<>(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException 错误信息
     * @return Result
     */
    public static Result<String> fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data 处理结果数据信息
     * @return Result
     */
    public static <T> Result<T> fail(BaseException baseException, T data) {
        return new Result<>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @param data      处理结果数据信息
     * @return Result
     */
    public static <T> Result<T> fail(ErrorType errorType, T data) {
        return new Result<>(errorType, data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType 错误类型
     * @return Result
     */
    public static <T> Result<T> fail(ErrorType errorType) {
        return Result.fail(errorType, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data 处理结果数据信息
     * @return Result
     */
    public static <T> Result<T> fail(T data) {
        return new Result<>(SystemErrorType.SYSTEM_ERROR, data);
    }

    public static Result<String> unauthorized(String message) {
        return new Result<>(SystemErrorType.UNAUTHORIZED, message, null);
    }

    public static Result<String> forbidden() {
        return new Result<>(SystemErrorType.FORBIDDEN, null);
    }

    public static Result<String> validateFailed(String message) {
        return new Result<>(SystemErrorType.VALIDATE_FAILED, message, null);
    }


    /**
     * 成功code=0
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS.getCode().equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
