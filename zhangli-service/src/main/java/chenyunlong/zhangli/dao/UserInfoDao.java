package chenyunlong.zhangli.dao;

import chenyunlong.zhangli.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserInfoDao extends JpaRepository<UserInfo,Long> {
}










