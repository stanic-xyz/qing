package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.Permission;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.exception.MyException;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author stan
 * @date 2018/6/3
 */
@Component
public interface UserService {


    /**
     * 通过用户查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User findUserByUserId(Long userId);


    /**
     * 查询用户信息
     */
    List<User> findAll();

    /**
     * 添加用户信息，注册
     *
     * @param userInfo 用户信息
     */
    User register(User userInfo);

    /**
     * 登陆
     *
     * @param user 用户登录信息
     */
    User login(User user);

    /**
     * 根据名称获取用户舒适信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findUserByUsername(String username);

    /**
     * 获取用户权限信息
     *
     * @param username 用户名
     * @return 用户权限信息
     */
    List<Permission> getPermissionByUsername(String username);

    /**
     * 添加用户信息
     *
     * @param user 用户信息
     * @throws MyException 异常
     */
    void addUserInfo(User user) throws MyException;
}