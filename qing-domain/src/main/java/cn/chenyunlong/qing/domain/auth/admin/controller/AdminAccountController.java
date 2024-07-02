package cn.chenyunlong.qing.domain.auth.admin.controller;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.auth.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.auth.admin.dto.query.AdminAccountQuery;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountCreateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountQueryRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.AdminAccountUpdateRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.UserAuthPlatformsRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.request.UserRolesRequest;
import cn.chenyunlong.qing.domain.auth.admin.dto.response.AdminAccountResponse;
import cn.chenyunlong.qing.domain.auth.admin.dto.updater.AdminAccountUpdater;
import cn.chenyunlong.qing.domain.auth.admin.dto.vo.AdminAccountVO;
import cn.chenyunlong.qing.domain.auth.admin.mapper.AdminAccountMapper;
import cn.chenyunlong.qing.domain.auth.admin.service.IAdminAccountService;
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

@Tag(name = "管理员账号管理")
@RestController
@Slf4j
@RequestMapping("api/v1/admin-account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final IAdminAccountService adminAccountService;

    /**
     * createRequest
     */
    @PostMapping
    public JsonResult<Long> createAdminAccount(@RequestBody AdminAccountCreateRequest request) {
        AdminAccountCreator creator = AdminAccountMapper.INSTANCE.request2Dto(request);
        return JsonResult.success(adminAccountService.createAdminAccount(creator));
    }

    /**
     * update request
     */
    @PostMapping("updateAdminAccount")
    public JsonResult<String> updateAdminAccount(@RequestBody AdminAccountUpdateRequest request) {
        AdminAccountUpdater updater = AdminAccountMapper.INSTANCE.request2Updater(request);
        adminAccountService.updateAdminAccount(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * valid
     */
    @PostMapping("valid/{id}")
    public JsonResult<String> validAdminAccount(@PathVariable Long id) {
        adminAccountService.validAdminAccount(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * invalid
     */
    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidAdminAccount(@PathVariable Long id) {
        adminAccountService.invalidAdminAccount(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * 为用户分配角色
     */
    @PostMapping(value = "assignRolesToUser")
    public JsonResult<String> assignRolesToUser(@RequestBody UserRolesRequest request) {
        adminAccountService.assignRolesToUser(request.getAccountId(), request.getRoleIds());
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * 为用户分配平台
     */
    @PostMapping("assignPlatformsToUser")
    public JsonResult<String> assignPlatformsToUser(@RequestBody UserAuthPlatformsRequest request) {
        adminAccountService.assignPlatformsToUser(request.getPlatformIds(), request.getAccountId());
        return JsonResult.success(CodeEnum.Success.getName());
    }

    /**
     * findById
     */
    @GetMapping("findById/{id}")
    public JsonResult<AdminAccountResponse> findById(@PathVariable Long id) {
        AdminAccountVO vo = adminAccountService.findById(id);
        AdminAccountResponse response = AdminAccountMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    /**
     * findByPage request
     */
    @PostMapping("page")
    public JsonResult<PageResult<AdminAccountResponse>> page(
        @RequestBody
        PageRequestWrapper<AdminAccountQueryRequest> request) {
        PageRequestWrapper<AdminAccountQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(AdminAccountMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<AdminAccountVO> page = adminAccountService.findByPage(wrapper);
        return JsonResult.success(
            PageResult.of(
                page.getContent().stream()
                    .map(AdminAccountMapper.INSTANCE::vo2CustomResponse)
                    .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber())
        );
    }
}
