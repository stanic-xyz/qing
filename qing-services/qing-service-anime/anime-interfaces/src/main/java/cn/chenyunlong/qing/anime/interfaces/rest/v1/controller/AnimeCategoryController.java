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
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeCategoryMapper;
import cn.chenyunlong.qing.anime.interfaces.service.AnimeQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "动漫分类")
@RestController
@Slf4j
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class AnimeCategoryController {

    private final AnimeCategoryService animeCategoryService;
    private final AnimeQueryService animeQueryService;

    @PostMapping
    public JsonResult<Long> createAnimeCategory(
        @RequestBody
        AnimeCategoryCreateRequest request) {
        AnimeCategoryCreator creator = AnimeCategoryMapper.INSTANCE.request2Dto(request);
        Category animeCategory = animeCategoryService.createAnimeCategory(creator);
        return JsonResult.success(animeCategory.getId().getId());
    }

    @PutMapping("{catId}")
    public JsonResult<String> updateAnimeCategory(
        @RequestBody
        AnimeCategoryUpdateRequest request) {
        AnimeCategoryUpdater updater = AnimeCategoryMapper.INSTANCE.request2Updater(request);
        animeCategoryService.updateAnimeCategory(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("{id}/valid")
    public JsonResult<String> validAnimeCategory(
        @PathVariable("id")
        Long id) {
        animeCategoryService.validAnimeCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("{id}/invalid")
    public JsonResult<String> invalidAnimeCategory(
        @PathVariable("id")
        Long id) {
        animeCategoryService.invalidAnimeCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("{id}")
    public JsonResult<AnimeCategoryResponse> findById(
        @PathVariable("id")
        Long id) {
        AnimeCategoryVO vo = animeCategoryService.findById(id);
        AnimeCategoryResponse response = AnimeCategoryMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @GetMapping("tree")
    public JsonResult<List<AnimeCategoryTreeVO>> tree() {
        List<AnimeCategoryTreeVO> vo = animeQueryService.tree();
        return JsonResult.success(vo);
    }
}
