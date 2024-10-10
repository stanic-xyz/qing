package cn.chenyunlong.qing.domain.productcenter.brand.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.creator.BrandCreator;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.query.BrandQuery;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.response.BrandResponse;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.updater.BrandUpdater;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.vo.BrandVO;
import cn.chenyunlong.qing.domain.productcenter.brand.mapper.BrandMapper;
import cn.chenyunlong.qing.domain.productcenter.brand.service.IBrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "品牌管理")
@RestController
@Slf4j
@RequestMapping("api/v1/brand")
@RequiredArgsConstructor
public class BrandController {

    private final IBrandService brandService;

    @PostMapping
    public JsonResult<Long> createBrand(
        @RequestBody
        BrandCreateRequest request) {
        BrandCreator creator = BrandMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(brandService.createBrand(creator));
    }

    @PostMapping("updateBrand")
    public JsonResult<String> updateBrand(@RequestBody BrandUpdateRequest request) {
        BrandUpdater updater = BrandMapper.INSTANCE.request2Updater(request);
        brandService.updateBrand(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validBrand(
        @PathVariable
        Long id) {
        brandService.validBrand(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidBrand(
        @PathVariable
        Long id) {
        brandService.invalidBrand(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<BrandResponse> findById(@PathVariable Long id) {
        BrandVO vo = brandService.findById(id);
        BrandResponse response = BrandMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<BrandResponse>> page(
        @RequestBody
        PageRequestWrapper<BrandQueryRequest> request) {
        PageRequestWrapper<BrandQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(BrandMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<BrandVO> page = brandService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(BrandMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
