package cn.chenyunlong.qing.domain.productcenter.template.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCategoryCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateCategoryQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateCategoryResponse;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateCategoryUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateCategoryVO;
import cn.chenyunlong.qing.domain.productcenter.template.mapper.TemplateCategoryMapper;
import cn.chenyunlong.qing.domain.productcenter.template.service.ITemplateCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("api/v1/template-category")
@RequiredArgsConstructor
public class TemplateCategoryController {
    private final ITemplateCategoryService templateCategoryService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createTemplateCategory(
            @RequestBody TemplateCategoryCreateRequest request) {
        TemplateCategoryCreator creator = TemplateCategoryMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(templateCategoryService.createTemplateCategory(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateTemplateCategory")
    public JsonResult<String> updateTemplateCategory(
            @RequestBody TemplateCategoryUpdateRequest request) {
        TemplateCategoryUpdater updater = TemplateCategoryMapper.INSTANCE.request2Updater(request);
        templateCategoryService.updateTemplateCategory(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validTemplateCategory(@PathVariable Long id) {
        templateCategoryService.validTemplateCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidTemplateCategory(@PathVariable Long id) {
        templateCategoryService.invalidTemplateCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<TemplateCategoryResponse> findById(@PathVariable Long id) {
        TemplateCategoryVO vo = templateCategoryService.findById(id);
        TemplateCategoryResponse response = TemplateCategoryMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<TemplateCategoryResponse>> page(
            @RequestBody PageRequestWrapper<TemplateCategoryQueryRequest> request) {
        PageRequestWrapper<TemplateCategoryQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TemplateCategoryMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TemplateCategoryVO> page = templateCategoryService.findByPage(wrapper);
        return JsonResult.success(
                PageResult.of(
                        page.getContent().stream()
                                .map(TemplateCategoryMapper.INSTANCE::vo2CustomResponse)
                                .collect(Collectors.toList()),
                        page.getTotalElements(),
                        page.getSize(),
                        page.getNumber())
        );
    }
}
