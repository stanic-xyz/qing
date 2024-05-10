package cn.chenyunlong.qing.domain.auth.user.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends BaseRepository<QingUser, Long> {

    @Query("select u from QingUser u where u.username = ?1")
    QingUser findByUsername(String username);

    @Query("select u from QingUser u where u.uid = ?1")
    QingUser findUserByUserId(String userId);
}
