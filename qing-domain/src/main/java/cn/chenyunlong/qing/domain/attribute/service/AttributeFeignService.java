package cn.chenyunlong.qing.domain.attribute.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeCreateRequest;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeQueryRequest;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeUpdateRequest;
import cn.chenyunlong.qing.domain.attribute.dto.response.AttributeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "attributeClient",
    path = "attribute/v1"
)
public interface AttributeFeignService {
    /**
     * 创建
     */
    @PostMapping("createAttribute")
    JsonResult<Long> createAttribute(@RequestBody AttributeCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateAttribute")
    JsonResult<String> updateAttribute(@RequestBody AttributeUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validAttribute(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidAttribute(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<AttributeResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<AttributeResponse>> page(
        @RequestBody PageRequestWrapper<AttributeQueryRequest> request);
}
