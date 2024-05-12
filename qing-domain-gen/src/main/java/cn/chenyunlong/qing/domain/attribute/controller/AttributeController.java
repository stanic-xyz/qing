package cn.chenyunlong.qing.domain.attribute.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.attribute.dto.creator.AttributeCreator;
import cn.chenyunlong.qing.domain.attribute.dto.query.AttributeQuery;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeCreateRequest;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeQueryRequest;
import cn.chenyunlong.qing.domain.attribute.dto.request.AttributeUpdateRequest;
import cn.chenyunlong.qing.domain.attribute.dto.response.AttributeResponse;
import cn.chenyunlong.qing.domain.attribute.dto.updater.AttributeUpdater;
import cn.chenyunlong.qing.domain.attribute.dto.vo.AttributeVO;
import cn.chenyunlong.qing.domain.attribute.mapper.AttributeMapper;
import cn.chenyunlong.qing.domain.attribute.service.IAttributeService;
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

@Tag(name = "属性", description = "属性接口")
@RestController
@Slf4j
@RequestMapping("api/v1/attribute")
@RequiredArgsConstructor
public class AttributeController {

    private final IAttributeService attributeService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createAttribute(@RequestBody AttributeCreateRequest request) {
        AttributeCreator creator = AttributeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(attributeService.createAttribute(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateAttribute")
    public JsonResult<String> updateAttribute(@RequestBody AttributeUpdateRequest request) {
        AttributeUpdater updater = AttributeMapper.INSTANCE.request2Updater(request);
        attributeService.updateAttribute(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validAttribute(@PathVariable Long id) {
        attributeService.validAttribute(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidAttribute(@PathVariable Long id) {
        attributeService.invalidAttribute(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<AttributeResponse> findById(@PathVariable Long id) {
        AttributeVO vo = attributeService.findById(id);
        AttributeResponse response = AttributeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<AttributeResponse>> page(
        @RequestBody PageRequestWrapper<AttributeQueryRequest> request) {
        PageRequestWrapper<AttributeQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(AttributeMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<AttributeVO> page = attributeService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(AttributeMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
