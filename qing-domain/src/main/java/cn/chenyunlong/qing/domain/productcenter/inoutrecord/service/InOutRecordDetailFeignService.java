package cn.chenyunlong.qing.domain.productcenter.inoutrecord.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.request.InOutRecordDetailUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.response.InOutRecordDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "inOutRecordDetailClient",
        path = "inOutRecordDetail/v1"
)
public interface InOutRecordDetailFeignService {
    /**
     * 创建
     */
    @PostMapping("createInOutRecordDetail")
    JsonResult<Long> createInOutRecordDetail(@RequestBody InOutRecordDetailCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateInOutRecordDetail")
    JsonResult<String> updateInOutRecordDetail(@RequestBody InOutRecordDetailUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validInOutRecordDetail(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidInOutRecordDetail(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<InOutRecordDetailResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<InOutRecordDetailResponse>> page(
            @RequestBody PageRequestWrapper<InOutRecordDetailQueryRequest> request);
}
