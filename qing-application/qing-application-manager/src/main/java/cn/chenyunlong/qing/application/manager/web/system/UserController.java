package cn.chenyunlong.qing.application.manager.web.system;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.dto.query.UserQuery;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserCreateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserQueryRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.request.UserUpdateRequest;
import cn.chenyunlong.qing.domain.auth.user.dto.response.UserResponse;
import cn.chenyunlong.qing.domain.auth.user.dto.updater.UserUpdater;
import cn.chenyunlong.qing.domain.auth.user.dto.vo.UserVO;
import cn.chenyunlong.qing.domain.auth.user.mapper.UserMapper;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import cn.chenyunlong.qing.security.enums.ErrorCodeEnum;
import cn.chenyunlong.qing.security.exception.RegisterUserFailureException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户管理")
@RestController
@Slf4j
@Validated
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping
    public JsonResult<Long> register(
        @RequestBody
        @Validated
        UserCreateRequest request) {
        UserCreator creator = UserMapper.INSTANCE.request2Dto(request);
        Long userId = userService.register(creator).map(QingUser::getId).orElseThrow(() -> new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, "注册失败"));
        return JsonResult.success(userId);
    }

    @PreAuthorize("hasAuthority('admin:modify')")
    @Operation(summary = "更新用户信息")
    @PostMapping("updateUser")
    public JsonResult<String> updateUser(
        @RequestBody
        @Validated
        UserUpdateRequest request) {
        UserUpdater updater = UserMapper.INSTANCE.request2Updater(request);
        userService.updateUser(updater);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("valid/{id}")
    public JsonResult<String> validUser(
        @PathVariable("id")
        Long id) {
        userService.validUser(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @PostMapping("invalid/{id}")
    public JsonResult<String> invalidUser(
        @PathVariable("id")
        Long id) {
        userService.invalidUser(id);
        return JsonResult.success(CodeEnum.Success.getName());
    }

    @GetMapping("findById/{id}")
    public JsonResult<UserResponse> findById(
        @PathVariable("id")
        Long id) {
        UserVO vo = userService.findById(id);
        UserResponse response = UserMapper.INSTANCE.vo2CustomResponse(vo);
        return JsonResult.success(response);
    }

    @PostMapping("page")
    public JsonResult<Page<UserResponse>> page(
        @RequestBody
        PageRequestWrapper<UserQueryRequest> request) {
        PageRequestWrapper<UserQuery> wrapper = new PageRequestWrapper<>();
        wrapper.setBean(UserMapper.INSTANCE.request2Query(request.getBean()));
        wrapper.setSorts(request.getSorts());
        wrapper.setPageSize(request.getPageSize());
        wrapper.setPage(request.getPage());
        Page<UserVO> page = userService.findByPage(wrapper);
        return JsonResult.success(page.map(UserMapper.INSTANCE::vo2CustomResponse));
    }
}
