package cn.chenyunlong.qing.domain.entity.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityCreateRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityQueryRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityUpdateRequest;
import cn.chenyunlong.qing.domain.entity.dto.response.EntityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "entityClient",
    path = "entity/v1"
)
public interface EntityFeignService {

    /**
     * 创建
     */
    @PostMapping("createEntity")
    JsonResult<Long> createEntity(@RequestBody EntityCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateEntity")
    JsonResult<String> updateEntity(@RequestBody EntityUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validEntity(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidEntity(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<EntityResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<EntityResponse>> page(
        @RequestBody PageRequestWrapper<EntityQueryRequest> request);
}
