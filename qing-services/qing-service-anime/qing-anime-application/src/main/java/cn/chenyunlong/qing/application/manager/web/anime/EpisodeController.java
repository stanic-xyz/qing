package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.anime.episode.dto.query.EpisodeQuery;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeCreateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeQueryRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.request.EpisodeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.episode.dto.response.EpisodeResponse;
import cn.chenyunlong.qing.domain.anime.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.domain.anime.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.domain.anime.episode.mapper.EpisodeMapper;
import cn.chenyunlong.qing.domain.anime.episode.service.IEpisodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @PostMapping
    public JsonResult<Long> createEpisode(
        @Valid
        @RequestBody
        EpisodeCreateRequest request) {
        EpisodeCreator creator = EpisodeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(episodeService.createEpisode(creator));
    }

    @PostMapping("updateEpisode")
    public JsonResult<String> updateEpisode(
        @RequestBody
        EpisodeUpdateRequest request) {
        EpisodeUpdater updater = EpisodeMapper.INSTANCE.request2Updater(request);
        episodeService.updateEpisode(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validEpisode(
        @PathVariable("id")
        Long id) {
        episodeService.validEpisode(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidEpisode(
        @PathVariable("id")
        Long id) {
        episodeService.invalidEpisode(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }


    @GetMapping("findById/{id}")
    public JsonResult<EpisodeResponse> findById(
        @PathVariable("id")
        Long id) {
        EpisodeVO vo = episodeService.findById(id);
        EpisodeResponse response = EpisodeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }
}
