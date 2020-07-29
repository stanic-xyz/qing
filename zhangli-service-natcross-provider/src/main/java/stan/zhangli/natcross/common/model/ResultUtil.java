package stan.zhangli.natcross.common.model;


import stan.zhangli.natcross.common.model.response.ApiResult;

public class ResultUtil {
    public static ApiResult success(Object data) {
        return new ApiResult(0, "success", data);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(-1, message, null);
    }
}