package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.UserInfoDao;
import chenyunlong.zhangli.entities.UserInfo;
import chenyunlong.zhangli.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;
    @Override
    public UserInfo getUserInfoByUsername(String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(123L);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username",ExampleMatcher.GenericPropertyMatchers.exact());
        Example<UserInfo> example = Example.of(userInfo);
        return userInfoDao.findOne(example).orElse(null );
    }
}
