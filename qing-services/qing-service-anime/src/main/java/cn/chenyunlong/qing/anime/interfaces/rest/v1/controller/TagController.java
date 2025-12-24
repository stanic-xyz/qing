package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.application.service.ITagService;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.TagCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.anime.infrastructure.converter.TagMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "标签管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @PostMapping
    @Operation(summary = "创建标签", description = "创建新的标签")
    public JsonResult<Long> createTag(
        @Valid @RequestBody TagCreateRequest request) {
        TagCreator creator = TagMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(tagService.createTag(creator));
    }

    @PostMapping("valid/{id}")
    @Operation(summary = "启用标签", description = "启用指定的标签")
    public JsonResult<String> validTag(
        @Parameter(description = "标签ID") @PathVariable("id") @NotNull Long id) {
        tagService.validTag(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    @Operation(summary = "禁用标签", description = "禁用指定的标签")
    public JsonResult<String> invalidTag(
        @Parameter(description = "标签ID") @PathVariable("id") @NotNull Long id) {
        tagService.invalidTag(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
