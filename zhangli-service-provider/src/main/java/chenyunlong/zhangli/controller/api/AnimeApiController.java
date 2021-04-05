package chenyunlong.zhangli.controller.api;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoParam;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.support.ApiResult;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.service.AnimeInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
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
    @PostMapping
    public ApiResult<AnimeInfoVo> addAnime(@Valid @RequestBody AnimeInfoParam animeInfo) {
        AnimeInfoVo animeInfoVo = animeInfoService.create(animeInfo.convertTo());
        return ApiResult.success(animeInfoVo);
    }

    @Log("获取视频详情")
    @GetMapping("detail/{aid}")
    public ApiResult<AnimeInfoVo> movie(@PathVariable(value = "aid") Long animeId) {
        AnimeInfoVo animeInfoVo = animeInfoService.convertToDetailVo(animeInfoService.getById(animeId));
        //可以修改查看数据了
        return ApiResult.success(animeInfoVo);
    }

    @Log("修改视频信息")
    @PutMapping("{animeId:\\d+}")
    public ApiResult<AnimeInfoVo> updateBy(@PathVariable("animeId") Integer animeId,
                                           @Valid @RequestBody AnimeInfoParam animeInfoParam) {
        AnimeInfo animeInfoToUpdate = animeInfoService.getById(animeId);
        animeInfoParam.update(animeInfoToUpdate);
        return ApiResult.success(animeInfoService.updateBy(animeInfoToUpdate));
    }

    @Log("获取所有动漫信息")
    @GetMapping("listAnime")
    public ApiResult<List<AnimeInfoVo>> listAnime(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                                  @RequestParam(value = "animeName", required = false, defaultValue = "") String animeName,
                                                  AnimeInfoQuery animeInfo) {
        IPage<AnimeInfo> animeInfoIPage = new Page<>(page, pageSize);
        IPage<AnimeInfoVo> animeInfos = animeInfoService.listByPage(animeInfoIPage, animeInfo);
        return ApiResult.success(animeInfos.getRecords());
    }

    @Log("获取所有动漫信息")
    @GetMapping("update/page")
    public ApiResult<IPage<AnimeInfoVo>> getUpdateInfo(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "24") Integer pageSize) {
        IPage<AnimeInfoVo> animeInfoPage = animeInfoService.getUpdateAnimeInfo(new Page<>(page, pageSize));
        return ApiResult.success(animeInfoPage);
    }

    @Log("删除视频信息")
    @DeleteMapping("deleteAnime")
    public ApiResult<Void> deleteAnime(@RequestParam("aid") Long animeId) {
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
