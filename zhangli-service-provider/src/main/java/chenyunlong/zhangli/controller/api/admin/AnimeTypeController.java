package chenyunlong.zhangli.controller.api.admin;

import chenyunlong.zhangli.core.ApiResult;
import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.params.AnimeTypeParam;
import chenyunlong.zhangli.service.AnimeTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/types")
public class AnimeTypeController {

    private final AnimeTypeService typeService;

    public AnimeTypeController(AnimeTypeService typeService) {
        this.typeService = typeService;
    }

    @ApiOperation("Gets animeType detail by id")
    @GetMapping("{typeId:\\d+}")
    public ApiResult<AnimeType> getAnimeTypeInfo(@PathVariable("typeId") Long typeId) {
        return ApiResult.success(typeService.getById(typeId));
    }

    @GetMapping("page")
    public ApiResult<IPage<AnimeType>> pageBy(AnimeTypeParam typeParam, Pageable pageable) {
        return ApiResult.success(typeService.pageBy(typeParam, pageable));
    }

    @GetMapping()
    public ApiResult<List<AnimeType>> getAnimeInfoService() {
        return ApiResult.success(typeService.getAllTypeInfo());
    }

    @PostMapping()
    public ApiResult<AnimeType> addAnimeType(@Valid @RequestBody AnimeTypeParam typeParam) {
        AnimeType animeType = typeService.addAnimeType(typeParam.convertTo());
        return ApiResult.success(animeType);
    }


    @PutMapping("{typeId:\\d+")
    public ApiResult<AnimeType> modifyAnimeType(@PathVariable("typeId") Long typeId,
                                                @Valid @RequestBody AnimeTypeParam typeParam) {
        //查询动漫信息
        AnimeType typeInfo = typeService.getById(typeId);

        //更新类型信息
        typeParam.update(typeInfo);

        return ApiResult.success(typeService.update(typeInfo));
    }
}
