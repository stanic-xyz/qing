package chenyunlong.zhangli.controller.auth;

import chenyunlong.zhangli.model.vo.system.UserInfoVO;
import chenyunlong.zhangli.security.support.TokenProvider;
import chenyunlong.zhangli.cache.lock.CacheLock;
import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.common.exception.AuthenticationException;
import chenyunlong.zhangli.common.exception.LoginErrorException;
import chenyunlong.zhangli.exception.AbstractException;
import chenyunlong.zhangli.model.dto.LoginPreCheckDTO;
import chenyunlong.zhangli.model.entities.User;
import chenyunlong.zhangli.model.enums.MFAType;
import chenyunlong.zhangli.model.params.LoginParam;
import chenyunlong.zhangli.model.params.UserParam;
import chenyunlong.zhangli.model.support.ApiResult;
import chenyunlong.zhangli.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Stan
 */
@Api(tags = "认证授权")
@RestController
@RequestMapping("authorize")
public class AuthController {

    private final AuthGithubRequest authRequest;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public AuthController(AuthGithubRequest authRequest, UserService userService, TokenProvider tokenProvider) {
        this.authRequest = authRequest;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("login/precheck")
    @ApiOperation("Login")
    @CacheLock(autoDelete = false, prefix = "login_precheck")
    public LoginPreCheckDTO authPreCheck(@RequestBody @Valid LoginParam loginParam) {
        final User user = userService.authenticate(loginParam);
        return new LoginPreCheckDTO(MFAType.useMFA(user.getMfaType()));
    }

    @Log(title = "通过表单登陆")
    @ApiOperation("通过表单登陆")
    @PostMapping(value = "formLogin")
    public ApiResult<UserInfoVO> formLoin(@RequestBody LoginParam loginParam) throws LoginErrorException {
        User authenticate = userService.authenticate(loginParam);
        UserInfoVO userInfoVO = new UserInfoVO().convertFrom(authenticate);
        String jwtToken = tokenProvider.createJwtToken(userInfoVO, false);
        userInfoVO.setToken(jwtToken);
        return ApiResult.success(userInfoVO);
    }

    @PostMapping("register")
    public ApiResult<User> register(@RequestBody UserParam userParam) throws AbstractException {
        try {
            User user = userService.addUserInfo(userParam.convertTo());
            return ApiResult.success(user);
        } catch (AbstractException exp) {
            return ApiResult.fail(exp.getMessage());
        }
    }

    /**
     * 跳转到登陆表单
     */
    @GetMapping("login")
    public void login(HttpServletResponse response) throws IOException {
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    @GetMapping("callback")
    public ApiResult<String> accessToken(AuthCallback callback) throws JsonProcessingException {
        AuthResponse authResponse = authRequest.login(callback);
        if (authResponse.ok()) {
            AuthUser user = (AuthUser) authResponse.getData();
            List<GrantedAuthority> authorities = new LinkedList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return ApiResult.success(tokenProvider.createJwtToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getToken(), authorities), false));
        } else {
            throw new AuthenticationException(authResponse.getMsg());
        }
    }
}
