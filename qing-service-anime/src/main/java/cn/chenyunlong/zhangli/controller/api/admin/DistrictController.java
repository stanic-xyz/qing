package cn.chenyunlong.zhangli.controller.api.admin;

import cn.chenyunlong.zhangli.controller.base.ApiController;
import cn.chenyunlong.zhangli.core.ApiResult;
import cn.chenyunlong.zhangli.model.dto.DistrictDTO;
import cn.chenyunlong.zhangli.service.DistrictService;
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
@Api(tags = "区域控制器")
@RestController
@RequestMapping("api/districts")
public class DistrictController extends ApiController {
    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public ApiResult<List<DistrictDTO>> getDistrictList() {
        return success(districtService.getAllDistrict());
    }
}
