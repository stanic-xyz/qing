package cn.chenyunlong.qing.domain.auth.platform.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformCreateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformQueryRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformUpdateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.response.PlatformResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "platformClient",
    path = "platform/v1"
)
public interface PlatformFeignService {

    /**
     * 创建
     */
    @PostMapping("createPlatform")
    JsonResult<Long> createPlatform(@RequestBody PlatformCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updatePlatform")
    JsonResult<String> updatePlatform(@RequestBody PlatformUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validPlatform(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidPlatform(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<PlatformResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<PlatformResponse>> page(
        @RequestBody
        PageRequestWrapper<PlatformQueryRequest> request);
}
