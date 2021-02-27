package chenyunlong.zhangli.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Stan
 */
public class MyException extends MyAbstractException {

    private String msg;
    private int code;

    public MyException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
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
