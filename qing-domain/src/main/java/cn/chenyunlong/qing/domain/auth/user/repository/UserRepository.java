package cn.chenyunlong.qing.domain.auth.user.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import java.util.List;
import java.util.Set;

public interface UserRepository extends BaseRepository<QingUser, Long> {

    QingUser findByUsername(String username);

    QingUser findUserByUserId(String userId);

    List<QingUser> findByUserNames(Set<String> nickNames);

    QingUser findByEmail(String email);
}
