package cn.chenyunlong.qing.domain.attachement.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.attachement.dto.request.AttachmentCreateRequest;
import cn.chenyunlong.qing.domain.attachement.dto.request.AttachmentQueryRequest;
import cn.chenyunlong.qing.domain.attachement.dto.request.AttachmentUpdateRequest;
import cn.chenyunlong.qing.domain.attachement.dto.response.AttachmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "attachmentClient",
    path = "attachment/v1"
)
public interface AttachmentFeignService {
    /**
     * 创建
     */
    @PostMapping("createAttachment")
    JsonResult<Long> createAttachment(@RequestBody AttachmentCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateAttachment")
    JsonResult<String> updateAttachment(@RequestBody AttachmentUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validAttachment(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidAttachment(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<AttachmentResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<AttachmentResponse>> page(
        @RequestBody PageRequestWrapper<AttachmentQueryRequest> request);
}
