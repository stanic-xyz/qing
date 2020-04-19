package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("{userId}/get")
    public String getUserInfo(@PathVariable("userId") String userId) throws JsonProcessingException {
        User userInfo = userMapper.findByState(userId);
        return new ObjectMapper().writeValueAsString(userInfo);
    }

    @GetMapping("list")
    public String list() {
        return "";
    }
}
