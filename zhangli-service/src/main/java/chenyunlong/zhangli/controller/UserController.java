package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.common.DeferredResultResponse;
import chenyunlong.zhangli.entities.UserInfo;
import chenyunlong.zhangli.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Api(description = "用户接口")
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "获取所有的用户")
    @GetMapping("list")
    public List<UserInfo> findAll() {
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
    public DeferredResultResponse addUser(@RequestBody UserInfo userInfo) {

        userService.addUserInfo(userInfo);
        DeferredResultResponse response = new DeferredResultResponse();
        response.setMsg(DeferredResultResponse.Msg.SUCCESS.getDesc());
        return response;
    }
}

