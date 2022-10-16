package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.core.exception.*;
import cn.chenyunlong.zhangli.mapper.PermissionMapper;
import cn.chenyunlong.zhangli.mapper.UserMapper;
import cn.chenyunlong.zhangli.model.entities.Permission;
import cn.chenyunlong.zhangli.model.entities.User;
import cn.chenyunlong.zhangli.model.entities.UserThird;
import cn.chenyunlong.zhangli.model.params.LoginParam;
import cn.chenyunlong.zhangli.security.support.TokenProvider;
import cn.chenyunlong.zhangli.service.UserService;
import cn.chenyunlong.zhangli.service.UserThirdService;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author stan
 * @date 2020-09-25
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserThirdService thirdService;


    @Override
    public User findUserByUserId(Long userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    /**
     * 更新用户的密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param userId      用户ID
     * @return 放弃上面的东西
     */
    @Override
    public User updatePassword(String oldPassword, String newPassword, Long userId) {
        Assert.hasText(oldPassword, "Old password must not be blank");
        Assert.hasText(newPassword, "New password must not be blank");
        Assert.notNull(userId, "User id must not be blank");
        if (oldPassword.equals(newPassword)) {
            throw new BadRequestException("新密码和旧密码不能相同");
        }
        User user = userMapper.selectById(userId);
        if (!passwordEncoder.matches(user.getPassword(), oldPassword)) {
            throw new BadRequestException("旧密码错误").setErrorData(oldPassword);
        }
        user.setPassword(newPassword);

        userMapper.updatePassword(user);

        return user;
    }

    @Override
    public User register(User userInfo) {
        return null;
    }

    @Override
    public User login(User user) {

        User userInfo = userMapper.findByUsername(user.getUsername());

        if (userInfo == null) {
            throw new NotFoundException("用户不存在");
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
    public User addUserInfo(User user) throws AbstractException {

        User userInfo = userMapper.findByUsername(user.getUsername());

        if (userInfo != null) {
            throw new LoginErrorException("用户已存在", ErrorCode.USER_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.addUser(user);
        return user;
    }

    /**
     * 根据Email获取用户信息
     *
     * @param email email
     * @return 用户信息
     */
    @Override
    public User findUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    /**
     * 检查密码是否匹配用户的密码
     *
     * @param userInfo 用户信息
     * @param password 输入的密码
     * @return 密码匹配结果
     */
    @Override
    public boolean passwordMatch(@NonNull User userInfo, @Nullable String password) {
        return passwordEncoder.matches(password, userInfo.getPassword());
    }

    @Override
    public User authenticate(LoginParam loginParam) {
        Assert.notNull(loginParam, "Login param must not be null");

        String username = loginParam.getUsername();

        String mismatchTip = "用户名或者密码不正确";
        final User userInfo;
        //判断是通过email登录还是通过用户名登录
        userInfo = Validator.isEmail(username) ? findUserByEmail(username) : findUserByUsername(username);
        if (userInfo == null) {
            throw new BadRequestException(mismatchTip);
        }

        if (!passwordMatch(userInfo, loginParam.getPassword())) {
            throw new BadRequestException(mismatchTip);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return userInfo;
    }

    @Override
    public User getUserInfoByThird(AuthUser authUser) {
        UserThird userThird = thirdService.lambdaQuery()
                .eq(UserThird::getUid, authUser.getUuid())
                .eq(UserThird::getAppType, authUser.getSource())
                .one();
        if (userThird == null) {
            return null;
        }
        userThird.setAccessToken(authUser.getToken().getAccessToken());
        userThird.setAccessExpire(authUser.getToken().getExpireIn());
        thirdService.updateById(userThird);

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>().eq(User::getUid, userThird.getUserId());
        return userMapper.selectOne(lambdaQueryWrapper);
    }

}
