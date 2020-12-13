package chenyunlong.zhangli.model;


import chenyunlong.zhangli.model.vo.ApiResult;

public class ResultUtil {


    public static ApiResult<Object> success() {
        return new ApiResult(0, "success", null);
    }

    public static ApiResult<Object> success(Object data) {
        return new ApiResult(0, "success", data);
    }

    public static ApiResult<Object> fail(String message) {
        return new ApiResult(-1, message, null);
    }
}
