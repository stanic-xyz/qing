package cn.chenyunlong.zhangli.controller.api.admin;

import cn.chenyunlong.zhangli.core.ApiResult;
import cn.chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.zhangli.model.params.AddEpisodeParam;
import cn.chenyunlong.zhangli.service.AnimeEpisodeService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Api(tags = "api/anime/episode")
@RestController
public class EpisodeController {

    private final AnimeEpisodeService episodeService;

    public EpisodeController(AnimeEpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @PostMapping("")
    public ApiResult<AnimeEpisodeDTO> add(@Validated @RequestBody AddEpisodeParam episodeParam) {
        return ApiResult.success(episodeService.add(episodeParam));
    }
}