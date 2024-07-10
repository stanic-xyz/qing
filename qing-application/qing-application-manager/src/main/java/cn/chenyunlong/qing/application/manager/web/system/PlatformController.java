package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.query.PlatformQuery;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformCreateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformQueryRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformUpdateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.response.PlatformResponse;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;
import cn.chenyunlong.qing.domain.auth.platform.mapper.PlatformMapper;
import cn.chenyunlong.qing.domain.auth.platform.service.IPlatformService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "平台管理", description = "平台管理")
@RestController
@Slf4j
@RequestMapping("api/v1/platform")
@RequiredArgsConstructor
public class PlatformController {

    private final IPlatformService platformService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createPlatform(
        @RequestBody
        PlatformCreateRequest request) {
        PlatformCreator creator = PlatformMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(platformService.createPlatform(creator));
    }

    /**
     * update request
     */
    @PostMapping("updatePlatform")
    public JsonResult<String> updatePlatform(
        @RequestBody
        PlatformUpdateRequest request) {
        PlatformUpdater updater = PlatformMapper.INSTANCE.request2Updater(request);
        platformService.updatePlatform(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validPlatform(
        @PathVariable("id")
        Long id) {
        platformService.validPlatform(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidPlatform(
        @PathVariable("id")
        Long id) {
        platformService.invalidPlatform(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<PlatformResponse> findById(
        @PathVariable("id")
        Long id) {
        PlatformVO vo = platformService.findById(id);
        PlatformResponse response = PlatformMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<PlatformResponse>> page(
        @RequestBody
        PageRequestWrapper<PlatformQueryRequest> request) {
        PageRequestWrapper<PlatformQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(PlatformMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<PlatformVO> page = platformService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(PlatformMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
