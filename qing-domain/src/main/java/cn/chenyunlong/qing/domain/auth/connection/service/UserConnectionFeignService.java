package cn.chenyunlong.qing.domain.auth.connection.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionCreateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionQueryRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.request.UserConnectionUpdateRequest;
import cn.chenyunlong.qing.domain.auth.connection.dto.response.UserConnectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "userConnectionClient",
    path = "userConnection/v1"
)
public interface UserConnectionFeignService {

    /**
     * 创建
     */
    @PostMapping("createUserConnection")
    JsonResult<Long> createUserConnection(@RequestBody UserConnectionCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateUserConnection")
    JsonResult<String> updateUserConnection(@RequestBody UserConnectionUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validUserConnection(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidUserConnection(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<UserConnectionResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<UserConnectionResponse>> page(
        @RequestBody
        PageRequestWrapper<UserConnectionQueryRequest> request);
}
