package chenyunlong.zhangli.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author 陈云龙
 * @date 2021/02/27
 **/
public abstract class MyAbstractException extends RuntimeException {

    /**
     * Error errorData.
     */
    private Object errorData;

    public MyAbstractException(String message) {
        super(message);
    }

    public MyAbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Http status code
     *
     * @return {@link HttpStatus}
     */
    @NonNull
    public abstract HttpStatus getStatus();

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    /**
     * Sets error errorData.
     *
     * @param errorData error data
     * @return current exception.
     */
    @NonNull
    public MyAbstractException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}