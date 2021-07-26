package com.stan.zhangli.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Email exception.
 *
 * @author johnniang
 * @date 19-4-23
 */
public class EmailException extends AbstractException {

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
