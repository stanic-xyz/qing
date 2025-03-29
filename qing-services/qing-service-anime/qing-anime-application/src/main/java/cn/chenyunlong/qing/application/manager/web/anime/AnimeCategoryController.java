package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryQueryRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCategoryUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.response.AnimeCategoryResponse;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeCategoryMapper;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "动漫分类")
@RestController
@Slf4j
@RequestMapping("api/v1/anime-category")
@RequiredArgsConstructor
public class AnimeCategoryController {

    private final IAnimeCategoryService animeCategoryService;

    @PostMapping
    public JsonResult<Long> createAnimeCategory(
        @RequestBody
        AnimeCategoryCreateRequest request) {
        AnimeCategoryCreator creator = AnimeCategoryMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(animeCategoryService.createAnimeCategory(creator));
    }

    @PostMapping("updateAnimeCategory")
    public JsonResult<String> updateAnimeCategory(
        @RequestBody
        AnimeCategoryUpdateRequest request) {
        AnimeCategoryUpdater updater = AnimeCategoryMapper.INSTANCE.request2Updater(request);
        animeCategoryService.updateAnimeCategory(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validAnimeCategory(
        @PathVariable("id")
        Long id) {
        animeCategoryService.validAnimeCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidAnimeCategory(
        @PathVariable("id")
        Long id) {
        animeCategoryService.invalidAnimeCategory(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<AnimeCategoryResponse> findById(
        @PathVariable("id")
        Long id) {
        AnimeCategoryVO vo = animeCategoryService.findById(id);
        AnimeCategoryResponse response = AnimeCategoryMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }
}
