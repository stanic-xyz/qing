package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.Permission;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.mapper.UserMapper;
import chenyunlong.zhangli.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User findUserByUserId(Long userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User register(User userInfo) {
        return null;
    }

    @Override
    public User login(User user) {
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<Permission> getPermissionByUsername(String username) {
        //TODO 还需要进行实现
        return new ArrayList<>();
    }


}
