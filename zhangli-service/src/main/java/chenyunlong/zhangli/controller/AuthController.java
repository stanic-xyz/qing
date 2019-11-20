package chenyunlong.zhangli.controller;

import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("authrize")
public class AuthController {

    @Autowired
    private AuthRequest authRequest;

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
    public String accessToken(@RequestParam String code) {
        AuthResponse authResponse = authRequest.login(code);
        return authResponse.toString() + '\n' + authResponse.getCode();
    }
}
