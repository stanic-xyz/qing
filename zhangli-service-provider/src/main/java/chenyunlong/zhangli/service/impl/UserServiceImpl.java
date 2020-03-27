package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User findUserByUserId(Long userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    /**
     * 添加用户信息，注册
     *
     * @param userInfo 用户信息
     * @return
     */
    @Override
    public User register(User userInfo) {
        return null;
    }

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return null;
    }
}
