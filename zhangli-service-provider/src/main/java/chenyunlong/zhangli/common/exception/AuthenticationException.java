package chenyunlong.zhangli.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Authentication exception.
 *
 * @author Stan
 */
public class AuthenticationException extends MyException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
