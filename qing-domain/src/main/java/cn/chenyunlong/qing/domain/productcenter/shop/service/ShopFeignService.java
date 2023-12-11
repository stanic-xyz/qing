package cn.chenyunlong.qing.domain.productcenter.shop.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.request.ShopUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.shop.dto.response.ShopResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "shopClient",
        path = "shop/v1"
)
public interface ShopFeignService {
    /**
     * 创建
     */
    @PostMapping("createShop")
    JsonResult<Long> createShop(@RequestBody ShopCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateShop")
    JsonResult<String> updateShop(@RequestBody ShopUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validShop(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidShop(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<ShopResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<ShopResponse>> page(
            @RequestBody PageRequestWrapper<ShopQueryRequest> request);
}
