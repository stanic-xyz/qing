package chenyunlong.zhangli.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("authrize")
public class AuthController {

    @Autowired
    private AuthGithubRequest authRequest;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("auth")
    public String login(
            @RequestBody String code
    ) {

        authRequest.login(code);

        return "登陆状态";
    }

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


        redisTemplate.opsForValue().set(user.getToken().getAccessToken(), user);

        return objectMapper.writeValueAsString(user);
    }

    @GetMapping("getUserInfo")
    public String getUserInfo(@RequestParam String accessToken) throws JsonProcessingException {
        String user = redisTemplate.opsForValue().get(accessToken).toString();

        return user;
    }
}
