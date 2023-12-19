package cn.chenyunlong.qing.domain.auth.admin.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountCreateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountQueryRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountUpdateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.response.AdminAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "adminAccountClient",
        path = "adminAccount/v1"
)
public interface AdminAccountFeignService {
    /**
     * 创建
     */
    @PostMapping("createAdminAccount")
    JsonResult<Long> createAdminAccount(@RequestBody AdminAccountCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateAdminAccount")
    JsonResult<String> updateAdminAccount(@RequestBody AdminAccountUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validAdminAccount(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidAdminAccount(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<AdminAccountResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<AdminAccountResponse>> page(
            @RequestBody PageRequestWrapper<AdminAccountQueryRequest> request);
}
