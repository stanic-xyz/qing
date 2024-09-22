package cn.chenyunlong.qing.infrastructure.repository.jpa.auth;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends BaseJpaRepository<QingUser, Long> {

    @Query("select u from QingUser u where u.username = ?1")
    QingUser findByUsername(String username);

    @Query("select u from QingUser u where u.uid = ?1")
    QingUser findUserByUserId(String userId);
    
    @Query("select u from QingUser u where u.nickname in ?1")
    List<QingUser> findByNickNames(Set<String> userNames);

    @Query("select u from QingUser u where u.email = ?1")
    QingUser findByEmail(String email);
}
