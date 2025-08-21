package cn.chenyunlong.codegen.example.domain.controller;

import cn.chenyunlong.codegen.example.domain.creator.ProductCreator;
import cn.chenyunlong.codegen.example.domain.mapper.ProductMapper;
import cn.chenyunlong.codegen.example.domain.query.ProductQuery;
import cn.chenyunlong.codegen.example.domain.request.ProductCreateRequest;
import cn.chenyunlong.codegen.example.domain.request.ProductQueryRequest;
import cn.chenyunlong.codegen.example.domain.request.ProductUpdateRequest;
import cn.chenyunlong.codegen.example.domain.response.ProductResponse;
import cn.chenyunlong.codegen.example.domain.service.IProductService;
import cn.chenyunlong.codegen.example.domain.updater.ProductUpdater;
import cn.chenyunlong.codegen.example.domain.vo.ProductVO;
import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.common.AggregateId;
import java.lang.Long;
import java.lang.String;
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

@RestController
@Slf4j
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    /**
     * createProduct request
     */
    @PostMapping("create")
    public JsonResult<Long> createProduct(@RequestBody ProductCreateRequest request) {
        ProductCreator creator = ProductMapper.INSTANCE.request2Dto(request);return JsonResult.success(productService.createProduct(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateProduct")
    public JsonResult<String> updateProduct(@RequestBody ProductUpdateRequest request) {
        ProductUpdater updater = ProductMapper.INSTANCE.request2Updater(request);productService.updateProduct(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validProduct(@PathVariable AggregateId id) {
        productService.validProduct(id);return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidProduct(@PathVariable AggregateId id) {
        productService.invalidProduct(id);return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<ProductResponse> findById(@PathVariable AggregateId id) {
        ProductVO vo = productService.findById(id);ProductResponse response = ProductMapper.INSTANCE.vo2CustomResponse(vo);return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<ProductResponse>> page(
            @RequestBody PageRequestWrapper<ProductQueryRequest> request) {
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
