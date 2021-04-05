package chenyunlong.zhangli.common.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends AbstractException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }
}
