package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;


import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.application.service.IAnimeService;
import cn.chenyunlong.qing.anime.domain.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCreateCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "番剧管理", description = "番剧管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final IAnimeService animeService;

    @Operation(summary = "创建动漫信息")
    @PostMapping
    public JsonResult<Long> createAnime(
        @Validated
        @RequestBody
        AnimeCreateCommand request) {
        AnimeCreator creator = AnimeMapper.INSTANCE.requestToCreator(request);
        Anime anime = animeService.createAnime(creator);
        return JsonResult.success(anime.getAggregateId().getId());
    }

    @Operation(summary = "更新动漫信息")
    @PostMapping("updateAnime")
    public JsonResult<String> updateAnime(
        @RequestBody
        AnimeUpdateRequest request) {
        AnimeUpdater updater = AnimeMapper.INSTANCE.request2Updater(request);
        animeService.updateAnime(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "根据id移除动漫信息")
    @DeleteMapping("removeById/{id}")
    public JsonResult<Void> removeById(
        @PathVariable("id")
        Long id) {
        animeService.removeById(id);
        return JsonResult.success();
    }

}
