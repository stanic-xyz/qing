package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.annotation.Log;
import chenyunlong.zhangli.anthentication.TokenProvider;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.exception.LoginErrorException;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.ApiResult;
import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("authrize")
public class AuthController {

    private final AuthGithubRequest authRequest;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserService userService;
    private final ZhangliProperties zhangliProperties;
    private final TokenProvider tokenProvider;

    public AuthController(AuthGithubRequest authRequest, ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate, UserService userService, ZhangliProperties zhangliProperties, TokenProvider tokenProvider) {
        this.authRequest = authRequest;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.userService = userService;
        this.zhangliProperties = zhangliProperties;
        this.tokenProvider = tokenProvider;
    }

    /**
     * 表单登陆接口
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @Log("通过表单登陆")
    @ApiOperation("通过表单登陆")
    @PostMapping("formLogin")
    public ApiResult formLofin(@RequestParam String userName, @RequestParam String password) throws LoginErrorException {

        if (StringUtil.isNullOrEmpty(userName)) {
            throw new LoginErrorException("用户名不能为空");
        }
        if (StringUtil.isNullOrEmpty(password)) {
            throw new LoginErrorException("密码不能为空");
        }
        User user = new User(userName, password);

        User userInfo = userService.login(user);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return ResultUtil.success(tokenProvider.createToken(new UsernamePasswordAuthenticationToken(userInfo.getUsername(), userInfo.getPassword(), authorities), false));
    }

    /**
     * 跳转到登陆表单
     *
     * @return
     */
    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:" + authRequest.authorize());
        return modelAndView;
    }

    @GetMapping("access_token")
    public String accessToken(@RequestParam String code) throws JsonProcessingException {

        AuthResponse authResponse = authRequest.login(code);
        AuthUser user = (AuthUser) authResponse.getData();

        return tokenProvider.createToken(new UsernamePasswordAuthenticationToken("stan", "stan", null), false);
    }

    @GetMapping("getUserInfo")
    public String getUserInfo(@RequestParam String username) {

        return "test-from 8080";
    }
}
