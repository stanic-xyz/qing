package chenyunlong.zhangli.service;

import chenyunlong.zhangli.dao.UserInfoDao;
import chenyunlong.zhangli.entities.UserInfo;

public interface UserInfoService {
    public UserInfo getUserInfoByUsername(String username);
}
