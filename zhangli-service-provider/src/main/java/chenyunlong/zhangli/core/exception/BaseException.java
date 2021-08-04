package chenyunlong.zhangli.core.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统异常基类
 *
 * @author Stan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    /**
     * 异常对应的错误类型
     */
    private ErrorType errorType;
    private String errorData;

    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public BaseException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public BaseException(String module, String code, Object[] args, String defaultMessage) {

    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    public String getErrorData() {
        return errorData;
    }
}
