package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.domain.anime.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.domain.anime.type.mapper.TypeMapper;
import cn.chenyunlong.qing.domain.anime.type.service.ITypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
