package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.application.command.CreateAnimeCommand;
import cn.chenyunlong.qing.anime.application.command.UpdateAnimeCommand;
import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.application.query.AnimeQuery;
import cn.chenyunlong.qing.anime.application.query.AnimeQueryService;
import cn.chenyunlong.qing.anime.application.service.AnimeApplicationService;
import cn.chenyunlong.qing.anime.application.service.AnimeDomainService;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeRemoveCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeShelveOffCommand;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeShelvingCommand;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.anime.infrastructure.converter.EpisodeMapper;
import cn.chenyunlong.qing.anime.interfaces.dto.request.AnimeCreateRequest;
import cn.chenyunlong.qing.anime.interfaces.dto.request.AnimeUpdateRequest;
import cn.chenyunlong.qing.anime.interfaces.dto.response.AnimeResponse;
import cn.chenyunlong.qing.anime.interfaces.dto.response.PageResponse;
import cn.chenyunlong.qing.anime.interfaces.mapper.AnimeWebMapper;
import cn.chenyunlong.qing.anime.interfaces.rest.v1.controller.dto.ShelveRequest;
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

    private final AnimeDomainService animeDomainService;
    private final AnimeQueryService animeQueryService;
    private final AnimeWebMapper animeWebMapper;
    private final EpisodeMapper episodeMapper;
    private final AnimeApplicationService animeApplicationService;

    @Operation(summary = "创建动漫信息")
    @PostMapping
    public JsonResult<Long> createAnime(
            @Valid @RequestBody AnimeCreateRequest request) {
        CreateAnimeCommand createCommand = animeWebMapper.toCreateCommand(request);
        AnimeDTO anime = animeApplicationService.createAnime(createCommand);
        return JsonResult.success(anime.getId());
    }

    @Operation(summary = "更新动漫信息")
    @PutMapping("{id}")
    public JsonResult<String> updateAnime(
            @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id,
            @Valid @RequestBody AnimeUpdateRequest request) {
        UpdateAnimeCommand updateCommand = animeWebMapper.toUpdateCommand(id, request);
        animeApplicationService.updateAnime(updateCommand);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "上架动漫")
    @PostMapping("/{id}/shelve")
    public JsonResult<Void> shelveBook(
            @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id,
            @Valid @RequestBody ShelveRequest shelveRequest) {
        log.info("动漫上架：{}", JSONUtil.toJsonStr(shelveRequest));
        AnimeShelvingCommand command = new AnimeShelvingCommand(AnimeId.of(id));
        animeDomainService.shelveAnime(command);
        return JsonResult.success();
    }

    @Operation(summary = "下架动漫")
    @PostMapping("/{id}/shelveOff")
    public JsonResult<Void> shelveOff(
            @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id,
            @Valid @RequestBody ShelveRequest shelveRequest) {
        log.info("动漫下架：{}", JSONUtil.toJsonStr(shelveRequest));
        AnimeShelveOffCommand command = new AnimeShelveOffCommand(AnimeId.of(id));
        animeDomainService.takeOffShelf(command);
        return JsonResult.success();
    }

    @Operation(summary = "删除动漫")
    @DeleteMapping("/{id}")
    public JsonResult<Void> remove(@Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        AnimeRemoveCommand command = new AnimeRemoveCommand(AnimeId.of(id));
        animeDomainService.deleteAnime(command);
        return JsonResult.success();
    }

    @GetMapping
    public JsonResult<List<AnimeResponse>> list() {
        List<AnimeDTO> animeVOList = animeQueryService.listAll();
        return JsonResult.success(animeWebMapper.toResponseList(animeVOList));
    }

    @Operation(summary = "根据ID查询动漫信息")
    @GetMapping("/{id}")
    public JsonResult<AnimeResponse> getById(
            @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        AnimeDTO animeDTO = animeQueryService.findById(AnimeId.of(id)).orElseThrow();
        AnimeResponse response = animeWebMapper.toResponse(animeDTO);
        return JsonResult.success(response);
    }

    @Operation(summary = "分页查询动漫列表")
    @GetMapping("/page")
    public JsonResult<PageResponse<AnimeResponse>> page(
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") @Positive Integer page,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") @Positive Integer size,
            @Parameter(description = "搜索关键词") @RequestParam(value = "keyword", required = false) String keyword) {
        AnimeQuery animeQuery = AnimeQuery.all();
        PageResult<AnimeDTO> animeVOList = animeQueryService.findPage(animeQuery);
        return JsonResult.success(animeWebMapper.toPageResponse(animeVOList));
    }

    @Operation(summary = "根据分类查询动漫列表")
    @GetMapping("/category/{categoryId}")
    public JsonResult<PageResult<AnimeResponse>> listByCategory(
            @Parameter(description = "分类ID") @PathVariable("categoryId") @NotNull @Positive Long categoryId,
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") @Positive Integer page,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") @Positive Integer size
    ) {
        PageResult<AnimeDTO> byCategory = animeQueryService.findByCategory(categoryId, page, size);
        PageResult<AnimeResponse> responsePageResult = byCategory.map(animeWebMapper::toResponse);
        return JsonResult.success(responsePageResult);
    }

    @Operation(summary = "启用动漫")
    @PostMapping("/{id}/enable")
    public JsonResult<Void> enable(
            @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        animeDomainService.validAnime(id);
        return JsonResult.success();
    }

    @Operation(summary = "禁用动漫")
    @PostMapping("/{id}/disable")
    public JsonResult<Void> disable(
            @Parameter(description = "动漫ID") @PathVariable("id") @NotNull @Positive Long id) {
        animeDomainService.invalidAnime(id);
        return JsonResult.success();
    }
}
