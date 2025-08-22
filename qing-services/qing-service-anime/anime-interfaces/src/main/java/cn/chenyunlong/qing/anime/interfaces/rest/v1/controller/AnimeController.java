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
        @Valid @RequestBody AnimeCreateRequest request) {
        CreatorAnimeCommand creator = AnimeMapper.INSTANCE.requestToCreator(request);
        Anime anime = animeService.createAnime(creator);
        return JsonResult.success(anime.getId().getId());
    }

    @Operation(summary = "更新动漫信息")
    @PutMapping("{id}")
    public JsonResult<String> updateAnime(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id,
        @Valid @RequestBody AnimeUpdateRequest request) {
        AnimeUpdateCommand updateCommand = AnimeMapper.INSTANCE.request2Updater(request);
        updateCommand.setId(new AnimeId(id));
        animeService.updateAnime(updateCommand);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "上架动漫")
    @PostMapping("/{id}/shelve")
    public JsonResult<Void> shelveBook(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id,
        @Valid @RequestBody ShelveRequest shelveRequest
    ) {
        log.info("动漫上架：{}", JSONUtil.toJsonStr(shelveRequest));
        AnimeShelvingCommand command = new AnimeShelvingCommand(new AnimeId(id));
        animeService.shelveAnime(command);
        return JsonResult.success();
    }

    @Operation(summary = "下架动漫")
    @PostMapping("/{id}/shelveOff")
    public JsonResult<Void> shelveOff(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id,
        @Valid @RequestBody ShelveRequest shelveRequest
    ) {
        log.info("动漫下架：{}", JSONUtil.toJsonStr(shelveRequest));
        AnimeShelveOffCommand command = new AnimeShelveOffCommand(new AnimeId(id));
        animeService.takeOffShelf(command);
        return JsonResult.success();
    }

    @Operation(summary = "删除动漫")
    @DeleteMapping("/{id}")
    public JsonResult<Void> remove(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        AnimeRemoveCommand command = new AnimeRemoveCommand(new AnimeId(id));
        animeService.deleteAnime(command);
        return JsonResult.success();
    }

    @GetMapping
    public JsonResult<List<AnimeVO>> list() {
        List<AnimeVO> animeVOList = animeQueryService.listAll();
        return JsonResult.success(animeVOList);
    }

    @Operation(summary = "根据ID查询动漫信息")
    @GetMapping("/{id}")
    public JsonResult<AnimeVO> getById(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        AnimeVO animeVO = animeQueryService.getById(id);
        return JsonResult.success(animeVO);
    }

    @Operation(summary = "分页查询动漫列表")
    @GetMapping("/page")
    public JsonResult<List<AnimeVO>> page(
        @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") @Positive Integer page,
        @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") @Positive Integer size,
        @Parameter(description = "搜索关键词") @RequestParam(value = "keyword", required = false) String keyword) {
        List<AnimeVO> animeVOList = animeQueryService.page(page, size, keyword);
        return JsonResult.success(animeVOList);
    }

    @Operation(summary = "根据分类查询动漫列表")
    @GetMapping("/category/{categoryId}")
    public JsonResult<List<AnimeVO>> listByCategory(
        @Parameter(description = "分类ID") @PathVariable("categoryId") @NotNull @Positive Long categoryId) {
        List<AnimeVO> animeVOList = animeQueryService.listByCategory(categoryId);
        return JsonResult.success(animeVOList);
    }

    @Operation(summary = "启用动漫")
    @PostMapping("/{id}/enable")
    public JsonResult<Void> enable(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        animeService.validAnime(id);
        return JsonResult.success();
    }

    @Operation(summary = "禁用动漫")
    @PostMapping("/{id}/disable")
    public JsonResult<Void> disable(
        @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        animeService.invalidAnime(id);
        return JsonResult.success();
    }
}
