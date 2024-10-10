package cn.chenyunlong.qing.application.manager.web.zan;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
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

@Tag(name = "实体管理", description = "实体管理")
@RestController
@Slf4j
@RequestMapping("api/v1/entity")
@RequiredArgsConstructor
public class EntityController {

    private final IEntityService entityService;

    @PostMapping
    public JsonResult<Long> createEntity(
        @RequestBody
        EntityCreateRequest request) {
        EntityCreator creator = EntityMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(entityService.createEntity(creator));
    }

    @PostMapping("updateEntity")
    public JsonResult<String> updateEntity(
        @RequestBody
        EntityUpdateRequest request) {
        EntityUpdater updater = EntityMapper.INSTANCE.request2Updater(request);
        entityService.updateEntity(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validEntity(
        @PathVariable("id")
        Long id) {
        entityService.validEntity(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidEntity(
        @PathVariable("id")
        Long id) {
        entityService.invalidEntity(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<EntityResponse> findById(
        @PathVariable("id")
        Long id) {
        EntityVO vo = entityService.findById(id);
        EntityResponse response = EntityMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<Page<EntityResponse>> page(
        @RequestBody
        PageRequestWrapper<EntityQueryRequest> request) {
        PageRequestWrapper<EntityQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(EntityMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<EntityVO> page = entityService.findByPage(wrapper);
        return JsonResult.success(page.map(EntityMapper.INSTANCE::vo2CustomResponse));
    }
}
