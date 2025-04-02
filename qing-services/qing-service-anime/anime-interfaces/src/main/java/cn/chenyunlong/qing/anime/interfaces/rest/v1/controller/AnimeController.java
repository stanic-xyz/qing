package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;


import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.application.service.AnimeDomainService;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeRemoveCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeShelveOffCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeShelvingCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.CreatorAnimeCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeUpdateCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeMapper;
import cn.chenyunlong.qing.anime.interfaces.rest.v1.controller.dto.ShelveRequest;
import cn.chenyunlong.qing.anime.interfaces.service.AnimeQueryService;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "番剧管理", description = "番剧管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeDomainService animeService;
    private final AnimeQueryService animeQueryService;

    @Operation(summary = "创建动漫信息")
    @PostMapping
    public JsonResult<Long> createAnime(
        @Validated
        @RequestBody
        AnimeCreateRequest request) {
        CreatorAnimeCommand creator = AnimeMapper.INSTANCE.requestToCreator(request);
        Anime anime = animeService.createAnime(creator);
        return JsonResult.success(anime.getId().getId());
    }

    @Operation(summary = "更新动漫信息")
    @PutMapping("{id}")
    public JsonResult<String> updateAnime(
        @PathVariable("id") Long id,
        @RequestBody AnimeUpdateRequest request) {
        AnimeUpdateCommand updateCommand = AnimeMapper.INSTANCE.request2Updater(request);
        updateCommand.setId(new AnimeId(id));
        animeService.updateAnime(updateCommand);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("/{id}/shelve")
    public JsonResult<Void> shelveBook(
        @PathVariable("id") Long id,
        @RequestBody ShelveRequest shelveRequest
    ) {
        log.info("动漫上架：{}", JSONUtil.toJsonStr(shelveRequest));
        AnimeShelvingCommand command = new AnimeShelvingCommand(new AnimeId(id));
        animeService.shelveAnime(command);
        return JsonResult.success();
    }

    @PostMapping("/{id}/shelveOff")
    public JsonResult<Void> shelveOff(
        @PathVariable("id") Long id,
        @RequestBody ShelveRequest shelveRequest
    ) {
        log.info("动漫下架：{}", JSONUtil.toJsonStr(shelveRequest));
        AnimeShelveOffCommand command = new AnimeShelveOffCommand(new AnimeId(id));
        animeService.takeOffShelf(command);
        return JsonResult.success();
    }

    @DeleteMapping("/{id}")
    public JsonResult<Void> remove(@PathVariable("id") Long id) {
        AnimeRemoveCommand command = new AnimeRemoveCommand(new AnimeId(id));
        animeService.deleteAnime(command);
        return JsonResult.success();
    }

    @GetMapping
    public JsonResult<List<AnimeVO>> list() {
        List<AnimeVO> animeVOList = animeQueryService.listAll();
        return JsonResult.success(animeVOList);
    }
}
