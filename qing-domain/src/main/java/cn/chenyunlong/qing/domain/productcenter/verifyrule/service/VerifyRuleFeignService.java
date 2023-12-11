package cn.chenyunlong.qing.domain.productcenter.verifyrule.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.response.VerifyRuleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "stanic",
        contextId = "verifyRuleClient",
        path = "verifyRule/v1"
)
public interface VerifyRuleFeignService {
    /**
     * 创建
     */
    @PostMapping("createVerifyRule")
    JsonResult<Long> createVerifyRule(@RequestBody VerifyRuleCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateVerifyRule")
    JsonResult<String> updateVerifyRule(@RequestBody VerifyRuleUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validVerifyRule(@PathVariable("id") Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidVerifyRule(@PathVariable("id") Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<VerifyRuleResponse> findById(@PathVariable("id") Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<VerifyRuleResponse>> page(
            @RequestBody PageRequestWrapper<VerifyRuleQueryRequest> request);
}
