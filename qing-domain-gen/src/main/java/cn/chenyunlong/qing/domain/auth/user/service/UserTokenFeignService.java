package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserTokenUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "userTokenClient",
    path = "userToken/v1"
)
public interface UserTokenFeignService {

    /**
     * 创建
     */
    @PostMapping("createUserToken")
    JsonResult<Long> createUserToken(@RequestBody UserTokenCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateUserToken")
    JsonResult<String> updateUserToken(@RequestBody UserTokenUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validUserToken(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidUserToken(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<UserTokenResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<UserTokenResponse>> page(
        @RequestBody
        PageRequestWrapper<UserTokenQueryRequest> request);
}
