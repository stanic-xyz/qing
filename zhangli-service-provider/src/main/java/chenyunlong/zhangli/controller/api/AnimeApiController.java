package chenyunlong.zhangli.controller.api;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.service.AnimeInfoService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Stan
 */
@Api(tags = "anime")
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
    public ApiResult addAnime(@Valid @RequestBody AnimeInfo animeInfo) {
        animeInfoService.add(animeInfo);
        return ApiResult.success();
    }

    @Log("获取视频详情")
    @GetMapping("detail/{aid}")
    public ApiResult<AnimeInfoVo> movie(@PathVariable(value = "aid") Long animeId) {
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
    public ApiResult<List<AnimeInfo>> listAnime(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                                @RequestParam(value = "animeName", required = false, defaultValue = "") String animeName,
                                                AnimeInfoQuery animeInfo) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return ApiResult.success(animeInfoService.query(pageRequest, animeInfo));
    }

    @Log("获取所有动漫信息")
    @GetMapping("update/page")
    public ApiResult<Page<AnimeInfo>> getUpdateInfo(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "24") Integer pageSize) {
        return ApiResult.success(animeInfoService.getUpdateAnimeInfo(page, pageSize));
    }

    @Log("删除视频信息")
    @DeleteMapping("deleteAnime")
    public ApiResult deleteAnime(@RequestParam("aid") Long animeId) {
        animeInfoService.deleteAnime(animeId);
        return ApiResult.success();
    }

    @Log("获取所有类型信息")
    @GetMapping("listTypes")
    public ApiResult<List<AnimeType>> getAnimeInfoService() {
        return ApiResult.success(animeInfoService.getAllAnimeType());
    }

    @Log("获取所有类型信息")
    @PostMapping("type/add")
    public ApiResult<AnimeType> addAnimeType(@Valid @RequestBody AnimeType animeType) {
        return ApiResult.success(animeInfoService.addAnimeType(animeType));
    }

}
