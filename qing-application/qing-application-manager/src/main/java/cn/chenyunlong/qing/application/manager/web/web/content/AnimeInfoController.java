package cn.chenyunlong.qing.application.manager.web.web.content;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendDetailVO;
import cn.chenyunlong.qing.domain.anime.recommend.service.IRecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "前端业务查询", description = "前端业务查询")
@Slf4j
@RestController
@RequestMapping("api/query/v1/front")
@RequiredArgsConstructor
public class AnimeInfoController {

    private final IAnimeService animeService;
    private final IRecommendService recommendService;

    @Operation(summary = "查询最近更新的动漫")
    @GetMapping("queryAnimeUpdate")
    public JsonResult<List<AnimeVO>> animePage() {
        List<AnimeVO> page = animeService.queryLatestUpdate();
        return JsonResult.success(page);
    }

    @GetMapping("page")
    public JsonResult<List<RecommendDetailVO>> queryRecommend(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date) {
        List<RecommendDetailVO> recommendDetailVOList = recommendService.listCommendAnime(date);
        return JsonResult.success(
            recommendDetailVOList
        );
    }
}
