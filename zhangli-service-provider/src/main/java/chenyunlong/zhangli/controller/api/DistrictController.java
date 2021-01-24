package chenyunlong.zhangli.controller.api;

import chenyunlong.zhangli.common.service.DistrictService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stan
 */
@Validated
@RestController
@RequestMapping("api/district")
public class DistrictController {
    private final DistrictService districtService;


    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }
}
