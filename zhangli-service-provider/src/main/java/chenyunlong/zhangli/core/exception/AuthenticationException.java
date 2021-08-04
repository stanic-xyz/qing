package chenyunlong.zhangli.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Authentication exception.
 *
 * @author Stan
 */
public class AuthenticationException extends AbstractException {

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
