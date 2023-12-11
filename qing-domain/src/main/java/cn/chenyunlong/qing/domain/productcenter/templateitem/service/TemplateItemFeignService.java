package cn.chenyunlong.qing.domain.productcenter.templateitem.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.response.TemplateItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "templateItemClient",
        path = "templateItem/v1"
)
public interface TemplateItemFeignService {
    /**
     * 创建
     */
    @PostMapping("createTemplateItem")
    JsonResult<Long> createTemplateItem(@RequestBody TemplateItemCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateTemplateItem")
    JsonResult<String> updateTemplateItem(@RequestBody TemplateItemUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validTemplateItem(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidTemplateItem(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<TemplateItemResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<TemplateItemResponse>> page(
            @RequestBody PageRequestWrapper<TemplateItemQueryRequest> request);
}
