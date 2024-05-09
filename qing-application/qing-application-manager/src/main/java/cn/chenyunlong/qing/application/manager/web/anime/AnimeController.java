package cn.chenyunlong.qing.application.manager.web.anime;


import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.IAnimeDomainService;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "番剧管理", description = "番剧管理")
@RestController
@Slf4j
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final IAnimeService animeService;
    private final IAnimeDomainService animeDomainService;

    @Operation(summary = "创建动漫信息")
    @PostMapping
    public JsonResult<Long> createAnime(
        @RequestBody
        AnimeCreateRequest request) {
        Long serviceAnime = animeDomainService.createAnime(request);
        return JsonResult.success(serviceAnime);
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
    @GetMapping("removeById/{id}")
    public JsonResult<Void> removeById(
        @PathVariable
        Long id) {
        animeService.removeById(id);
        return JsonResult.success();
    }
}
