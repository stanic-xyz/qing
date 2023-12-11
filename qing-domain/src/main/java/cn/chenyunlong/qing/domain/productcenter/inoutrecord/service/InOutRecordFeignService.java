package cn.chenyunlong.qing.domain.productcenter.inoutrecord.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response.InOutRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "inOutRecordClient",
        path = "inOutRecord/v1"
)
public interface InOutRecordFeignService {
    /**
     * 创建
     */
    @PostMapping("createInOutRecord")
    JsonResult<Long> createInOutRecord(@RequestBody InOutRecordCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateInOutRecord")
    JsonResult<String> updateInOutRecord(@RequestBody InOutRecordUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validInOutRecord(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidInOutRecord(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<InOutRecordResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<InOutRecordResponse>> page(
            @RequestBody PageRequestWrapper<InOutRecordQueryRequest> request);
}
