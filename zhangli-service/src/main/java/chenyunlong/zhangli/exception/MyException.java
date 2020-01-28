package chenyunlong.zhangli.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MyException extends Exception {

    private String msg;
    private Long code;

    public MyException(String msg, Long code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
}
