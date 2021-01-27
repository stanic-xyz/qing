package chenyunlong.zhangli.controller.api;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.service.AnimeRecommendService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "anime/update")
@RestController
public class AnimeUpdateController {
    @Autowired
    private AnimeRecommendService animeRecommendService;


    @GetMapping("list")
    public ApiResult<List<AnimeInfo>> getRecommendAnimeInfoList() {

        Pageable pageable = PageRequest.of(1, 20);
        return ApiResult.success(animeRecommendService.getRecommendAnimeInfoList(pageable));
    }
}
