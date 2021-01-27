package chenyunlong.zhangli.controller.api;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.entities.AnimeType;
import chenyunlong.zhangli.entities.District;
import chenyunlong.zhangli.entities.Version;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.service.AnimeTypeService;
import chenyunlong.zhangli.service.DistrictService;
import chenyunlong.zhangli.service.VersionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Stan
 */
@Validated
@Api(tags = "anime")
@RestController
@RequestMapping("api/district")
public class AnimeController {
    @Autowired
    private DistrictService districtService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private AnimeTypeService animeTypeService;

    public AnimeController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("list")
    public ApiResult<List<District>> getDistrictList() {
        return ApiResult.success(districtService.getAllDistrict());
    }

    @GetMapping("version/list")
    public ApiResult<List<Version>> getVersionList() {
        return ApiResult.success(versionService.getAllVersions());
    }

    @Log("获取所有类型信息")
    @GetMapping("type/list")
    public ApiResult<List<AnimeType>> getAnimeInfoService() {
        return ApiResult.success(animeTypeService.getAllTypeInfo());
    }

    @Log("获取所有类型信息")
    @PostMapping("type/add")
    public ApiResult<AnimeType> addAnimeType(@Valid @RequestBody AnimeType animeType) {
        animeTypeService.addAnimeType(animeType);
        return ApiResult.success();
    }

}
