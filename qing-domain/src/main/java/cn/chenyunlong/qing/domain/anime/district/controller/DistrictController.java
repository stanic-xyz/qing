package cn.chenyunlong.qing.domain.anime.district.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.district.dto.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.anime.district.dto.query.DistrictQuery;
import cn.chenyunlong.qing.domain.anime.district.dto.request.DistrictCreateRequest;
import cn.chenyunlong.qing.domain.anime.district.dto.request.DistrictQueryRequest;
import cn.chenyunlong.qing.domain.anime.district.dto.request.DistrictUpdateRequest;
import cn.chenyunlong.qing.domain.anime.district.dto.response.DistrictResponse;
import cn.chenyunlong.qing.domain.anime.district.dto.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.anime.district.dto.vo.DistrictVO;
import cn.chenyunlong.qing.domain.anime.district.mapper.DistrictConverter;
import cn.chenyunlong.qing.domain.anime.district.service.IDistrictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "地区管理")
@RestController
@Slf4j
@RequestMapping("api/v1/district")
@RequiredArgsConstructor
public class DistrictController {
    private final IDistrictService districtService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createDistrict(@RequestBody DistrictCreateRequest request) {
        DistrictCreator creator = DistrictConverter.INSTANCE.request2Dto(request);
        return JsonResult.success(districtService.createDistrict(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateDistrict")
    public JsonResult<String> updateDistrict(@RequestBody DistrictUpdateRequest request) {
        DistrictUpdater updater = DistrictConverter.INSTANCE.request2Updater(request);
        districtService.updateDistrict(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validDistrict(@PathVariable Long id) {
        districtService.validDistrict(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidDistrict(@PathVariable Long id) {
        districtService.invalidDistrict(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<DistrictResponse> findById(@PathVariable Long id) {
        DistrictVO vo = districtService.findById(id);
        DistrictResponse response = DistrictConverter.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<DistrictResponse>> page(
        @RequestBody PageRequestWrapper<DistrictQueryRequest> request) {
        PageRequestWrapper<DistrictQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(DistrictConverter.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<DistrictVO> page = districtService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                        .map(DistrictConverter.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
