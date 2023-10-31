package cn.chenyunlong.qing.domain.zan.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanQueryRequest;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanUpdateRequest;
import cn.chenyunlong.qing.domain.zan.dto.response.ZanResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "zanClient",
    path = "zan/v1"
)
public interface ZanFeignService {
    /**
     * 创建
     */
    @PostMapping("createZan")
    JsonResult<Long> createZan(@RequestBody ZanCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateZan")
    JsonResult<String> updateZan(@RequestBody ZanUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validZan(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidZan(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<ZanResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<ZanResponse>> page(
        @RequestBody PageRequestWrapper<ZanQueryRequest> request);
}
