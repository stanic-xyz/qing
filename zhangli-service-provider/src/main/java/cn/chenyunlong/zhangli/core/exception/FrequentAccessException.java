package cn.chenyunlong.zhangli.core.exception;

import org.springframework.http.HttpStatus;

public class FrequentAccessException extends AbstractException {
    public FrequentAccessException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}