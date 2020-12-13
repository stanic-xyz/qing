package chenyunlong.zhangli.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
@ApiModel(description = "返回数据值")
public class ApiResult<T> implements Serializable {
    @ApiModelProperty(value = "返回值编码；0表示成功，其他全部失败")
    private int code;
    @ApiModelProperty(value = "异常提示信息")
    private String msg;
    @ApiModelProperty(value = "具体返回值信息,如果为一般表示请求成功")
    private T data;

    public ApiResult() {

    }

    public ApiResult(int code, String msg, T data) {
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

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(0, "success", null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(0, "success", data);
    }

    public static <T> ApiResult<T> faild(String msg) {
        return new ApiResult<>(1, msg, null);
    }
}
