package cn.chenyunlong.zhangli.controller.api.admin;

import cn.chenyunlong.zhangli.core.ApiResult;
import cn.chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import cn.chenyunlong.zhangli.model.params.AnimeInfoQuery;
import cn.chenyunlong.zhangli.service.AnimeRecommendService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stan
 */
@Validated
@Api(tags = "api/anime/update")
@RestController
public class AnimeUpdateController {
    private final AnimeRecommendService animeRecommendService;

    public AnimeUpdateController(AnimeRecommendService animeRecommendService) {
        this.animeRecommendService = animeRecommendService;
    }


    @GetMapping("list")
    public ApiResult<List<AnimeInfo>> getRecommendAnimeInfoList(AnimeInfoQuery animeInfoQuery) {
        Pageable pageable = PageRequest.of(1, 20);
        animeRecommendService.getRecommendAnimeInfoList(pageable, animeInfoQuery);
        return ApiResult.success();
    }
}