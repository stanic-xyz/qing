package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.QingUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserJpaRepository extends BaseJpaQueryRepository<QingUserEntity, Long> {

    @Query("select u from QingUserEntity u where u.username = ?1")
    QingUserEntity findByUsername(String username);

    @Query("select u from QingUserEntity u where u.uid = ?1")
    QingUserEntity findUserByUserId(String userId);

    @Query("select u from QingUserEntity u where u.nickname in ?1")
    List<QingUserEntity> findByNickNames(Set<String> userNames);

    @Query("select u from QingUserEntity u where u.email = ?1")
    QingUserEntity findByEmail(String email);

    @Query("select  u from QingUserEntity u where u.username = ?1")
    boolean existsByUsername(String username);
}
