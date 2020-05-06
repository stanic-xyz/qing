package chenyunlong.zhangli.exception;

public class MyException extends Exception {

    private String msg;
    private int code;

    public MyException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
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
}
