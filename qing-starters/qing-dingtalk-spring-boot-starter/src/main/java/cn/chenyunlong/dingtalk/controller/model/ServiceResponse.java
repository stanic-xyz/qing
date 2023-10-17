package cn.chenyunlong.dingtalk.controller.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务相应数据模型。
 *
 * @author stanic
 * @since 2022/10/2022/10/24
 */
@Getter
@Setter
public class ServiceResponse<T> {
    private boolean success;
    private Integer code;
    private String message;
    private T data;

    /**
     * 成功响应。
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 实际响应。
     */
    public static <T> ServiceResponse<T> buildSuccessServiceResponse(T data) {
        ServiceResponse<T> serviceResponse = new ServiceResponse<>();
        serviceResponse.setData(data);
        serviceResponse.setSuccess(true);
        serviceResponse.setCode(200);
        serviceResponse.setMessage("ok");
        return serviceResponse;
    }
}
