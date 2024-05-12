package cn.chenyunlong.qing.domain.productcenter.template.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "templateClient",
    path = "template/v1"
)
public interface TemplateFeignService {

    /**
     * 创建
     */
    @PostMapping("createTemplate")
    JsonResult<Long> createTemplate(@RequestBody TemplateCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateTemplate")
    JsonResult<String> updateTemplate(@RequestBody TemplateUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validTemplate(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidTemplate(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<TemplateResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<TemplateResponse>> page(
        @RequestBody
        PageRequestWrapper<TemplateQueryRequest> request);
}
