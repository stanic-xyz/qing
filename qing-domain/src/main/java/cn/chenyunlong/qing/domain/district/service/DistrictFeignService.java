package cn.chenyunlong.qing.domain.district.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.district.dto.request.DistrictCreateRequest;
import cn.chenyunlong.qing.domain.district.dto.request.DistrictQueryRequest;
import cn.chenyunlong.qing.domain.district.dto.request.DistrictUpdateRequest;
import cn.chenyunlong.qing.domain.district.dto.response.DistrictResponse;
import java.lang.Long;
import java.lang.String;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic",
    contextId = "districtClient",
    path = "district/v1"
)
public interface DistrictFeignService {
    /**
     * 创建
     */
    @PostMapping("createDistrict")
    JsonResult<Long> createDistrict(@RequestBody DistrictCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateDistrict")
    JsonResult<String> updateDistrict(@RequestBody DistrictUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validDistrict(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidDistrict(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<DistrictResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<DistrictResponse>> page(
        @RequestBody PageRequestWrapper<DistrictQueryRequest> request);
}
