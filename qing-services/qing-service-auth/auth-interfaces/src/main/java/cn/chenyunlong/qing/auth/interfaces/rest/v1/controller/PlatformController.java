package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.application.service.PlatformService;
import cn.chenyunlong.qing.auth.domain.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.auth.domain.platform.dto.request.PlatformCreateRequest;
import cn.chenyunlong.qing.auth.domain.platform.dto.request.PlatformUpdateRequest;
import cn.chenyunlong.qing.auth.domain.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.auth.infrastructure.converter.PlatformMapper;
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

    private final PlatformService platformService;

    @PostMapping
    public JsonResult<Long> createPlatform(
        @RequestBody
        PlatformCreateRequest request) {
        PlatformCreator creator = PlatformMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(platformService.createPlatform(creator));
    }

    @PutMapping("/{id}")
    public JsonResult<String> updatePlatform(
            @PathVariable("id") Long id,
            @RequestBody PlatformUpdateRequest request) {
        request.setId(id);
        PlatformUpdater updater = PlatformMapper.INSTANCE.request2Updater(request);
        platformService.updatePlatform(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PatchMapping("/{id}/valid")
    public JsonResult<String> validPlatform(
        @PathVariable("id")
        Long id) {
        platformService.validPlatform(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PatchMapping("/{id}/invalid")
    public JsonResult<String> invalidPlatform(
        @PathVariable("id")
        Long id) {
        platformService.invalidPlatform(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
