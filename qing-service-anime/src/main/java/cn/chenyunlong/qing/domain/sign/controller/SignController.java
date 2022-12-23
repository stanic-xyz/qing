// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.sign.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonObject;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.sign.creator.SignCreator;
import cn.chenyunlong.qing.domain.sign.mapper.SignMapper;
import cn.chenyunlong.qing.domain.sign.query.SignQuery;
import cn.chenyunlong.qing.domain.sign.request.SignCreateRequest;
import cn.chenyunlong.qing.domain.sign.request.SignQueryRequest;
import cn.chenyunlong.qing.domain.sign.request.SignUpdateRequest;
import cn.chenyunlong.qing.domain.sign.response.SignResponse;
import cn.chenyunlong.qing.domain.sign.service.ISignService;
import cn.chenyunlong.qing.domain.sign.updater.SignUpdater;
import cn.chenyunlong.qing.domain.sign.vo.SignVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("sign/v1")
@RequiredArgsConstructor
public class SignController {
    private final ISignService signService;

    /**
     * createRequest
     */
    @PostMapping("createSign")
    public JsonObject<Long> createSign(@RequestBody SignCreateRequest request) {
        SignCreator creator = SignMapper.INSTANCE.request2Dto(request);
        return JsonObject.success(signService.createSign(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateSign")
    public JsonObject<String> updateSign(@RequestBody SignUpdateRequest request) {
        SignUpdater updater = SignMapper.INSTANCE.request2Updater(request);
        signService.updateSign(updater);
        return JsonObject.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonObject<String> validSign(@PathVariable Long id) {
        signService.validSign(id);
        return JsonObject.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonObject<String> invalidSign(@PathVariable Long id) {
        signService.invalidSign(id);
        return JsonObject.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonObject<SignResponse> findById(@PathVariable Long id) {
        SignVO vo = signService.findById(id);
        SignResponse response = SignMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonObject.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("findByPage")
    public JsonObject<PageResult<SignResponse>> findByPage(
            @RequestBody PageRequestWrapper<SignQueryRequest> request) {
        PageRequestWrapper<SignQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(SignMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<SignVO> page = signService.findByPage(wrapper);
        return JsonObject.success(
                PageResult.of(
                        page.getContent().stream()
                                .map(vo -> SignMapper.INSTANCE.vo2CustomResponse(vo))
                                .collect(Collectors.toList()),
                        page.getTotalElements(),
                        page.getSize(),
                        page.getNumber())
        );
    }
}