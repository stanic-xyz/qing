package chenyunlong.zhangli.common.exception;

import chenyunlong.zhangli.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class LoginErrorException extends AbstractException {
    public LoginErrorException(String msg, int code) {
        super(msg);
    }

    public LoginErrorException(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }
}
