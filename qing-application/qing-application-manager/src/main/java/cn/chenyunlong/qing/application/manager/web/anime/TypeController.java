package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.anime.type.dto.query.TypeQuery;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeQueryRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.response.TypeResponse;
import cn.chenyunlong.qing.domain.anime.type.dto.updater.TypeUpdater;
import cn.chenyunlong.qing.domain.anime.type.dto.vo.TypeVO;
import cn.chenyunlong.qing.domain.anime.type.mapper.TypeMapper;
import cn.chenyunlong.qing.domain.anime.type.service.ITypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "分类管理")
@RestController
@Slf4j
@RequestMapping("api/v1/type")
@RequiredArgsConstructor
public class TypeController {

    private final ITypeService typeService;

    @PostMapping
    public JsonResult<Long> createType(
        @RequestBody
        TypeCreateRequest request) {
        TypeCreator creator = TypeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(typeService.createType(creator));
    }

    @PostMapping("updateType")
    public JsonResult<String> updateType(
        @RequestBody
        TypeUpdateRequest request) {
        TypeUpdater updater = TypeMapper.INSTANCE.request2Updater(request);
        typeService.updateType(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validType(
        @PathVariable("id")
        Long id) {
        typeService.validType(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidType(
        @PathVariable("id")
        Long id) {
        typeService.invalidType(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<TypeResponse> findById(
        @PathVariable("id")
        Long id) {
        TypeVO vo = typeService.findById(id);
        TypeResponse response = TypeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<Page<TypeResponse>> page(
        @RequestBody
        PageRequestWrapper<TypeQueryRequest> request) {
        PageRequestWrapper<TypeQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TypeMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TypeVO> page = typeService.findByPage(wrapper);
        return JsonResult.success(page.map(TypeMapper.INSTANCE::vo2CustomResponse));
    }
}
