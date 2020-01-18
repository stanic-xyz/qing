package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.annotation.Log;
import chenyunlong.zhangli.common.DeferredResultResponse;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.entities.UserInfo;
import chenyunlong.zhangli.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(description = "用户接口")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Log("获取用户信息")
    @GetMapping("userInfo")
    @ApiOperation(value = "获取用户信息")
    public UserInfo getUserInfo(String token) {
        log.debug(token);
        return new UserInfo();
    }

    @ApiOperation(value = "获取所有的用户")
    @GetMapping("list")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("index")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        modelAndView.addObject("test", "thisis test");
        return modelAndView;
    }

    @ApiOperation(value = "添加一个用户", notes = "测试Post接口")
    @PostMapping("post")
    public DeferredResultResponse addUser(@RequestBody User userInfo) {

        userService.register(userInfo);
        DeferredResultResponse response = new DeferredResultResponse();
        response.setMsg(DeferredResultResponse.Msg.SUCCESS.getDesc());
        return response;
    }
}


