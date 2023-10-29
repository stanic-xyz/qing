package cn.chenyunlong.qing.domain.user.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.user.dto.request.UserCreateRequest;
import cn.chenyunlong.qing.domain.user.dto.request.UserQueryRequest;
import cn.chenyunlong.qing.domain.user.dto.request.UserUpdateRequest;
import cn.chenyunlong.qing.domain.user.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "userClient",
    path = "user/v1"
)
public interface UserFeignService {
    /**
     * 创建
     */
    @PostMapping("createUser")
    JsonResult<Long> createUser(@RequestBody UserCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateUser")
    JsonResult<String> updateUser(@RequestBody UserUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validUser(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidUser(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<UserResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<UserResponse>> page(
        @RequestBody PageRequestWrapper<UserQueryRequest> request);
}
