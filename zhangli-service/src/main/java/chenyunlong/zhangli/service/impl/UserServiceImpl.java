package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.UserDAO;
import chenyunlong.zhangli.entities.User;
import chenyunlong.zhangli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author stan
 * @date 2018/6/3 23:38
 */
@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public User findUserByUserId(String userId) {

        User userInfo = new User();

        Example<User> example = Example.of(userInfo);
        Optional<User> userInfo1 = userDAO.findOne(example);
        return userInfo1.orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User register(User userInfo) {
        return userDAO.save(userInfo);
    }


    @Override
    public User login(User user) {
        if (user == null) {
            return null;
        }

        Example<User> example = Example.of(user);

        Optional<User> loginUser = userDAO.findOne(example);
        return loginUser.orElse(null);
    }
}