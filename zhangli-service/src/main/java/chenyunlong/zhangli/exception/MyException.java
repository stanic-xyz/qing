package chenyunlong.zhangli.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MyException extends Exception {

    private String msg;
    private int code;

    public MyException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
}
