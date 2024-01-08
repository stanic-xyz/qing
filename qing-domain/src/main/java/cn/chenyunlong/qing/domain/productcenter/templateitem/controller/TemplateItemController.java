package cn.chenyunlong.qing.domain.productcenter.templateitem.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.creator.TemplateItemCreator;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.query.TemplateItemQuery;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.request.TemplateItemUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.response.TemplateItemResponse;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.updater.TemplateItemUpdater;
import cn.chenyunlong.qing.domain.productcenter.templateitem.dto.vo.TemplateItemVO;
import cn.chenyunlong.qing.domain.productcenter.templateitem.mapper.TemplateItemMapper;
import cn.chenyunlong.qing.domain.productcenter.templateitem.service.ITemplateItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "模板项管理")
@RestController
@Slf4j
@RequestMapping("api/v1/template-item")
@RequiredArgsConstructor
public class TemplateItemController {
    private final ITemplateItemService templateItemService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createTemplateItem(@RequestBody TemplateItemCreateRequest request) {
        TemplateItemCreator creator = TemplateItemMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(templateItemService.createTemplateItem(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateTemplateItem")
    public JsonResult<String> updateTemplateItem(@RequestBody TemplateItemUpdateRequest request) {
        TemplateItemUpdater updater = TemplateItemMapper.INSTANCE.request2Updater(request);
        templateItemService.updateTemplateItem(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validTemplateItem(@PathVariable Long id) {
        templateItemService.validTemplateItem(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidTemplateItem(@PathVariable Long id) {
        templateItemService.invalidTemplateItem(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<TemplateItemResponse> findById(@PathVariable Long id) {
        TemplateItemVO vo = templateItemService.findById(id);
        TemplateItemResponse response = TemplateItemMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<TemplateItemResponse>> page(
            @RequestBody PageRequestWrapper<TemplateItemQueryRequest> request) {
        PageRequestWrapper<TemplateItemQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TemplateItemMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TemplateItemVO> page = templateItemService.findByPage(wrapper);
        return JsonResult.success(
                PageResult.of(
                        page.getContent().stream()
                                .map(TemplateItemMapper.INSTANCE::vo2CustomResponse)
                                .collect(Collectors.toList()),
                        page.getTotalElements(),
                        page.getSize(),
                        page.getNumber())
        );
    }
}
