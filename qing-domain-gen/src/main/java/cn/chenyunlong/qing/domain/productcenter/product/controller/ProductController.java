package cn.chenyunlong.qing.domain.productcenter.product.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.product.dto.creator.ProductCreator;
import cn.chenyunlong.qing.domain.productcenter.product.dto.query.ProductQuery;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.response.ProductResponse;
import cn.chenyunlong.qing.domain.productcenter.product.dto.updater.ProductUpdater;
import cn.chenyunlong.qing.domain.productcenter.product.dto.vo.ProductVO;
import cn.chenyunlong.qing.domain.productcenter.product.mapper.ProductMapper;
import cn.chenyunlong.qing.domain.productcenter.product.service.IProductService;
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

@Tag(name = "产品管理", description = "产品管理接口")
@RestController
@Slf4j
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping
    public JsonResult<Long> createProduct(
        @RequestBody
        ProductCreateRequest request) {
        ProductCreator creator = ProductMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(productService.createProduct(creator));
    }

    @PostMapping("updateProduct")
    public JsonResult<String> updateProduct(@RequestBody ProductUpdateRequest request) {
        ProductUpdater updater = ProductMapper.INSTANCE.request2Updater(request);
        productService.updateProduct(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validProduct(
        @PathVariable
        Long id) {
        productService.validProduct(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidProduct(
        @PathVariable
        Long id) {
        productService.invalidProduct(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<ProductResponse> findById(@PathVariable Long id) {
        ProductVO vo = productService.findById(id);
        ProductResponse response = ProductMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<PageResult<ProductResponse>> page(
        @RequestBody
        PageRequestWrapper<ProductQueryRequest> request) {
        PageRequestWrapper<ProductQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(ProductMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<ProductVO> page = productService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(ProductMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
