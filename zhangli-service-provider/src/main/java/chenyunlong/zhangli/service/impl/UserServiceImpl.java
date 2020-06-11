package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.Permission;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.mapper.PermissionMapper;
import chenyunlong.zhangli.mapper.UserMapper;
import chenyunlong.zhangli.service.UserService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    ;


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

        User userInfo = userMapper.findByUsername(user.getUsername());

        if (passwordEncoder.matches(user.getPassword(), userInfo.getPassword())) {
            return userInfo;
        }
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<Permission> getPermissionByUsername(String username) {
        return permissionMapper.getPermissionByUsername(username);
    }
}
