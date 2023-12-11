package cn.chenyunlong.qing.domain.productcenter.template.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateCategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "templateCategoryClient",
        path = "templateCategory/v1"
)
public interface TemplateCategoryFeignService {
    /**
     * 创建
     */
    @PostMapping("createTemplateCategory")
    JsonResult<Long> createTemplateCategory(@RequestBody TemplateCategoryCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateTemplateCategory")
    JsonResult<String> updateTemplateCategory(@RequestBody TemplateCategoryUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validTemplateCategory(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidTemplateCategory(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<TemplateCategoryResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<TemplateCategoryResponse>> page(
            @RequestBody PageRequestWrapper<TemplateCategoryQueryRequest> request);
}
