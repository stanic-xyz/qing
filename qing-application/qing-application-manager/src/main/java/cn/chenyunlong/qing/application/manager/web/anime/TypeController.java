package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
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

@Tag(name = "分类管理")
@RestController
@Slf4j
@RequestMapping("api/v1/type")
@RequiredArgsConstructor
public class TypeController {

    private final ITypeService typeService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createType(
        @RequestBody
        TypeCreateRequest request) {
        TypeCreator creator = TypeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(typeService.createType(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateType")
    public JsonResult<String> updateType(
        @RequestBody
        TypeUpdateRequest request) {
        TypeUpdater updater = TypeMapper.INSTANCE.request2Updater(request);
        typeService.updateType(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validType(
        @PathVariable("id")
        Long id) {
        typeService.validType(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidType(
        @PathVariable("id")
        Long id) {
        typeService.invalidType(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<TypeResponse> findById(
        @PathVariable("id")
        Long id) {
        TypeVO vo = typeService.findById(id);
        TypeResponse response = TypeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<TypeResponse>> page(
        @RequestBody
        PageRequestWrapper<TypeQueryRequest> request) {
        PageRequestWrapper<TypeQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TypeMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TypeVO> page = typeService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(TypeMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
