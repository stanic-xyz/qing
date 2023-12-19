package cn.chenyunlong.qing.domain.auth.role.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleCreateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleQueryRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.request.RoleUpdateRequest;
import cn.chenyunlong.qing.domain.auth.role.dto.response.RoleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "roleClient",
        path = "role/v1"
)
public interface RoleFeignService {
    /**
     * 创建
     */
    @PostMapping("createRole")
    JsonResult<Long> createRole(@RequestBody RoleCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateRole")
    JsonResult<String> updateRole(@RequestBody RoleUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validRole(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidRole(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<RoleResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<RoleResponse>> page(
            @RequestBody PageRequestWrapper<RoleQueryRequest> request);
}
