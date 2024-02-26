package cn.chenyunlong.qing.domain.productcenter.product.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.request.ProductUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.product.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "productClient",
    path = "product/v1"
)
public interface ProductFeignService {

    /**
     * 创建
     */
    @PostMapping("createProduct")
    JsonResult<Long> createProduct(@RequestBody ProductCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateProduct")
    JsonResult<String> updateProduct(@RequestBody ProductUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validProduct(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidProduct(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<ProductResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<ProductResponse>> page(
        @RequestBody
        PageRequestWrapper<ProductQueryRequest> request);
}
