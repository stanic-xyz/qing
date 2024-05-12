package cn.chenyunlong.qing.domain.example.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.example.dto.creator.TestDomainCreator;
import cn.chenyunlong.qing.domain.example.dto.query.TestDomainQuery;
import cn.chenyunlong.qing.domain.example.dto.request.TestDomainCreateRequest;
import cn.chenyunlong.qing.domain.example.dto.request.TestDomainQueryRequest;
import cn.chenyunlong.qing.domain.example.dto.request.TestDomainUpdateRequest;
import cn.chenyunlong.qing.domain.example.dto.response.TestDomainResponse;
import cn.chenyunlong.qing.domain.example.dto.updater.TestDomainUpdater;
import cn.chenyunlong.qing.domain.example.dto.vo.TestDomainVO;
import cn.chenyunlong.qing.domain.example.mapper.TestDomainMapper;
import cn.chenyunlong.qing.domain.example.service.ITestDomainService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/v1/test-domain")
@RequiredArgsConstructor
public class TestDomainController {

    private final ITestDomainService testDomainService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createTestDomain(
        @RequestBody
        TestDomainCreateRequest request) {
        TestDomainCreator creator = TestDomainMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(testDomainService.createTestDomain(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateTestDomain")
    public JsonResult<String> updateTestDomain(
        @RequestBody
        TestDomainUpdateRequest request) {
        TestDomainUpdater updater = TestDomainMapper.INSTANCE.request2Updater(request);
        testDomainService.updateTestDomain(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validTestDomain(
        @PathVariable
        Long id) {
        testDomainService.validTestDomain(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidTestDomain(
        @PathVariable
        Long id) {
        testDomainService.invalidTestDomain(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<TestDomainResponse> findById(
        @PathVariable
        Long id) {
        TestDomainVO vo = testDomainService.findById(id);
        TestDomainResponse response = TestDomainMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<TestDomainResponse>> page(
        @RequestBody
        PageRequestWrapper<TestDomainQueryRequest> request) {
        PageRequestWrapper<TestDomainQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(TestDomainMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<TestDomainVO> page = testDomainService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(TestDomainMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
