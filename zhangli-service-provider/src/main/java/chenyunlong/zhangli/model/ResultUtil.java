package chenyunlong.zhangli.model;


import chenyunlong.zhangli.model.response.BaseResponse;

public class ResultUtil {
    public static BaseResponse success(Object data) {
        return new BaseResponse(0, "success", data);
    }

    public static BaseResponse fail(String message) {
        return new BaseResponse(-1, message, null);
    }
}
