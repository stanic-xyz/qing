package cn.chenyunlong.qing.application.manager.web.anime;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.application.manager.web.anime.query.AnimeQueryService;
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

@Tag(name = "地区管理")
@RestController
@Slf4j
@RequestMapping("api/v1/district")
@RequiredArgsConstructor
public class DistrictController {

    private final IDistrictService districtService;
    private final AnimeQueryService animeQueryService;

    @PostMapping
    public JsonResult<Long> createDistrict(
        @RequestBody
        DistrictCreateRequest request) {
        DistrictCreator creator = DistrictConverter.INSTANCE.request2Dto(request);
        return JsonResult.success(districtService.createDistrict(creator));
    }

    @PutMapping("{id}")
    public JsonResult<String> updateDistrict(
        @PathVariable("id")
        Long id,
        @RequestBody
        DistrictUpdateRequest request) {
        DistrictUpdater updater = DistrictConverter.INSTANCE.request2Updater(request);
        districtService.updateDistrict(id, updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validDistrict(
        @PathVariable("id")
        Long id) {
        districtService.validDistrict(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidDistrict(
        @PathVariable("id")
        Long id) {
        districtService.invalidDistrict(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("findById/{id}")
    public JsonResult<DistrictResponse> findById(
        @PathVariable("id")
        Long id) {
        DistrictVO vo = districtService.findById(id);
        DistrictResponse response = DistrictConverter.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @DeleteMapping("{id}")
    public JsonResult<Void> deleteById(
        @PathVariable("id")
        Long id) {
        districtService.deleteById(id);
        return JsonResult.success();
    }
}
