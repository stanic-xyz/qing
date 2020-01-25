package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.UserDAO;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.entities.UserInfo;
import chenyunlong.zhangli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public UserInfo findUserByUserId(String userId) {

        UserInfo userInfo = new UserInfo();

        Example<UserInfo> example = Example.of(userInfo);
        Optional<UserInfo> userInfo1 = userDAO.findOne(example);
        return userInfo1.orElse(null);
    }

    @Override
    public int insertUserInfo(UserInfo userInfo) {
        return 0;
    }

}