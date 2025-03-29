package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.auth;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth.QingUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserJpaRepository extends BaseJpaQueryRepository<QingUserEntity, Long> {

    @Query("select u from QingUserEntity u where u.username = ?1")
    QingUser findByUsername(String username);

    @Query("select u from QingUserEntity u where u.uid = ?1")
    QingUser findUserByUserId(String userId);

    @Query("select u from QingUserEntity u where u.nickname in ?1")
    List<QingUser> findByNickNames(Set<String> userNames);

    @Query("select u from QingUserEntity u where u.email = ?1")
    QingUser findByEmail(String email);
}
