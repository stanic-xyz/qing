package chenyunlong.zhangli.controller.auth;

import chenyunlong.zhangli.anthentication.TokenProvider;
import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.common.exception.AuthenticationException;
import chenyunlong.zhangli.common.exception.BadRequestException;
import chenyunlong.zhangli.common.exception.LoginErrorException;
import chenyunlong.zhangli.common.exception.MyException;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.model.params.LoginParam;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.service.UserService;
import cn.hutool.core.lang.Validator;
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
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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

    @Log("通过表单登陆")
    @ApiOperation("通过表单登陆")
    @PostMapping("formLogin")
    public ApiResult<String> formLoin(@RequestBody LoginParam loginParam) throws LoginErrorException {
        Assert.notNull(loginParam, "Login param must not be null");

        String username = loginParam.getUsername();

        String mismatchTip = "用户名或者密码不正确";
        final User userInfo;
        //判断是通过email登录还是通过用户名登录
        userInfo = Validator.isEmail(username) ? userService.findUserByEmail(username) : userService.findUserByUsername(username);
        if (userInfo == null) {
            throw new BadRequestException(mismatchTip);
        }

        if (!userService.passwordMatch(userInfo, loginParam.getPassword())) {
            throw new BadRequestException(mismatchTip);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return ApiResult.success(tokenProvider.createToken(new UsernamePasswordAuthenticationToken(userInfo.getUsername(), userInfo.getPassword(), authorities), false));
    }

    /**
     * 添加用户信息
     *
     * @param userName 用户名
     * @param password 密码，原始密码，不需要经过加密
     * @return 认证信息
     * @throws MyException 注册异常信息
     */
    @PostMapping("register")
    public ApiResult<Object> register(@RequestParam String userName, @RequestParam String password) throws MyException {
        try {
            userService.addUserInfo(new User(userName, password));
            return ApiResult.success();
        } catch (MyException exp) {
            return ApiResult.faild(exp.getMessage());
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
            return ApiResult.success(tokenProvider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getToken(), authorities), false));
        } else {
            throw new AuthenticationException(authResponse.getMsg());
        }
    }
}
