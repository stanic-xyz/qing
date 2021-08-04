package chenyunlong.zhangli.controller.api.admin;

import chenyunlong.zhangli.core.ApiResult;
import chenyunlong.zhangli.model.entities.District;
import chenyunlong.zhangli.model.entities.Version;
import chenyunlong.zhangli.service.DistrictService;
import chenyunlong.zhangli.service.VersionService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Stan
 */
@Validated
@Api(tags = "anime")
@RestController
@RequestMapping("api/district")
public class AnimeController {
    private final DistrictService districtService;
    private final VersionService versionService;

    public AnimeController(DistrictService districtService, VersionService versionService) {
        this.districtService = districtService;
        this.versionService = versionService;
    }

    @GetMapping("list")
    public ApiResult<List<District>> getDistrictList() {
        return ApiResult.success(districtService.getAllDistrict());
    }

    @GetMapping("version/list")
    public ApiResult<List<Version>> getVersionList() {
        return ApiResult.success(versionService.getAllVersions());
    }
}
