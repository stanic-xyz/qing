package chenyunlong.zhangli.common.exception;

public class LoginErrorException extends MyException {
    public LoginErrorException(String msg, int code) {
        super(msg, code);
    }

    public LoginErrorException(String msg) {
        super(msg, 1);
    }
}
