package cn.chenyunlong.zhangli.controller.api.admin;

import cn.chenyunlong.zhangli.core.ApiResult;
import cn.chenyunlong.zhangli.model.entities.AnimeRecommendEntity;
import cn.chenyunlong.zhangli.model.params.AnimeInfoQuery;
import cn.chenyunlong.zhangli.model.params.AnimeRecommendParam;
import cn.chenyunlong.zhangli.service.AnimeRecommendService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/admin/recommend")
public class AnimeRecommendController {

    private final AnimeRecommendService recommendService;


    public AnimeRecommendController(AnimeRecommendService recommendService) {
        this.recommendService = recommendService;
    }

    @ApiOperation("Gets animeType detail by id")
    @GetMapping("{recommendId:\\d+}")
    public ApiResult<AnimeRecommendEntity> getAnimeTypeInfo(@PathVariable("recommendId") Long recommendId) {
        return ApiResult.success(recommendService.getById(recommendId));
    }

    @GetMapping("page")
    public ApiResult<IPage<AnimeRecommendEntity>> pageBy(AnimeRecommendParam typeParam, Pageable pageable) {
        return ApiResult.success(recommendService.pageBy(typeParam, pageable));
    }

    @GetMapping()
    public ApiResult<List<AnimeRecommendEntity>> getAnimeInfoService(AnimeInfoQuery typeParam, Pageable pageable) {
        return ApiResult.success(recommendService.getRecommendAnimeInfoList(pageable, typeParam));
    }

    @PostMapping()
    public ApiResult<AnimeRecommendEntity> addAnimeType(@Valid @RequestBody AnimeRecommendParam recommendParam) {
        AnimeRecommendEntity animeType = recommendService.addRecommend(recommendParam.convertTo());
        return ApiResult.success(animeType);
    }


    @PutMapping("{typeId:\\d+")
    public ApiResult<AnimeRecommendEntity> modifyAnimeType(@PathVariable("typeId") Long typeId,
                                                           @Valid @RequestBody AnimeRecommendParam typeParam) {
        //查询动漫信息
        AnimeRecommendEntity animeRecommendEntity = recommendService.getById(typeId);
        //更新类型信息
        typeParam.update(animeRecommendEntity);

        return ApiResult.success(recommendService.update(animeRecommendEntity));
    }
}
