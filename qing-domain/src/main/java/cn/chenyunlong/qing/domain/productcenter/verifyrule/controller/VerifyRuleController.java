package cn.chenyunlong.qing.domain.productcenter.verifyrule.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.creator.VerifyRuleCreator;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.query.VerifyRuleQuery;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleCreateRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleQueryRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request.VerifyRuleUpdateRequest;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.response.VerifyRuleResponse;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.updater.VerifyRuleUpdater;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.vo.VerifyRuleVO;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.mapper.VerifyRuleMapper;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.service.IVerifyRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("api/v1/verify-rule")
@RequiredArgsConstructor
public class VerifyRuleController {
    private final IVerifyRuleService verifyRuleService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createVerifyRule(@RequestBody VerifyRuleCreateRequest request) {
        VerifyRuleCreator creator = VerifyRuleMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(verifyRuleService.createVerifyRule(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateVerifyRule")
    public JsonResult<String> updateVerifyRule(@RequestBody VerifyRuleUpdateRequest request) {
        VerifyRuleUpdater updater = VerifyRuleMapper.INSTANCE.request2Updater(request);
        verifyRuleService.updateVerifyRule(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validVerifyRule(@PathVariable Long id) {
        verifyRuleService.validVerifyRule(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidVerifyRule(@PathVariable Long id) {
        verifyRuleService.invalidVerifyRule(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<VerifyRuleResponse> findById(@PathVariable Long id) {
        VerifyRuleVO vo = verifyRuleService.findById(id);
        VerifyRuleResponse response = VerifyRuleMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<VerifyRuleResponse>> page(
            @RequestBody PageRequestWrapper<VerifyRuleQueryRequest> request) {
        PageRequestWrapper<VerifyRuleQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(VerifyRuleMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<VerifyRuleVO> page = verifyRuleService.findByPage(wrapper);
        return JsonResult.success(
                PageResult.of(
                        page.getContent().stream()
                                .map(VerifyRuleMapper.INSTANCE::vo2CustomResponse)
                                .collect(Collectors.toList()),
                        page.getTotalElements(),
                        page.getSize(),
                        page.getNumber())
        );
    }
}
