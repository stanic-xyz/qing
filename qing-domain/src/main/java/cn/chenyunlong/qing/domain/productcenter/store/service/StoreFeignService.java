package cn.chenyunlong.qing.domain.productcenter.store.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.request.StoreUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.store.dto.response.StoreResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "storeClient",
    path = "store/v1"
)
public interface StoreFeignService {

    /**
     * 创建
     */
    @PostMapping("createStore")
    JsonResult<Long> createStore(@RequestBody StoreCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateStore")
    JsonResult<String> updateStore(@RequestBody StoreUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validStore(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidStore(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<StoreResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<StoreResponse>> page(
        @RequestBody
        PageRequestWrapper<StoreQueryRequest> request);
}
