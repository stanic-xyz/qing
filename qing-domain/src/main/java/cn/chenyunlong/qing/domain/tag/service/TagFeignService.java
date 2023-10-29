package cn.chenyunlong.qing.domain.tag.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.tag.dto.request.TagCreateRequest;
import cn.chenyunlong.qing.domain.tag.dto.request.TagQueryRequest;
import cn.chenyunlong.qing.domain.tag.dto.request.TagUpdateRequest;
import cn.chenyunlong.qing.domain.tag.dto.response.TagResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "tagClient",
    path = "tag/v1"
)
public interface TagFeignService {
    /**
     * 创建
     */
    @PostMapping("createTag")
    JsonResult<Long> createTag(@RequestBody TagCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateTag")
    JsonResult<String> updateTag(@RequestBody TagUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validTag(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidTag(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<TagResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<TagResponse>> page(
        @RequestBody PageRequestWrapper<TagQueryRequest> request);
}
