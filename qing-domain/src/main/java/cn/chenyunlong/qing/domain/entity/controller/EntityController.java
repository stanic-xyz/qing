package cn.chenyunlong.qing.domain.entity.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.entity.dto.creator.EntityCreator;
import cn.chenyunlong.qing.domain.entity.dto.query.EntityQuery;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityCreateRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityQueryRequest;
import cn.chenyunlong.qing.domain.entity.dto.request.EntityUpdateRequest;
import cn.chenyunlong.qing.domain.entity.dto.response.EntityResponse;
import cn.chenyunlong.qing.domain.entity.dto.updater.EntityUpdater;
import cn.chenyunlong.qing.domain.entity.dto.vo.EntityVO;
import cn.chenyunlong.qing.domain.entity.mapper.EntityMapper;
import cn.chenyunlong.qing.domain.entity.service.IEntityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "实体管理", description = "实体管理")
@RestController
@Slf4j
@RequestMapping("api/v1/entity")
@RequiredArgsConstructor
public class EntityController {
    private final IEntityService entityService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createEntity(@RequestBody EntityCreateRequest request) {
        EntityCreator creator = EntityMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(entityService.createEntity(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateEntity")
    public JsonResult<String> updateEntity(@RequestBody EntityUpdateRequest request) {
        EntityUpdater updater = EntityMapper.INSTANCE.request2Updater(request);
        entityService.updateEntity(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validEntity(@PathVariable Long id) {
        entityService.validEntity(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidEntity(@PathVariable Long id) {
        entityService.invalidEntity(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<EntityResponse> findById(@PathVariable Long id) {
        EntityVO vo = entityService.findById(id);
        EntityResponse response = EntityMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<EntityResponse>> page(
        @RequestBody PageRequestWrapper<EntityQueryRequest> request) {
        PageRequestWrapper<EntityQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(EntityMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<EntityVO> page = entityService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(EntityMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
