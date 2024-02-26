package cn.chenyunlong.qing.domain.auth.user.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.user.dto.request.QingUserCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.QingUserQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.QingUserUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.QingUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "qingUserClient",
    path = "qingUser/v1"
)
public interface QingUserFeignService {

    /**
     * 创建
     */
    @PostMapping("createQingUser")
    JsonResult<Long> createQingUser(@RequestBody QingUserCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateQingUser")
    JsonResult<String> updateQingUser(@RequestBody QingUserUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validQingUser(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidQingUser(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<QingUserResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<QingUserResponse>> page(
        @RequestBody
        PageRequestWrapper<QingUserQueryRequest> request);
}
