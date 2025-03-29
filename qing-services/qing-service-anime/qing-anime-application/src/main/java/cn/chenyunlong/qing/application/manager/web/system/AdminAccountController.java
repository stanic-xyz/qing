package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountCreateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountUpdateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.UserAuthPlatformsRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.UserRolesRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.domain.auth.admin.mapper.AdminAccountMapper;
import cn.chenyunlong.qing.domain.auth.admin.service.IAdminAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员账号管理")
@RestController
@Slf4j
@RequestMapping("api/v1/admin-account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final IAdminAccountService adminAccountService;

    @PostMapping
    public JsonResult<Long> createAdminAccount(
        @RequestBody
        AdminAccountCreateRequest request) {
        AdminAccountCreator creator = AdminAccountMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(adminAccountService.createAdminAccount(creator));
    }

    @PostMapping("updateAdminAccount")
    public JsonResult<String> updateAdminAccount(
        @RequestBody
        AdminAccountUpdateRequest request) {
        AdminAccountUpdater updater = AdminAccountMapper.INSTANCE.request2Updater(request);
        adminAccountService.updateAdminAccount(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validAdminAccount(
        @PathVariable("id")
        Long id) {
        adminAccountService.validAdminAccount(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidAdminAccount(
        @PathVariable("id")
        Long id) {
        adminAccountService.invalidAdminAccount(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * 为用户分配角色
     */
    @PostMapping(value = "assignRolesToUser")
    public JsonResult<String> assignRolesToUser(
        @RequestBody
        UserRolesRequest request) {
        adminAccountService.assignRolesToUser(request.getAccountId(), request.getRoleIds());
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * 为用户分配平台
     */
    @PostMapping("assignPlatformsToUser")
    public JsonResult<String> assignPlatformsToUser(
        @RequestBody
        UserAuthPlatformsRequest request) {
        adminAccountService.assignPlatformsToUser(request.getPlatformIds(), request.getAccountId());
        return JsonResult.success(CodeEnum.Success.getName());
    }
}
