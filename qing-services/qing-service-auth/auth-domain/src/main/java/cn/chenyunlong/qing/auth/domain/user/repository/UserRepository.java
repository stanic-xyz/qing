package cn.chenyunlong.qing.auth.domain.user.repository;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends BaseRepository<QingUser, AggregateId> {

    QingUser findByUsername(String username);

    QingUser findUserByUserId(String userId);

    List<QingUser> findByUserNames(Set<String> nickNames);

    QingUser findByEmail(String email);

    boolean existsByUsername(String username);
}
