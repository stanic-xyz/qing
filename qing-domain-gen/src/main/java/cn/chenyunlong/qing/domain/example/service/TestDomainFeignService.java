package cn.chenyunlong.qing.domain.example.service;

import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.example.dto.request.TestDomainCreateRequest;
import cn.chenyunlong.qing.domain.example.dto.request.TestDomainQueryRequest;
import cn.chenyunlong.qing.domain.example.dto.request.TestDomainUpdateRequest;
import cn.chenyunlong.qing.domain.example.dto.response.TestDomainResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    value = "stanic-feign-client",
    contextId = "testDomainClient",
    path = "testDomain/v1"
)
public interface TestDomainFeignService {

    /**
     * 创建
     */
    @PostMapping("createTestDomain")
    JsonResult<Long> createTestDomain(
        @RequestBody
        TestDomainCreateRequest request);

    /**
     * 更新请求
     */
    @PostMapping("updateTestDomain")
    JsonResult<String> updateTestDomain(
        @RequestBody
        TestDomainUpdateRequest request);

    /**
     * 有效
     */
    @PostMapping("valid/{id}")
    JsonResult<String> validTestDomain(
        @PathVariable("id")
        Long id);

    /**
     * 无效
     */
    @PostMapping("invalid/{id}")
    JsonResult<String> invalidTestDomain(
        @PathVariable("id")
        Long id);

    /**
     * 根据ID查询
     */
    @GetMapping("findById/{id}")
    JsonResult<TestDomainResponse> findById(
        @PathVariable("id")
        Long id);

    /**
     * 分页查询
     */
    @PostMapping("findByPage")
    JsonResult<PageResult<TestDomainResponse>> page(
        @RequestBody
        PageRequestWrapper<TestDomainQueryRequest> request);
}
