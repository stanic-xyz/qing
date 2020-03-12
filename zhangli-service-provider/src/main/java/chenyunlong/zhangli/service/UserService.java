package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.User;

import java.util.List;

/**
 * @author stan
 * @date 2018/6/3 23:38
 * @email 1529949535@qq.com
 */
public interface UserService {


    User findUserByUserId(Long userId);


    List<User> findAll();

    /**
     * 添加用户信息，注册
     *
     * @param userInfo 用户信息
     * @return
     */
    User register(User userInfo);

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    User login(User user);
}