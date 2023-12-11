package cn.chenyunlong.qing.domain.productcenter.productsku.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.request.ProductSkuUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.productsku.dto.response.ProductSkuResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "productSkuClient",
        path = "productSku/v1"
)
public interface ProductSkuFeignService {
    /**
     * 创建
     */
    @PostMapping("createProductSku")
    JsonResult<Long> createProductSku(@RequestBody ProductSkuCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateProductSku")
    JsonResult<String> updateProductSku(@RequestBody ProductSkuUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validProductSku(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidProductSku(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<ProductSkuResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<ProductSkuResponse>> page(
            @RequestBody PageRequestWrapper<ProductSkuQueryRequest> request);
}
