package chenyunlong.zhangli.model.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 全局返回数据类型
 *
 * @author Stan
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "返回数据值")
public class ApiResult<T> {
    @ApiModelProperty(value = "返回值编码；0表示成功，其他全部失败")
    private int code;
    @ApiModelProperty(value = "异常提示信息")
    private String msg;
    @ApiModelProperty(value = "具体返回值信息,如果为一般表示请求成功")
    private T data;

    /**
     * Creates an ok result. (Default status is 200)
     *
     * @return ok result with message and data
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(0, "success", null);
    }

    /**
     * Creates an ok result with data only. (Default message is OK, status is 200)
     *
     * @param data data to response
     * @param <T>  data type
     * @return base response with data
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(0, "success", data);
    }

    /**
     * Creates an error result. (Default message is OK, status is 400)
     *
     * @param <T> data type
     * @return base response with data
     */
    public static <T> ApiResult<T> fail(String msg) {
        return new ApiResult<>(1, msg, null);
    }
}
