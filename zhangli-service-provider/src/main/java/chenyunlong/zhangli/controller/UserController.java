package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.anthentication.SecurityUser;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.mapper.UserMapper;
import com.alipay.api.domain.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserMapper userMapper;

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

    @GetMapping("userInfo")
    public void userInfo() {
        SecurityUser currentUser = null;
        Object principl = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principl instanceof UserDetails) {
            currentUser = ((SecurityUser) principl);
        } else {
            logger.info(principl.toString());
        }
    }
}
