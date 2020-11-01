package chenyunlong.zhangli.model.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class ApiResult implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public ApiResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ApiResult success(Object data) {
        return new ApiResult(200, "success", data);
    }

    public static ApiResult faild(String msg) {
        return new ApiResult(400, msg, null);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ApiResult;
    }

}
