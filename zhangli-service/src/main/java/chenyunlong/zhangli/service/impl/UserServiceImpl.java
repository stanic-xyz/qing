package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.UserDAO;
import chenyunlong.zhangli.entities.UserInfo;
import chenyunlong.zhangli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhuzhe
 * @date 2018/6/3 23:38
 * @email 1529949535@qq.com
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<UserInfo> findAll() {
        // 这里我们就可以直接使用 findAll 方法
        return userDAO.findAll();
    }

    @Override
    public UserInfo addUserInfo(UserInfo userInfo) {
        return userDAO.save(userInfo);
    }

}