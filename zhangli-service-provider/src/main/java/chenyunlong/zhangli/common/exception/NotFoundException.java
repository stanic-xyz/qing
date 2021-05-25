package chenyunlong.zhangli.common.exception;

import chenyunlong.zhangli.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractException {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
