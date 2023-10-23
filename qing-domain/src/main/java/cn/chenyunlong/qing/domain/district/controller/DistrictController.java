// ---Auto Generated by Qing-Generator --

package cn.chenyunlong.qing.domain.district.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.district.creator.DistrictCreator;
import cn.chenyunlong.qing.domain.district.mapper.DistrictMapper;
import cn.chenyunlong.qing.domain.district.query.DistrictQuery;
import cn.chenyunlong.qing.domain.district.request.DistrictCreateRequest;
import cn.chenyunlong.qing.domain.district.request.DistrictQueryRequest;
import cn.chenyunlong.qing.domain.district.request.DistrictUpdateRequest;
import cn.chenyunlong.qing.domain.district.response.DistrictResponse;
import cn.chenyunlong.qing.domain.district.service.IDistrictService;
import cn.chenyunlong.qing.domain.district.updater.DistrictUpdater;
import cn.chenyunlong.qing.domain.district.vo.DistrictVO;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
        DistrictCreator creator = DistrictMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(districtService.createDistrict(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateDistrict")
    public JsonResult<String> updateDistrict(@RequestBody DistrictUpdateRequest request) {
        DistrictUpdater updater = DistrictMapper.INSTANCE.request2Updater(request);
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
        DistrictResponse response = DistrictMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<DistrictResponse>> page(
        @RequestBody PageRequestWrapper<DistrictQueryRequest> request) {
        PageRequestWrapper<DistrictQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(DistrictMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<DistrictVO> page = districtService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(DistrictMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}