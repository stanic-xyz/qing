package chenyunlong.zhangli.model.response;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    private int code;
    private String Msg;
    private Object data;

    public BaseResponse(int code, String msg, Object data) {
        this.code = code;
        Msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static BaseResponse success(Object data) {
        return new BaseResponse(200, "success", data);
    }

    public static BaseResponse faild(String msg) {
        return new BaseResponse(400, msg, null);
    }
}
