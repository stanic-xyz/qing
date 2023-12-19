package cn.chenyunlong.qing.domain.auth.department.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentCreateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentQueryRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.request.DepartmentUpdateRequest;
import cn.chenyunlong.qing.domain.auth.department.dto.response.DepartmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "departmentClient",
        path = "department/v1"
)
public interface DepartmentFeignService {
    /**
     * 创建
     */
    @PostMapping("createDepartment")
    JsonResult<Long> createDepartment(@RequestBody DepartmentCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateDepartment")
    JsonResult<String> updateDepartment(@RequestBody DepartmentUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validDepartment(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidDepartment(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<DepartmentResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<DepartmentResponse>> page(
            @RequestBody PageRequestWrapper<DepartmentQueryRequest> request);
}
