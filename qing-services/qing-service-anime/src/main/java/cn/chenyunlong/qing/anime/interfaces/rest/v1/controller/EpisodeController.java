package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.anime.application.service.IEpisodeService;
import cn.chenyunlong.qing.anime.domain.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.anime.domain.episode.dto.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.anime.domain.episode.dto.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.anime.domain.episode.dto.response.EpisodeResponse;
import cn.chenyunlong.qing.anime.domain.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.anime.domain.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.anime.infrastructure.converter.EpisodeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "单集管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final IEpisodeService episodeService;
    private final EpisodeMapper episodeMapper;

    @Operation(summary = "创建单集", description = "创建一个新的动漫单集")
    @PostMapping
    public JsonResult<Long> createEpisode(
            @Valid
            @RequestBody
            @Parameter(description = "单集创建请求", required = true)
            EpisodeCreateRequest request) {
        EpisodeCreator creator = episodeMapper.request2Dto(request);
        return JsonResult.success(episodeService.createEpisode(creator));
    }

    @Operation(summary = "更新单集", description = "更新单集信息")
    @PostMapping("updateEpisode")
    public JsonResult<String> updateEpisode(
            @Valid
            @RequestBody
            @Parameter(description = "单集更新请求", required = true)
            EpisodeUpdateRequest request) {
        EpisodeUpdater updater = episodeMapper.request2Updater(request);
        episodeService.updateEpisode(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "启用单集", description = "将指定单集设置为有效状态")
    @PostMapping("valid/{id}")
    public JsonResult<String> validEpisode(
            @PathVariable("id")
            @NotNull
            @Parameter(description = "单集ID", required = true)
            Long id) {
        episodeService.validEpisode(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @Operation(summary = "禁用单集", description = "将指定单集设置为无效状态")
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidEpisode(
            @PathVariable("id")
            @NotNull
            @Parameter(description = "单集ID", required = true)
            Long id) {
        episodeService.invalidEpisode(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @Operation(summary = "根据ID查询单集", description = "根据单集ID获取单集详细信息")
    @GetMapping("findById/{id}")
    public JsonResult<EpisodeResponse> findById(
            @PathVariable("id")
            @NotNull
            @Parameter(description = "单集ID", required = true)
            Long id) {
        EpisodeVO vo = episodeService.findById(id);
        EpisodeResponse response = episodeMapper.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }
}
