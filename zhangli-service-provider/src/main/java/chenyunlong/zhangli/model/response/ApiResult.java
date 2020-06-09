package chenyunlong.zhangli.model.response;

import java.io.Serializable;

public class ApiResult implements Serializable {

    private int code;
    private String Msg;
    private Object data;

    public ApiResult(int code, String msg, Object data) {
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

    public static ApiResult success(Object data) {
        return new ApiResult(200, "success", data);
    }

    public static ApiResult faild(String msg) {
        return new ApiResult(400, msg, null);
    }
}
