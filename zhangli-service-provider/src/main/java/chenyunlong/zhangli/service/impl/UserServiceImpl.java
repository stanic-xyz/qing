package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.Permission;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.common.exception.ErrorCode;
import chenyunlong.zhangli.common.exception.MyException;
import chenyunlong.zhangli.mapper.PermissionMapper;
import chenyunlong.zhangli.mapper.UserMapper;
import chenyunlong.zhangli.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author stan
 * @date 2020-09-25
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserMapper userMapper,
                           PermissionMapper permissionMapper,
                           PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.passwordEncoder = passwordEncoder;
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

        String encode = passwordEncoder.encode(user.getUsername());
        if (userInfo == null) {
            return null;
        }
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

    @Override
    public void addUserInfo(User user) throws MyException {

        User userInfo = userMapper.findByUsername(user.getUsername());

        if (userInfo != null) {
            throw new MyException("用户已存在", ErrorCode.USER_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.addUser(user);
    }
}
