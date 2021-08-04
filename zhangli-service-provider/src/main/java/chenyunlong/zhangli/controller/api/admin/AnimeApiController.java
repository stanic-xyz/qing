package chenyunlong.zhangli.controller.api.admin;

import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoParam;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.service.AnimeInfoService;
import cn.hutool.core.thread.NamedThreadFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stan.zhangli.core.core.support.ApiResult;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Stan
 */
@Api(tags = "anime")
@Validated
@RestController
@RequestMapping("api/admin/anime")
public class AnimeApiController {

    private final AnimeInfoService animeInfoService;

    public AnimeApiController(AnimeInfoService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @PostMapping
    public ApiResult<AnimeInfoVo> addAnime(@Valid @RequestBody AnimeInfoParam animeInfo) {
        AnimeInfoVo animeInfoVo = animeInfoService.create(animeInfo.convertTo());
        return ApiResult.success(animeInfoVo);
    }

    @GetMapping("detail/{aid}")
    public ApiResult<AnimeInfoVo> movie(@PathVariable(value = "aid") Long animeId) {
        AnimeInfoVo animeInfoVo = animeInfoService.convertToDetailVo(animeInfoService.getById(animeId));
        //可以修改查看数据了
        return ApiResult.success(animeInfoVo);
    }

    @PutMapping("{animeId:\\d+}")
    public ApiResult<AnimeInfoVo> updateBy(@PathVariable("animeId") Integer animeId,
                                           @Valid @RequestBody AnimeInfoParam animeInfoParam) {
        AnimeInfo animeInfoToUpdate = animeInfoService.getById(animeId);
        animeInfoParam.update(animeInfoToUpdate);
        return ApiResult.success(animeInfoService.updateBy(animeInfoToUpdate));
    }

    @GetMapping("listAnime")
    public ApiResult<IPage<AnimeInfoVo>> listAnime(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                                   AnimeInfoQuery animeInfo) {
        IPage<AnimeInfo> animeInfoPage = new Page<>(page, pageSize);
        IPage<AnimeInfoVo> animeInfos = animeInfoService.listByPage(animeInfoPage, animeInfo);
        return ApiResult.success(animeInfos);
    }

    @GetMapping("update/page")
    public ApiResult<IPage<AnimeInfoMinimalDTO>> getUpdateInfo(@RequestParam(defaultValue = "1") Integer page,
                                                               @RequestParam(defaultValue = "24") Integer pageSize) {
        IPage<AnimeInfoMinimalDTO> animeInfoPage = animeInfoService.getUpdateAnimeInfo(new Page<>(page, pageSize));
        return ApiResult.success(animeInfoPage);
    }

    @DeleteMapping("deleteAnime")
    public ApiResult<Void> deleteAnime(@RequestParam("aid") Long animeId) {
        animeInfoService.deleteAnime(animeId);
        return ApiResult.success();
    }


    @PostMapping("img/downloadImages")
    public ApiResult<Void> downloadImages() throws IOException {
        ExecutorService executor = new ScheduledThreadPoolExecutor(2, new NamedThreadFactory("zhangli-", false));
        executor.execute(() -> {
            try {
                animeInfoService.downloadImages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return ApiResult.success();
    }
}
