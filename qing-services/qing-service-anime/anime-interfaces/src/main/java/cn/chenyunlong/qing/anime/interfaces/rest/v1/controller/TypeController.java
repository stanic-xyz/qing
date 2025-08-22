package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.domain.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.anime.domain.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.anime.infrastructure.converter.TypeMapper;
import cn.chenyunlong.qing.anime.application.service.ITypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "分类管理")
@RestController
@Slf4j
@RequestMapping("api/v1/type")
@RequiredArgsConstructor
@Validated
public class TypeController {

    private final ITypeService typeService;

    @PostMapping
    @Operation(summary = "创建类型", description = "创建新的动漫类型")
    public JsonResult<Long> createType(
        @Valid @RequestBody TypeCreateRequest request) {
        TypeCreator creator = TypeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(typeService.createType(creator));
    }

    @PostMapping("valid/{id}")
    @Operation(summary = "启用类型", description = "启用指定的动漫类型")
    public JsonResult<String> validType(
        @Parameter(description = "类型ID") @PathVariable("id") @NotNull @Positive Long id) {
        typeService.validType(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    @Operation(summary = "禁用类型", description = "禁用指定的动漫类型")
    public JsonResult<String> invalidType(
        @Parameter(description = "类型ID") @PathVariable("id") @NotNull @Positive Long id) {
        typeService.invalidType(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

}
