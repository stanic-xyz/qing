package chenyunlong.zhangli.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    private int code;
    private String Msg;
    private Object data;


    public static BaseResponse success(Object data) {
        return new BaseResponse(200, "success", data);
    }

    public static BaseResponse faild(String msg) {
        return new BaseResponse(400, msg, null);
    }
}
