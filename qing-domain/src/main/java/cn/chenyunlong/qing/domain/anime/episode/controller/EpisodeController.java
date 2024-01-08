package cn.chenyunlong.qing.domain.anime.episode.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "单集管理")
@RestController
@Slf4j
@RequestMapping("api/v1/episode")
@RequiredArgsConstructor
public class EpisodeController {
    private final IEpisodeService episodeService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createEpisode(@RequestBody EpisodeCreateRequest request) {
        EpisodeCreator creator = EpisodeMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(episodeService.createEpisode(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateEpisode")
    public JsonResult<String> updateEpisode(@RequestBody EpisodeUpdateRequest request) {
        EpisodeUpdater updater = EpisodeMapper.INSTANCE.request2Updater(request);
        episodeService.updateEpisode(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validEpisode(@PathVariable Long id) {
        episodeService.validEpisode(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidEpisode(@PathVariable Long id) {
        episodeService.invalidEpisode(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<EpisodeResponse> findById(@PathVariable Long id) {
        EpisodeVO vo = episodeService.findById(id);
        EpisodeResponse response = EpisodeMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<EpisodeResponse>> page(
        @RequestBody PageRequestWrapper<EpisodeQueryRequest> request) {
        PageRequestWrapper<EpisodeQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(EpisodeMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<EpisodeVO> page = episodeService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(EpisodeMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
