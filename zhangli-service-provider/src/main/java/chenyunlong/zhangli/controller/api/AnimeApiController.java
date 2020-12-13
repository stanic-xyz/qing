package chenyunlong.zhangli.controller.api;

import chenyunlong.zhangli.annotation.Log;
import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.service.AnimeInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/anime")
public class AnimeApiController {

    private final AnimeInfoService animeInfoService;

    public AnimeApiController(AnimeInfoService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @Log("添加视频信息")
    @PostMapping("addAnime")
    public ApiResult addAnime(@Valid AnimeInfo animeInfo) {
        animeInfoService.add(animeInfo);
        return ApiResult.success();
    }

    @Log("获取视频详情")
    @GetMapping("detail/{aid}")
    public ApiResult<AnimeInfoVo> movie(@PathVariable(value = "aid", required = false) String animeId) {
        return ApiResult.success(animeInfoService.getMovieDetail(animeId));
    }

    @Log("修改视频信息")
    @PutMapping("updateAnime")
    public ApiResult updateAnime(@Valid @RequestBody AnimeInfo animeInfo) {
        animeInfoService.updateAnime(animeInfo);
        return ApiResult.success();
    }

    @Log("获取所有动漫信息")
    @GetMapping("listAnime")
    public ApiResult<List<AnimeInfo>> listAnime(@RequestParam("page") Integer page,
                                                @RequestParam("page") Integer pageSize,
                                                @RequestParam("animeName") String animeName) {
        return ApiResult.success(animeInfoService.query(animeName, page, pageSize));
    }

    @Log("删除视频信息")
    @DeleteMapping("deleteAnime")
    public ApiResult deleteAnime(@RequestParam("aid") Long animeId) {
        animeInfoService.deleteAnime(animeId);
        return ApiResult.success();
    }
}
