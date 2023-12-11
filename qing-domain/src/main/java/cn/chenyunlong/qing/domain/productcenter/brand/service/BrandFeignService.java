package cn.chenyunlong.qing.domain.productcenter.brand.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.request.BrandUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.brand.dto.response.BrandResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "brandClient",
        path = "brand/v1"
)
public interface BrandFeignService {
    /**
     * 创建
     */
    @PostMapping("createBrand")
    JsonResult<Long> createBrand(@RequestBody BrandCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateBrand")
    JsonResult<String> updateBrand(@RequestBody BrandUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validBrand(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidBrand(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<BrandResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<BrandResponse>> page(
            @RequestBody PageRequestWrapper<BrandQueryRequest> request);
}
