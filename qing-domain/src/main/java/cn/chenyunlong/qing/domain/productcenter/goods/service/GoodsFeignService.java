package cn.chenyunlong.qing.domain.productcenter.goods.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.request.GoodsUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.goods.dto.response.GoodsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "goodsClient",
    path = "goods/v1"
)
public interface GoodsFeignService {

    /**
     * 创建
     */
    @PostMapping("createGoods")
    JsonResult<Long> createGoods(@RequestBody GoodsCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateGoods")
    JsonResult<String> updateGoods(@RequestBody GoodsUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validGoods(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidGoods(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<GoodsResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<GoodsResponse>> page(
        @RequestBody
        PageRequestWrapper<GoodsQueryRequest> request);
}
