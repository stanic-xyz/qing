package cn.chenyunlong.qing.auth.interfaces.rest.v1.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.auth.app.service.UserTokenService;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserTokenCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserTokenCreateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.request.UserTokenUpdateRequest;
import cn.chenyunlong.qing.auth.domain.user.dto.updater.UserTokenUpdater;
import cn.chenyunlong.qing.infrastructure.auth.converter.UserTokenMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户令牌管理")
@RestController
@Slf4j
@RequestMapping("api/v1/user-token")
@RequiredArgsConstructor
public class UserTokenController {

    private final UserTokenService userTokenService;

    @PostMapping
    public JsonResult<Long> createUserToken(
        @RequestBody
        UserTokenCreateRequest request) {
        UserTokenCreator creator = UserTokenMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(userTokenService.createUserToken(creator));
    }

    @PostMapping("updateUserToken")
    public JsonResult<String> updateUserToken(
        @RequestBody
        UserTokenUpdateRequest request) {
        UserTokenUpdater updater = UserTokenMapper.INSTANCE.request2Updater(request);
        userTokenService.updateUserToken(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validUserToken(
        @PathVariable("id")
        Long id) {
        userTokenService.validUserToken(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidUserToken(
        @PathVariable("id")
        Long id) {
        userTokenService.invalidUserToken(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
