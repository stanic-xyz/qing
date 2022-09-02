package cn.chenyunlong.zhangli.controller.base;

import cn.chenyunlong.zhangli.core.ApiResult;

public class ApiController {

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data 处理结果数据信息
     * @return Result
     */
    public static <T> ApiResult<T> success(T data) {
        return ApiResult.success(data);
    }

}
