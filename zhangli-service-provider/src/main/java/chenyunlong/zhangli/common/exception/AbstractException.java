package chenyunlong.zhangli.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Stan
 */
public abstract class AbstractException extends MyAbstractException {

    private String msg;
    private int code;

    public AbstractException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable cause) {
        super(message, cause);
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Http status code
     *
     * @return {@link HttpStatus}
     */
    @Override
    public HttpStatus getStatus() {
        return null;
    }
}
