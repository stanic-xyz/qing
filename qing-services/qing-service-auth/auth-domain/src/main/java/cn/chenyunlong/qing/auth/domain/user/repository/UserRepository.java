package cn.chenyunlong.qing.auth.domain.user.repository;

import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends BaseRepository<User, UserId> {

    Optional<User> findByUsername(Username username);

    Optional<User> findUserByUserId(Long uid);

    List<User> findByUserNames(Set<Username> nickNames);

    Optional<User> findByEmail(Email email);

    boolean existsByUsername(Username username);

    boolean existsByEmail(Email email);

    boolean existsByNicknames(String nickname);
}
