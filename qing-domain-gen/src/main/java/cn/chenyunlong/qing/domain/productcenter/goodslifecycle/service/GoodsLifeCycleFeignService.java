package cn.chenyunlong.qing.domain.productcenter.goodslifecycle.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.request.GoodsLifeCycleUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.goodslifecycle.dto.response.GoodsLifeCycleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "goodsLifeCycleClient",
    path = "goodsLifeCycle/v1"
)
public interface GoodsLifeCycleFeignService {

    /**
     * 创建
     */
    @PostMapping("createGoodsLifeCycle")
    JsonResult<Long> createGoodsLifeCycle(@RequestBody GoodsLifeCycleCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateGoodsLifeCycle")
    JsonResult<String> updateGoodsLifeCycle(@RequestBody GoodsLifeCycleUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validGoodsLifeCycle(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidGoodsLifeCycle(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<GoodsLifeCycleResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<GoodsLifeCycleResponse>> page(
        @RequestBody
        PageRequestWrapper<GoodsLifeCycleQueryRequest> request);
}
