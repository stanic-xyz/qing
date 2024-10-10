package cn.chenyunlong.qing.domain.productcenter.template.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.template.dto.creator.TemplateCreator;
import cn.chenyunlong.qing.domain.productcenter.template.dto.query.TemplateQuery;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.request.TemplateUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.template.dto.response.TemplateResponse;
import cn.chenyunlong.qing.domain.productcenter.template.dto.updater.TemplateUpdater;
import cn.chenyunlong.qing.domain.productcenter.template.dto.vo.TemplateVO;
import cn.chenyunlong.qing.domain.productcenter.template.mapper.TemplateMapper;
import cn.chenyunlong.qing.domain.productcenter.template.service.ITemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "模板管理")
@RestController
@Slf4j
@RequestMapping("api/v1/template")
@RequiredArgsConstructor
public class TemplateController {

    private final ITemplateService templateService;

    @PostMapping
    public JsonResult<Long> createTemplate(
        @RequestBody
        TemplateCreateRequest request) {
        TemplateCreator creator = TemplateMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(templateService.createTemplate(creator));
    }

    @PostMapping("updateTemplate")
    public JsonResult<String> updateTemplate(@RequestBody TemplateUpdateRequest request) {
        TemplateUpdater updater = TemplateMapper.INSTANCE.request2Updater(request);
        templateService.updateTemplate(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validTemplate(
        @PathVariable
        Long id) {
        templateService.validTemplate(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidTemplate(
        @PathVariable
        Long id) {
        templateService.invalidTemplate(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<TemplateResponse> findById(@PathVariable Long id) {
        TemplateVO vo = templateService.findById(id);
        TemplateResponse response = TemplateMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<TemplateResponse>> page(
        @RequestBody
        PageRequestWrapper<TemplateQueryRequest> request) {
        PageRequestWrapper<TemplateQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TemplateMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TemplateVO> page = templateService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(TemplateMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
