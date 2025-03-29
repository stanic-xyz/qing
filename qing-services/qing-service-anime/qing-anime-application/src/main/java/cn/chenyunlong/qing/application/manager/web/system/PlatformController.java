package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformCreateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.request.PlatformUpdateRequest;
import cn.chenyunlong.qing.domain.auth.platform.dto.response.PlatformResponse;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;
import cn.chenyunlong.qing.domain.auth.platform.mapper.PlatformMapper;
import cn.chenyunlong.qing.domain.auth.platform.service.IPlatformService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "平台管理", description = "平台管理")
@RestController
@Slf4j
@RequestMapping("api/v1/platform")
@RequiredArgsConstructor
public class PlatformController {

    private final IPlatformService platformService;

    @PostMapping
    public JsonResult<Long> createPlatform(
        @RequestBody
        PlatformCreateRequest request) {
        PlatformCreator creator = PlatformMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(platformService.createPlatform(creator));
    }

    @PostMapping("updatePlatform")
    public JsonResult<String> updatePlatform(
        @RequestBody
        PlatformUpdateRequest request) {
        PlatformUpdater updater = PlatformMapper.INSTANCE.request2Updater(request);
        platformService.updatePlatform(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validPlatform(
        @PathVariable("id")
        Long id) {
        platformService.validPlatform(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidPlatform(
        @PathVariable("id")
        Long id) {
        platformService.invalidPlatform(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
