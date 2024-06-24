package cn.chenyunlong.qing.domain.anime.type.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeCreateRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeQueryRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.request.TypeUpdateRequest;
import cn.chenyunlong.qing.domain.anime.type.dto.response.TypeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "typeClient",
    path = "type/v1"
)
public interface TypeFeignService {

    /**
     * 创建
     */
    @PostMapping("createType")
    JsonResult<Long> createType(@RequestBody TypeCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateType")
    JsonResult<String> updateType(@RequestBody TypeUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validType(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidType(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<TypeResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<TypeResponse>> page(
        @RequestBody PageRequestWrapper<TypeQueryRequest> request);
}
