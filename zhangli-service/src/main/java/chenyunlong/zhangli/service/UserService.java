package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.entities.UserInfo;

import java.util.List;

/**
 * @author zhuzhe
 * @date 2018/6/3 23:38
 * @email 1529949535@qq.com
 */
public interface UserService {
    List<User> findAll();

    UserInfo findUserByUserId(String userId);
}