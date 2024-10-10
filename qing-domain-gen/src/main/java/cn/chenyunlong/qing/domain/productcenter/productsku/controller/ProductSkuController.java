package cn.chenyunlong.qing.domain.productcenter.productsku.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.creator.ProductSkuCreator;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.query.ProductSkuQuery;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.response.ProductSkuResponse;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.updater.ProductSkuUpdater;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.vo.ProductSkuVO;
import cn.chenyunlong.qing.domain.productcenter.productsku.mapper.ProductSkuMapper;
import cn.chenyunlong.qing.domain.productcenter.productsku.service.IProductSkuService;
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

@Tag(name = "商品SKU", description = "商品SKU接口")
@RestController
@Slf4j
@RequestMapping("api/v1/product-sku")
@RequiredArgsConstructor
public class ProductSkuController {

    private final IProductSkuService productSkuService;

    @PostMapping
    public JsonResult<Long> createProductSku(
        @RequestBody
        ProductSkuCreateRequest request) {
        ProductSkuCreator creator = ProductSkuMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(productSkuService.createProductSku(creator));
    }

    @PostMapping("updateProductSku")
    public JsonResult<String> updateProductSku(@RequestBody ProductSkuUpdateRequest request) {
        ProductSkuUpdater updater = ProductSkuMapper.INSTANCE.request2Updater(request);
        productSkuService.updateProductSku(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validProductSku(
        @PathVariable
        Long id) {
        productSkuService.validProductSku(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidProductSku(
        @PathVariable
        Long id) {
        productSkuService.invalidProductSku(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<ProductSkuResponse> findById(@PathVariable Long id) {
        ProductSkuVO vo = productSkuService.findById(id);
        ProductSkuResponse response = ProductSkuMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<ProductSkuResponse>> page(
        @RequestBody
        PageRequestWrapper<ProductSkuQueryRequest> request) {
        PageRequestWrapper<ProductSkuQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(ProductSkuMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<ProductSkuVO> page = productSkuService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(ProductSkuMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
