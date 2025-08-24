package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.application.service.AnimeCategoryService;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeCategoryCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.response.AnimeCategoryResponse;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryTreeVO;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeCategoryInfrastructureMapper;
import cn.chenyunlong.qing.anime.interfaces.service.AnimeQueryService;
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

import java.util.List;

@Tag(name = "动漫分类")
@RestController
@Slf4j
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
@Validated
public class AnimeCategoryController {

    private final AnimeCategoryService animeCategoryService;
    private final AnimeQueryService animeQueryService;
    private final AnimeCategoryInfrastructureMapper animeCategoryInfrastructureMapper;

    @PostMapping
    @Operation(summary = "创建动漫分类", description = "创建新的动漫分类")
    public JsonResult<Long> createAnimeCategory(
            @Valid @RequestBody
            AnimeCategoryCreateRequest request) {
        AnimeCategoryCreator creator = animeCategoryInfrastructureMapper.request2Dto(request);
        Category animeCategory = animeCategoryService.createAnimeCategory(creator);
        return JsonResult.success(animeCategory.getId().getValue());
    }

    @PutMapping("{catId}")
    @Operation(summary = "更新动漫分类", description = "更新指定的动漫分类信息")
    public JsonResult<String> updateAnimeCategory(
            @Valid @RequestBody
            AnimeCategoryUpdateRequest request, @PathVariable("catId") String catId) {
        AnimeCategoryUpdater updater = animeCategoryInfrastructureMapper.request2Updater(request);
        animeCategoryService.updateAnimeCategory(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("{id}/valid")
    @Operation(summary = "启用动漫分类", description = "启用指定的动漫分类")
    public JsonResult<String> validAnimeCategory(
            @Parameter(description = "分类ID") @PathVariable("id")
            @NotNull @Positive Long id) {
        animeCategoryService.validAnimeCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("{id}/invalid")
    @Operation(summary = "禁用动漫分类", description = "禁用指定的动漫分类")
    public JsonResult<String> invalidAnimeCategory(
            @Parameter(description = "分类ID") @PathVariable("id") @NotNull @Positive
            Long id) {
        animeCategoryService.invalidAnimeCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("{id}")
    @Operation(summary = "根据ID查询分类", description = "根据分类ID查询分类详情")
    public JsonResult<AnimeCategoryResponse> findById(
            @Parameter(description = "分类ID") @PathVariable("id") @NotNull @Positive
            Long id) {
        AnimeCategoryVO vo = animeCategoryService.findById(id);
        AnimeCategoryResponse response = animeCategoryInfrastructureMapper.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @GetMapping("tree")
    @Operation(summary = "获取分类树", description = "获取动漫分类的树形结构")
    public JsonResult<List<AnimeCategoryTreeVO>> tree() {
        List<AnimeCategoryTreeVO> vo = animeQueryService.tree();
        return JsonResult.success(vo);
    }
}
